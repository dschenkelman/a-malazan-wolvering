namespace PhoneTicket.Web.Controllers
{
    using System.Collections.Generic;
    using System.Linq;
    using System.Threading.Tasks;
    using System.Web.Mvc;

    using PagedList;

    using PhoneTicket.Web.Models;
    using PhoneTicket.Web.Services;
    using PhoneTicket.Web.ViewModels;

    [Authorize]
    [RequireHttps]
    public class UsersController : Controller
    {
        private const int PageSize = 20;

        private readonly IUserService userService;

        public UsersController(IUserService userService)
        {
            this.userService = userService;
        }

        public async Task<ActionResult> Index(string emailSearch, int? page)
        {
            IEnumerable<User> users; 
            
            if (string.IsNullOrEmpty(emailSearch))
            {
                users = await this.userService.GetUsersAsync();
            }
            else
            {
                users = await this.userService.GetUsersAsync(u => u.EmailAddress.Contains(emailSearch));
            }

            var userViewModels = users.Select(ListUserViewModel.FromUser);
            return this.View(userViewModels.ToPagedList(page ?? 1, PageSize));
        }
    }
}