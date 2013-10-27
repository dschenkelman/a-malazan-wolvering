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

        [HttpGet]
        public ActionResult Create()
        {
            var viewModel = new DiscountViewModel();

            viewModel.PopulateDiscountTypes();

            viewModel.StartDate = DateTimeHelpers.DateTimeInArgentina;
            viewModel.EndDate = DateTimeHelpers.DateTimeInArgentina.AddDays(7);
            
            return this.View(viewModel);
        }

        [HttpGet]
        public async Task<ActionResult> Edit(int discountId)
        {
            var discount = await this.discountService.GetByIdAsync(discountId);

            var viewModel = DiscountViewModel.FromDiscount(discount);

            viewModel.PopulateDiscountTypes(discount.Type);

            return this.View(viewModel);
        }

        [HttpPost]
        public async Task<ActionResult> Create(DiscountViewModel viewModel)
        {
            if (!ModelState.IsValid)
            {
                viewModel.PopulateDiscountTypes();
                return this.View(viewModel);
            }

            var discount = viewModel.ToDiscount();

            await this.discountService.CreateAsync(discount);

            this.ViewBag.Message = string.Format("La promoción ha sido creada.");
            this.ViewBag.LinkText = "Aceptar";
            this.ViewBag.Action = "Index";
            this.ViewBag.Controller = "Discounts";
            this.ViewBag.RouteValues = new { page = 1 };

            return this.View("~/Views/Shared/Confirmation.cshtml");
        }

        [HttpPost]
        public async Task<ActionResult> Edit(DiscountViewModel viewModel)
        {
            if (!ModelState.IsValid)
            {
                viewModel.PopulateDiscountTypes((DiscountType)viewModel.Type);
                return this.View(viewModel);
            }

            var discount = await this.discountService.GetByIdAsync(viewModel.Id);

            discount.UpdateFrom(viewModel);

            await this.discountService.UpdateAsync(discount);

            this.ViewBag.Message = string.Format("La promoción ha sido actualizada.");
            this.ViewBag.LinkText = "Aceptar";
            this.ViewBag.Action = "Index";
            this.ViewBag.Controller = "Discounts";
            this.ViewBag.RouteValues = new { page = 1 };

            return this.View("~/Views/Shared/Confirmation.cshtml");
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