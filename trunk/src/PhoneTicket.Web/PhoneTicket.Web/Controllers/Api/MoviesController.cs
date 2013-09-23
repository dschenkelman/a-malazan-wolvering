namespace PhoneTicket.Web.Controllers.Api
{
    
    using System;
    using System.Collections.Generic;
    using System.Linq;
    using System.Web;
    using System.Web.Http;
    using System.Threading.Tasks;


    using PhoneTicket.Web.Services;
    using PhoneTicket.Web.ViewModels.Api;
    using PhoneTicket.Web.Helpers;

    [Authorize]
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
            var movies = await this.movieService.GetMovies();

            return movies.Select(m => m.ToListItemViewModel());
        }
    }
}