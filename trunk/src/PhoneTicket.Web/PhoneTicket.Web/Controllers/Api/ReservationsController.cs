namespace PhoneTicket.Web.Controllers.Api
{
    using System;
    using System.Collections.Generic;
    using System.Linq;
    using System.Net.Http;
    using System.Threading;
    using System.Threading.Tasks;
    using System.Web;
    using System.Web.Http;

    using PhoneTicket.Web.Models;
    using PhoneTicket.Web.Services;
    using PhoneTicket.Web.ViewModels.Api;
    using System.Net;
    using PhoneTicket.Web.Helpers;

    [RoutePrefix("api/reservations")]
    public class ReservationsController : ApiController
    {
        private readonly IUserService userService;

        private readonly IOperationService operationService;

        private readonly IOccupiedSeatsService occupiedSeatsService;

        private readonly IOperationDiscountsService operationDiscounts;

        public ReservationsController(IOperationService operationService, IOccupiedSeatsService occupiedSeatsService, IUserService userService, IOperationDiscountsService operationDiscounts)
        {
            this.operationService = operationService;

            this.occupiedSeatsService = occupiedSeatsService;

            this.userService = userService;

            this.operationDiscounts = operationDiscounts;
        }

        [Authorize]
        [HttpPost("")]
        public async Task<HttpResponseMessage> NewReservation(NewOperationViewModel newOperationViewModel)
        {
            var operations = await this.operationService.GetAsync(o => o.ShowId == newOperationViewModel.ShowId);

            var occupiedSeats = operations.SelectMany(op => op.OccupiedSeats);

            if (AvailableSeatsHelper.ValidateSeats(newOperationViewModel.ArmChairs, occupiedSeats))
            {
                var userId = await this.userService.GetIdAsync(Thread.CurrentPrincipal.Identity.Name);

                var operation = new Operation { UserId = userId, Date = DateTimeHelpers.DateTimeInArgentina, ShowId = newOperationViewModel.ShowId,
                                                Type = OperationType.Reservation};

                var operationId = await this.operationService.CreateAsync(operation);

                foreach (ArmChairViewModel wantedSeat in newOperationViewModel.ArmChairs)
                {
                    var newSeat = new OccupiedSeat { OperationId = operationId, Row = wantedSeat.Row, Column = wantedSeat.Column};

                    await this.occupiedSeatsService.CreateAsync(newSeat);

                }

                return new HttpResponseMessage(HttpStatusCode.Created);
            }

            return new HttpResponseMessage(HttpStatusCode.Conflict);
        }

        [Authorize]
        [HttpGet("{id}/cancel")]
        public async Task<HttpResponseMessage> CancelReservation(int id)
        {
            //By cascade, it also deletes occupied seats and discounts referenced to de operation.

            await this.operationService.DeleteAsync(id);

            return new HttpResponseMessage(HttpStatusCode.OK);
        }
    }
}
