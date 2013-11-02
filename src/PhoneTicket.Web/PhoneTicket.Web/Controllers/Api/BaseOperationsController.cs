namespace PhoneTicket.Web.Controllers.Api
{
    using System;
    using System.Drawing.Imaging;
    using System.IO;
    using System.Linq;
    using System.Net;
    using System.Net.Http;
    using System.Net.Mail;
    using System.Net.Mime;
    using System.Threading;
    using System.Threading.Tasks;
    using System.Web.Http;

    using MessagingToolkit.QRCode.Codec;

    using PhoneTicket.Web.Helpers;
    using PhoneTicket.Web.Models;
    using PhoneTicket.Web.Services;
    using PhoneTicket.Web.Templates;
    using PhoneTicket.Web.ViewModels.Api;

    public abstract class BaseOperationsController : ApiController
    {
        private readonly IUserService userService;

        private readonly IEmailService emailService;

        private readonly IOccupiedSeatsService occupiedSeatsService;

        protected BaseOperationsController(
            IOperationService operationService,
            IOccupiedSeatsService occupiedSeatsService,
            IUserService userService,
            IEmailService emailService,
            IShowService showService)
        {
            this.OperationService = operationService;

            this.occupiedSeatsService = occupiedSeatsService;

            this.userService = userService;
            this.emailService = emailService;
            this.ShowService = showService;
        }

        protected IShowService ShowService { get; private set; }

        protected IOperationService OperationService { get; private set; }

        protected async Task<Guid> NewOperation(NewOperationViewModel newOperationViewModel, OperationType type)
        {
            var operations = await this.OperationService.GetAsync(o => o.ShowId == newOperationViewModel.ShowId);

            var occupiedSeats = operations.SelectMany(op => op.OccupiedSeats);

            if (AvailableSeatsHelper.ValidateSeats(newOperationViewModel.ArmChairs, occupiedSeats))
            {
                var user = await this.userService.GetUserAsync(Thread.CurrentPrincipal.Identity.Name);

                var operation = newOperationViewModel.ToOperation(user.Id, type);

                var operationId = await this.OperationService.CreateAsync(operation);

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

                var show = await this.ShowService.GetAsync(operation.ShowId);

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

                        message.Attachments.Add(new Attachment(stream, new ContentType("image/bmp") { Name = "CodigoQR" }));

                        await this.emailService.SendAsync(message);
                    }
                }

                return operationId;
            }

            throw new HttpResponseException(HttpStatusCode.Conflict);
        }
    }
}
