namespace PhoneTicket.Web.Controllers
{
    using System.Linq;
    using System.Threading.Tasks;
    using System.Web.Mvc;

    using PagedList;

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

        public async Task<ActionResult> Index(int? page)
        {
            var users = await this.userService.GetUsers();
            var userViewModels = users.Select(ListUserViewModel.FromUser);
            return this.View(userViewModels.ToPagedList(page ?? 1, PageSize));
        }
    }
}