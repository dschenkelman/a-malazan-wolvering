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
    using System.Collections.Generic;
    using System;

    [Authorize]
    [RequireSsl]
    public class MoviesController : Controller
    {
        private readonly IMovieService movieService;

        private readonly IGenreService genreService;

        private readonly IRatingService ratingService;

        private const int PageSize = 6;

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
            Movie movie = new Movie() { Title = "", GenreId = -1, RatingId = -1, Synopsis = "", TrailerUrl = "", DurationInMinutes = 0, ImageUrl = "" };

            await this.SetUpViewBags(movie);
            
            return this.View(movie);
        }

        public async Task<ActionResult> Edit(int movieId)
        {
            var movie = await this.movieService.GetMovie(movieId);

            await this.SetUpViewBags(movie);

            return this.View(movie);

        }

        public ActionResult CreateMovie(Movie movie, string MovieGenreType, string MovieRatingType)
        {
            this.AssembleMovie(movie, MovieGenreType, MovieRatingType);

            this.movieService.CreateAsync(movie);

            return RedirectToAction("Index", "Movies", new { page = 1 });
        }

        public ActionResult EditMovie(Movie movie, string MovieGenreType, string MovieRatingType)
        {
            this.AssembleMovie(movie, MovieGenreType, MovieRatingType);

            this.movieService.UpdateAsync(movie);

            return RedirectToAction("Index", "Movies", new { page = 1 });
        }

        public async Task<ActionResult> DeleteMovie(int movieId)
        {
            var movie = await this.movieService.GetMovie(movieId);

            await this.movieService.DeleteAsync(movie);

            return RedirectToAction("Index", "Movies", new { page = 1 });
        }

        private void AssembleMovie(Movie movie, string MovieGenreType, string MovieRatingType)
        {
            movie.GenreId = Convert.ToInt32(MovieGenreType);

            movie.RatingId = Convert.ToInt32(MovieRatingType);
        }

        private async Task SetUpViewBags(Movie movie)
        {
            IEnumerable<SelectListItem> availableGenres = await this.genreService.GetGenreListAsync(movie.GenreId);

            IEnumerable<SelectListItem> availableRatings = await this.ratingService.GetRatingListAsync(movie.RatingId);

            ViewBag.MovieGenreType = availableGenres;
            ViewBag.MovieRatingType = availableRatings;
            ViewBag.MovieId = movie.Id;
        }
    }
}