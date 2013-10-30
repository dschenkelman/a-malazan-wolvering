namespace PhoneTicket.Web.Controllers.Api
{
    using System;
    using System.Collections.Generic;
    using System.Drawing.Imaging;
    using System.IO;
    using System.Linq;
    using System.Net.Http;
    using System.Net.Mail;
    using System.Net.Mime;
    using System.Threading;
    using System.Threading.Tasks;
    using System.Web;
    using System.Web.Http;

    using MessagingToolkit.QRCode.Codec;

    using PhoneTicket.Web.Models;
    using PhoneTicket.Web.Services;
    using PhoneTicket.Web.Templates;
    using PhoneTicket.Web.ViewModels.Api;
    using System.Net;
    using PhoneTicket.Web.Helpers;

    [RoutePrefix("api/reservations")]
    public class ReservationsController : ApiController
    {
        private readonly IUserService userService;

        private readonly IEmailService emailService;

        private readonly IShowService showService;

        private readonly IOperationService operationService;

        private readonly IOccupiedSeatsService occupiedSeatsService;

        public ReservationsController(
            IOperationService operationService,
            IOccupiedSeatsService occupiedSeatsService,
            IUserService userService,
            IEmailService emailService,
            IShowService showService)
        {
            this.operationService = operationService;

            this.occupiedSeatsService = occupiedSeatsService;

            this.userService = userService;
            this.emailService = emailService;
            this.showService = showService;
        }

        [Authorize]
        [HttpPost("")]
        public async Task<HttpResponseMessage> NewReservation(NewOperationViewModel newOperationViewModel)
        {
            var operations = await this.operationService.GetAsync(o => o.ShowId == newOperationViewModel.ShowId);

            var occupiedSeats = operations.SelectMany(op => op.OccupiedSeats);

            if (AvailableSeatsHelper.ValidateSeats(newOperationViewModel.ArmChairs, occupiedSeats))
            {
                var user = await this.userService.GetUserAsync(Thread.CurrentPrincipal.Identity.Name);

                var operation = new Operation
                                    {
                                        UserId = user.Id,
                                        Date = DateTimeHelpers.DateTimeInArgentina,
                                        ShowId = newOperationViewModel.ShowId,
                                        Type = OperationType.Reservation
                                    };

                var operationId = await this.operationService.CreateAsync(operation);

                foreach (ArmChairViewModel wantedSeat in newOperationViewModel.ArmChairs)
                {
                    var newSeat = new OccupiedSeat
                                      {
                                          OperationId = operationId,
                                          Row = wantedSeat.Row,
                                          Column = wantedSeat.Column
                                      };

                    await this.occupiedSeatsService.CreateAsync(newSeat);
                }

                var show = await this.showService.GetAsync(operation.ShowId);

                var template = new OperationEmailTemplate(user, operation, show);

                var message = this.emailService.CreateMessage(
                    "[CinemAR] Confirmación de operación", template.TransformText(), user.EmailAddress);

                var encoder = new QRCodeEncoder();
                using (var bitmap = encoder.Encode(operation.Number.ToString()))
                {
                    using (var stream = new MemoryStream())
                    {
                        bitmap.Save(stream, ImageFormat.Bmp);

                        stream.Seek(0, SeekOrigin.Begin);

                        message.Attachments.Add(new Attachment(stream, new ContentType("image/bmp"){ Name = "CodigoQR" }));

                        await this.emailService.SendAsync(message);
                    }
                }

                return new HttpResponseMessage(HttpStatusCode.Created);
            }

            return new HttpResponseMessage(HttpStatusCode.Conflict);
        }

        [Authorize]
        [HttpGet("{id}/cancel")]
        public async Task<HttpResponseMessage> CancelReservation(Guid id)
        {
            var showId = (await this.operationService.GetAsync(id)).ShowId;
            
            // By cascade, it also deletes occupied seats and discounts referenced to the operation.
            await this.operationService.DeleteAsync(id);

            await this.showService.ManageAvailability(showId);

            return new HttpResponseMessage(HttpStatusCode.OK);
        }
    }
}
