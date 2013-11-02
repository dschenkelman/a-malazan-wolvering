namespace PhoneTicket.Web.Controllers.Api
{
    using System;
    using System.Net.Http;
    using System.Threading.Tasks;
    using System.Web.Http;

    using PhoneTicket.Web.Models;
    using PhoneTicket.Web.Services;
    using PhoneTicket.Web.ViewModels.Api;

    public class PurchasesController : BaseOperationsController
    {
        public PurchasesController(
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
        public async Task<Guid> NewPurchase(NewOperationViewModel newOperationViewModel)
        {
            return await this.NewOperation(newOperationViewModel, OperationType.PurchaseWithReservation);
        }
    }
}