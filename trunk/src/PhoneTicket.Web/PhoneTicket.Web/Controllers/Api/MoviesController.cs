namespace PhoneTicket.Web.Controllers.Api
{
    using System.Collections.Generic;
    using System.Linq;
    using System.Web.Http;
    using System.Threading.Tasks;


    using PhoneTicket.Web.Services;
    using PhoneTicket.Web.ViewModels.Api;
    using PhoneTicket.Web.Helpers;

    [RoutePrefix("api/movies")]
    public class MoviesController : ApiController
    {
        private readonly IMovieService movieService;

        public MoviesController(IMovieService movieService)
        {
            this.movieService = movieService;
        }

        [HttpGet]
        public async Task<IEnumerable<MovieListItemViewModel>> Get()
        {
            var movies = await this.movieService.GetMoviesAsync();

            return movies.Select(m => m.ToListItemViewModel());
        }

        [HttpGet("{id}")]
        public async Task<MovieViewModel> Get(int id)
        {
            var movie = await this.movieService.GetAsync(id);

            return movie.ToMovieViewModel();
        }
    }
}