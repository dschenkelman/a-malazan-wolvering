namespace PhoneTicket.Web.Controllers.Api
{
    using System;
    using System.Net;
    using System.Net.Http;
    using System.Threading.Tasks;
    using System.Web.Http;

    using PhoneTicket.Web.Models;
    using PhoneTicket.Web.Services;
    using PhoneTicket.Web.ViewModels.Api;

    [RoutePrefix("api/reservations")]
    public class ReservationsController : BaseOperationsController
    {
        public ReservationsController(
            IOperationService operationService,
            IOccupiedSeatsService occupiedSeatsService,
            IUserService userService,
            IEmailService emailService,
            IShowService showService) 
            : base(operationService, occupiedSeatsService, userService, emailService, showService)
        {
        }

        [Authorize]
        [HttpPost("")]
        public async Task<Guid> NewReservation(NewOperationViewModel newOperationViewModel)
        {
            return await this.NewOperation(newOperationViewModel, OperationType.Reservation);
        }

        [Authorize]
        [HttpDelete("{id}")]
        public async Task<HttpResponseMessage> CancelReservation(Guid id)
        {
            var showId = (await this.OperationService.GetAsync(id)).ShowId;
  
            // By cascade, it also deletes occupied seats and discounts referenced to the operation.
            await this.OperationService.DeleteAsync(id);

            await this.ShowService.ManageAvailabilityAsync(showId);

            return new HttpResponseMessage(HttpStatusCode.OK);
        }

        [Authorize]
        [HttpPost("{id}/confirm")]
        public async Task<IHttpActionResult> ConfirmReservation(Guid id, ConfirmReservationViewModel reservationViewModel)
        {
            var operation = await this.OperationService.GetAsync(id);

            if (operation.Type != OperationType.Reservation)
            {
                return this.BadRequest("Solo se puede confirmar una reserva");
            }

            operation.CreditCardCompanyId = reservationViewModel.CreditCardCompanyId;
            operation.CreditCardExpirationDate = reservationViewModel.CreditCardExpirationDate;
            operation.CreditCardSecurityCode = reservationViewModel.CreditCardSecurityCode;
            operation.CreditCardNumber = reservationViewModel.CreditCardNumber;
            operation.Type = OperationType.PurchaseWithReservation;

            await this.OperationService.SaveAsync(operation);

            return this.Ok();
        }
    }
}
