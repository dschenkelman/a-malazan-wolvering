namespace PhoneTicket.Web.Controllers.Api
{
    using System;
    using System.Collections.Generic;
    using System.Globalization;
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

        private readonly IShowService showService;

        public MoviesController(IMovieService movieService, IShowService showService)
        {
            this.movieService = movieService;
            this.showService = showService;
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

        [HttpGet("{id}/weeklyShows")]
        public async Task<IEnumerable<ShowsPerComplexViewModel>> WeekShowsForMovie(int id)
        {
            var shows = await this.showService.GetForMovieAsync(id);

            var groupedShows = shows
                .Where(s => s.Date >= DateTime.Now && s.Date < DateTime.Now.AddDays(7) && s.IsAvailable)
                .OrderBy(s => s.Room.Complex.Id)
                .ThenBy(s => s.Date)
                .Select(s => new
                    {
                        s.Room.Complex.Id,
                        s.Room.Complex.Name,
                        DateAndInfo = new { s.Date, Info = new ShowInfoViewModel { Id = s.Id, Day = s.Date.DayOfWeek.InSpanish(), Time = s.Date.ToString("HH:mm") } }
                    })
                .GroupBy(s => new { s.Id, s.Name });

            var showsPerComplex = new List<ShowsPerComplexViewModel>();

            foreach (var groupedShow in groupedShows)
            {
                var showsForThisComplex = new ShowsPerComplexViewModel { ComplexId = groupedShow.Key.Id, ComplexName = groupedShow.Key.Name };

                foreach (var show in groupedShow)
                {
                    showsForThisComplex.Functions.Add(
                        new ShowInfoViewModel { Id = show.DateAndInfo.Info.Id, Day = show.DateAndInfo.Info.Day, Time = show.DateAndInfo.Info.Time });
                }

                showsPerComplex.Add(showsForThisComplex);
            }

            return showsPerComplex;
        }
    }
}