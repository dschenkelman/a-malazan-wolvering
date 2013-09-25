namespace PhoneTicket.Web.Controllers
{
    using System.Linq;
    using System.Threading.Tasks;
    using System.Web.Mvc;

    using PagedList;

    using PhoneTicket.Web.Handlers;
    using PhoneTicket.Web.Services;
    using PhoneTicket.Web.ViewModels;
    using PhoneTicket.Web.Models;

    [Authorize]
    [RequireSsl]
    public class MoviesController : Controller
    {
        private readonly IMovieService movieService;

        private const int PageSize = 6;

        public MoviesController(IMovieService movieService)
        {
            this.movieService = movieService;
        }

        public async Task<ActionResult> Index(int? page)
        {
            var movies = await this.movieService.GetMoviesAsync();

            var moviesViewModels = movies.Select(ListMovieViewModel.FromUser);

            return this.View(moviesViewModels.ToPagedList(page ?? 1, PageSize));
        }

        public async Task<ActionResult> AddMovie(int movieId)
        {
            Movie movie = null;
            if (movieId > 0)
            {
                movie = await this.movieService.GetMovie(movieId);
            }
            return this.View(movie);
        }

        public ActionResult EditMovie(int movieId)
        {
            return RedirectToAction("AddMovie", "Movies", new { movieId = movieId });
        }

        public ActionResult DeleteMovie(int movieId)
        {
            //TODO - Delete movie from DB
            return RedirectToAction("Index", "Movies", new { page = 1 });
        }

    }
}