namespace PhoneTicket.Web.Controllers.Api
{
    using PhoneTicket.Web.Services;
    using PhoneTicket.Web.ViewModels.Api;
    using System;
    using System.Collections.Generic;
    using System.Linq;
    using System.Threading;
    using System.Threading.Tasks;
    using System.Web;
    using System.Web.Http;

    [RoutePrefix("api/currentUser")]
    public class CurrentUserController : ApiController
    {
        private readonly IUserService userService;

        public CurrentUserController(IUserService userService)
        {
            this.userService = userService;
        }

        [Authorize]
        [HttpGet("info")]
        public async Task<UserInformationViewModel> Info()
        {
            var user = await this.userService.GetUserAsync(Thread.CurrentPrincipal.Identity.Name);

            return (UserInformationViewModel.FromUser(user));
        }
    }

}