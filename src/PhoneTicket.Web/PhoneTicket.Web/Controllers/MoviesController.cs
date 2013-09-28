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

        public async Task<ActionResult> Index(int? page)
        {
            var movies = await this.movieService.GetMoviesAsync();

            var moviesViewModels = movies.Select(ListMovieViewModel.FromUser);

            return this.View(moviesViewModels.ToPagedList(page ?? 1, PageSize));
        }

        public async Task<ActionResult> AddMovie(int movieId)
        {
            Movie movie = null;
            int genreId = -1;
            int ratingId = -1;

            if (movieId > 0)
            {
                movie = await this.movieService.GetMovie(movieId);
                genreId = movie.GenreId;
                ratingId = movie.RatingId;
            }

            IEnumerable<SelectListItem> availableGenres = await this.genreService.GetGenreListAsync(genreId);

            IEnumerable<SelectListItem> availableRatings = await this.ratingService.GetRatingListAsync(ratingId);

            ViewBag.MovieGenreType = availableGenres;
            ViewBag.MovieRatingType = availableRatings;

            
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

        public ActionResult Edit(Movie movie, string MovieGenreType, string MovieRatingType)
        {

            movie.GenreId = Convert.ToInt32(MovieGenreType);

            movie.RatingId = Convert.ToInt32(MovieRatingType);

            if (movie.Id > 0)  //Edit
            {
                this.movieService.UpdateAsync(movie);
            }
            else  //Create
            {
                this.movieService.CreateAsync(movie);
            }

            return RedirectToAction("Index", "Movies", new { page = 1 });
        }
    }
}