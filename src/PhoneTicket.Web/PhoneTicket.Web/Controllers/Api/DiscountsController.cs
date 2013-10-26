namespace PhoneTicket.Web.Controllers.Api
{
    using System.Collections.Generic;
    using System.Linq;
    using System.Threading.Tasks;
    using System.Web.Http;

    using PhoneTicket.Web.Services;
    using PhoneTicket.Web.ViewModels.Api;

    [RoutePrefix("api/discounts")]
    public class DiscountsController : ApiController
    {
        private readonly IDiscountService discountService;

        public DiscountsController(IDiscountService discountService)
        {
            this.discountService = discountService;
        }

        [HttpGet]
        public async Task<IEnumerable<DiscountInfoViewModel>> Get()
        {
            var activeDiscounts = await this.discountService.GetActive();

            return activeDiscounts.Select(DiscountInfoViewModel.FromDiscount);
        }
    }
}
