namespace PhoneTicket.Web.Controllers
{
    using System.Linq;
    using System.Threading.Tasks;
    using System.Web.Mvc;

    using PhoneTicket.Web.Handlers;
    using PhoneTicket.Web.Services;
    using PhoneTicket.Web.ViewModels;

    [Authorize]
    [RequireSsl]
    public class MoviesController : Controller
    {
        private readonly IMovieService movieService;

        private const int PageSize = 5;

        public MoviesController(IMovieService movieService)
        {
            this.movieService = movieService;
        }

        public async Task<ActionResult> Index()
        {
            var movies = await this.movieService.GetMovies();

            return this.View(movies.Select(ListMovieViewModel.FromUser));
        }
    }
}