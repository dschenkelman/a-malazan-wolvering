namespace PhoneTicket.Web.Controllers
{
    using System.Collections.Generic;
    using System.Linq;
    using System.Threading.Tasks;
    using System.Web.Mvc;

    using PagedList;

    using PhoneTicket.Web.Handlers;
    using PhoneTicket.Web.Models;
    using PhoneTicket.Web.Services;
    using PhoneTicket.Web.ViewModels;

    [Authorize]
    [RequireSsl]
    public class MoviesController : Controller
    {
        private readonly IMovieService movieService;

        private readonly IGenreService genreService;

        private readonly IRatingService ratingService;

        private const int PageSize = 20;

        public MoviesController(IMovieService movieService, IGenreService genreService, IRatingService ratingService)
        {
            this.movieService = movieService;

            this.genreService = genreService;

            this.ratingService = ratingService;
        }

        public async Task<ActionResult> Index(string titleSearch, int? page)
        {
            IEnumerable<Movie> movies;

            if (string.IsNullOrEmpty(titleSearch))
            {
                movies = await this.movieService.GetMoviesAsync();
            }
            else
            {
                movies = await this.movieService.GetMoviesAsync(m => m.Title.Contains(titleSearch));
            }

            var moviesViewModels = movies.Select(ListMovieViewModel.FromUser);

            return this.View(moviesViewModels.ToPagedList(page ?? 1, PageSize));
        }

        public async Task<ActionResult> Add(int movieId)
        {
            var movie = new Movie() { Id = -1, Title = string.Empty, GenreId = -1, RatingId = -1, Synopsis = string.Empty, TrailerUrl = string.Empty, DurationInMinutes = 0, ImageUrl = string.Empty };
            
            await this.SetUpViewBag(movie);
            
            return this.View(movie);
        }

        public async Task<ActionResult> Edit(int movieId)
        {
            var movie = await this.movieService.GetAsync(movieId);

            await this.SetUpViewBag(movie);

            return this.View(movie);
        }

        public async Task<ActionResult> CreateMovie(Movie movie, int MovieGenreType, int MovieRatingType)
        {
            movie.GenreId = MovieGenreType;

            movie.RatingId = MovieRatingType;

            await this.movieService.CreateAsync(movie);

            return RedirectToAction("Index", "Movies", new { page = 1 });
        }

        public async Task<ActionResult> EditMovie(Movie movie, int MovieGenreType, int MovieRatingType)
        {
            movie.GenreId = MovieGenreType;

            movie.RatingId = MovieRatingType;

            await this.movieService.UpdateAsync(movie);

            return RedirectToAction("Index", "Movies", new { page = 1 });
        }

        public async Task<ActionResult> DeleteMovie(int movieId)
        {
            var movie = await this.movieService.GetAsync(movieId);

            await this.movieService.DeleteAsync(movie);

            return RedirectToAction("Index", "Movies", new { page = 1 });
        }

        private async Task SetUpViewBag(Movie movie)
        {
            IEnumerable<SelectListItem> availableGenres = await this.genreService.ListAsync(movie.GenreId);

            IEnumerable<SelectListItem> availableRatings = await this.ratingService.ListAsync(movie.RatingId);

            ViewBag.MovieGenreType = availableGenres;
            ViewBag.MovieRatingType = availableRatings;
            ViewBag.MovieId = movie.Id;
        }
    }
}