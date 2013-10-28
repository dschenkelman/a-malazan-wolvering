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
    

    public class OperationsController : ApiController
    {
        private readonly IUserService userService;

        private readonly IOperationService operationService;

        private readonly IOccupiedSeatsService occupiedSeatsService;

        public OperationsController(IOperationService operationService, IOccupiedSeatsService occupiedSeatsService, IUserService userService)
        {
            this.operationService = operationService;

            this.occupiedSeatsService = occupiedSeatsService;

            this.userService = userService;
        }

        [Authorize]
        [HttpPost("api/currentUser/newReservation")]
        public async Task<HttpResponseMessage> NewReservation(NewOperationViewModel newOperationViewModel)
        {
            var operations = await this.operationService.GetAsync(o => o.ShowId == newOperationViewModel.ShowId);

            var occupiedSeats = operations.SelectMany(op => op.OccupiedSeats);

            if (this.ValidateArmChairs(newOperationViewModel.ArmChairs, occupiedSeats))
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

            return new HttpResponseMessage(HttpStatusCode.Conflict); //409
        }

        private bool ValidateArmChairs(List<ArmChairViewModel> wantedSeats, IEnumerable<OccupiedSeat> occupiedSeats)
        {
            return ( wantedSeats.Count(ws => occupiedSeats.Any(os => os.Row == ws.Row && os.Column == ws.Column)) == 0 );
        }

    }
}
