namespace PhoneTicket.Web.Controllers.Api
{
    
    using System;
    using System.Collections.Generic;
    using System.Linq;
    using System.Web;
    using System.Web.Http;

    using PhoneTicket.Web.Services;
    using PhoneTicket.Web.ViewModels;
    using System.Threading.Tasks;

    [Authorize]
    [RoutePrefix("api/movies")]
    public class MoviesController : ApiController
    {
        private readonly IMovieService movieService;

        public MoviesController(IMovieService movieService)
        {
            this.movieService = movieService;
        }

        [HttpPost("")]
        public async Task<IEnumerable<ListMovieViewModel>> Get()
        {
            var movies = await this.movieService.GetMovies();

            return movies.Select(m => ListMovieViewModel.FromUser(m));
        }
    }
}