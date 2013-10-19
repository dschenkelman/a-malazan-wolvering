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

            var moviesViewModels = movies.Select(ListMovieViewModel.FromMovie);

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

            this.ViewBag.Message = string.Format("La película \"{0}\" se ha guardado con éxito.", movie.Title);
            this.ViewBag.LinkText = "Aceptar";
            this.ViewBag.Action = "Index";
            this.ViewBag.Controller = "Movies";

            return this.View("~/Views/Shared/Confirmation.cshtml");
        }

        public async Task<ActionResult> EditMovie(Movie updatedMovie, int MovieGenreType, int MovieRatingType)
        {
            var existingMovie = await this.movieService.GetAsync(updatedMovie.Id);

            existingMovie.ImageUrl = updatedMovie.ImageUrl;
            existingMovie.DurationInMinutes = updatedMovie.DurationInMinutes;
            existingMovie.GenreId = MovieGenreType;
            existingMovie.RatingId = MovieRatingType;
            existingMovie.Synopsis = updatedMovie.Synopsis;
            existingMovie.Title = updatedMovie.Title;
            existingMovie.TrailerUrl = updatedMovie.TrailerUrl;

            await this.movieService.UpdateAsync(existingMovie);
            
            this.ViewBag.Message = string.Format("La película \"{0}\" se ha guardado con éxito.", existingMovie.Title);
            this.ViewBag.LinkText = "Aceptar";
            this.ViewBag.Action = "Index";
            this.ViewBag.Controller = "Movies";

            return this.View("~/Views/Shared/Confirmation.cshtml");
        }

        public async Task<ActionResult> DeleteMovie(int movieId)
        {
            await this.movieService.DeleteAsync(movieId);

            this.ViewBag.Message = string.Format("La película ha sido borrada.");
            this.ViewBag.LinkText = "Aceptar";
            this.ViewBag.Action = "Index";
            this.ViewBag.Controller = "Movies";
            this.ViewBag.RouteValues = new { page = 1 };

            return this.View("~/Views/Shared/Confirmation.cshtml");
        }

        public async Task<ActionResult> Details(int movieId)
        {
            var movie = await this.movieService.GetAsync(movieId);

            var movieViewModel = MovieReadOnlyViewModel.FromMovie(movie);

            return this.View(movieViewModel);
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