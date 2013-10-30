namespace PhoneTicket.Web.Controllers.Api
{
    using System;
    using System.Collections.Generic;
    using System.Linq;
    using System.Threading.Tasks;
    using System.Web.Http;

    using PhoneTicket.Web.Models;
    using PhoneTicket.Web.Services;
    using PhoneTicket.Web.ViewModels.Api;

    [Authorize]
    public class DiscountsController : ApiController
    {
        private readonly IDiscountService discountService;

        private readonly IOperationService operationService;

        public DiscountsController(IDiscountService discountService, IOperationService operationService)
        {
            this.discountService = discountService;
            this.operationService = operationService;
        }

        [HttpGet("api/discounts")]
        public async Task<IEnumerable<DiscountInfoViewModel>> Get()
        {
            var activeDiscounts = await this.discountService.GetActiveAsync();

            return activeDiscounts.Select(DiscountInfoViewModel.FromDiscount);
        }

        [HttpPost("api/operations/{opNumber}/discounts")]
        public async Task<IHttpActionResult> SetDiscounts(Guid opNumber, IEnumerable<DiscountForOperationViewModel> discountsForOperation)
        {
            var operation = (await this.operationService.GetAsync(o => o.Number == opNumber)).FirstOrDefault();

            if (operation == null)
            {
                return this.BadRequest("La operación no existe");
            }

            var discountForOperationViewModels = discountsForOperation as DiscountForOperationViewModel[] ?? discountsForOperation.ToArray();
            var discountIds = discountForOperationViewModels.Select(dfo => dfo.DiscountId).ToArray();

            var discounts = await this.discountService.GetByIdsAsync(discountIds);

            var enumerable = discounts as Discount[] ?? discounts.ToArray();
            if (enumerable.Sum(d => d.RelatedTickets * discountForOperationViewModels.First(dfo => dfo.DiscountId == d.Id).Count)
                > operation.OccupiedSeats.Count())
            {
                return this.BadRequest("La cantidad de asientos de la promociones es mayor a la cantidad de asientos reservados");
            }
            
            await this.operationService.AddDiscountsAsync(operation, discountForOperationViewModels);

            return this.Ok();
        }
    }
}
