namespace PhoneTicket.Web.Controllers.Api
{
    using System;
    using System.Collections.Generic;
    using System.Linq;
    using System.Threading;
    using System.Threading.Tasks;
    using System.Web;
    using System.Web.Http;

    using PhoneTicket.Web.Models;
    using PhoneTicket.Web.Services;
    using PhoneTicket.Web.ViewModels.Api;

    [RoutePrefix("api/currentUser")]
    public class CurrentUserController : ApiController
    {
        private readonly IUserService userService;

        private readonly IOperationService operationService;

        public CurrentUserController(IUserService userService, IOperationService operationService)
        {
            this.userService = userService;

            this.operationService = operationService;
        }

        [Authorize]
        [HttpGet("info")]
        public async Task<UserInformationViewModel> Info()
        {
            var user = await this.userService.GetUserAsync(Thread.CurrentPrincipal.Identity.Name);

            return (UserInformationViewModel.FromUser(user));
        }

        [Authorize]
        [HttpGet("operations")]
        public async Task<IEnumerable<UserOperationsViewModel>> Operations()
        {
            var userId = await this.userService.GetIdAsync(Thread.CurrentPrincipal.Identity.Name);

            var operations = await this.operationService.GetAsync(o => o.UserId == userId);

            var viewModel = operations.Select(o => new UserOperationsViewModel { Id = o.Number, IsBought = (!o.Type.Equals(OperationType.Reservation)), 
                                                                                MovieTitle = o.Show.Movie.Title, ShowDateAndTime = o.Show.Date.ToString("dd/MM HH:mm")+"Hs", 
                                                                                ComplexAddress = o.Show.Room.Complex.Address });

            return viewModel;
        }

        [Authorize]
        [HttpGet("operations/{id}")]
        public async Task<OperationDetailViewModel> Operations(Guid id)
        {
            var op = await this.operationService.GetAsync(id);

            var seats = op.OccupiedSeats.Select(ArmChairViewModel.FromOccupiedSeat).ToList();

            var discounts = op.OperationDiscounts.Select(OperationDiscountDetailViewModel.FromOperationDiscount).ToList();

            var viewModel = new OperationDetailViewModel
            {
                MovieTitle = op.Show.Movie.Title,
                ShowDateAndTime = op.Show.Date.ToString("dd/MM HH:mm") + "Hs",
                ComplexAddress = op.Show.Room.Complex.Address,
                ShowPrice = op.Show.Price,
                Seats = seats,
                Discounts = discounts
            };

            return viewModel;
        }
    }

}