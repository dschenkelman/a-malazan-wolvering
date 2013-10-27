namespace PhoneTicket.Web.Controllers
{
    using System.Collections.Generic;
    using System.Linq;
    using System.Threading.Tasks;
    using System.Web.Mvc;

    using PagedList;

    using PhoneTicket.Web.Handlers;
    using PhoneTicket.Web.Helpers;
    using PhoneTicket.Web.Models;
    using PhoneTicket.Web.Services;
    using PhoneTicket.Web.ViewModels;

    [Authorize]
    [RequireSsl]
    public class DiscountsController : Controller
    {
        private const int PageSize = 10;

        private readonly IDiscountService discountService;

        private readonly ICurrentUserRole userRole;

        public DiscountsController(IDiscountService discountService, ICurrentUserRole userRole)
        {
            this.discountService = discountService;
            this.userRole = userRole;
        }

        public async Task<ActionResult> Index(string descriptionSearch, int? page)
        {
            IEnumerable<Discount> discounts;

            if (string.IsNullOrEmpty(descriptionSearch))
            {
                discounts = await this.discountService.GetActiveAndFutureAsync();
            }
            else
            {
                discounts = await this.discountService.GetActiveAndFutureAsync(d => d.Description.Contains(descriptionSearch));
            }

            var isAdmin = this.userRole.IsAdmin();

            ViewBag.CanEdit = isAdmin;

            var discountsViewModels = discounts.Select(d => ListDiscountViewModel.FromDiscount(d, isAdmin));

            return this.View(discountsViewModels.ToPagedList(page ?? 1, PageSize));
        }

        public async Task<ActionResult> Delete(int discountId)
        {
            await this.discountService.DeleteAsync(discountId);

            this.ViewBag.Message = string.Format("La promoción ha sido borrada.");
            this.ViewBag.LinkText = "Aceptar";
            this.ViewBag.Action = "Index";
            this.ViewBag.Controller = "Discounts";
            this.ViewBag.RouteValues = new { page = 1 };

            return this.View("~/Views/Shared/Confirmation.cshtml");
        }
    }
}