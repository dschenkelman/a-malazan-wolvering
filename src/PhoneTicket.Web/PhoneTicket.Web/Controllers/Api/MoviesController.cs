namespace PhoneTicket.Web.Controllers.Api
{
    using System.Collections.Generic;
    using System.Linq;
    using System.Threading.Tasks;
    using System.Web.Http;

    using PhoneTicket.Web.Helpers;
    using PhoneTicket.Web.Models;
    using PhoneTicket.Web.Services;
    using PhoneTicket.Web.ViewModels.Api;

    [RoutePrefix("api/movies")]
    public class MoviesController : ApiController
    {
        private readonly IMovieService movieService;

        private readonly IShowService showService;

        private readonly IOperationService operationService;

        public MoviesController(IMovieService movieService, IShowService showService, IOperationService operationService)
        {
            this.movieService = movieService;
            this.showService = showService;
            this.operationService = operationService;
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
            var withinNextHourShows = await this.showService.GetWithinNextHourForMovieAsync(id);

            var operationsToDelete = await this.operationService.GetReservationsForShowsAsync(withinNextHourShows.Select(s => s.Id).ToArray());

            var toDelete = operationsToDelete as Operation[] ?? operationsToDelete.ToArray();
            foreach (var operation in toDelete)
            {
                // cascades to delete seats
                await this.operationService.DeleteAsync(operation.Number);
            }

            foreach (int showId in toDelete.Select(o => o.ShowId))
            {
                await this.showService.ManageAvailabilityAsync(showId);
            }

            var weeklyShows = await this.showService.GetForMovieAsync(id);
            
            var groupedShows = weeklyShows
                .Where(s => s.Date >= DateTimeHelpers.DateTimeInArgentina
                        && s.Date < DateTimeHelpers.DateTimeInArgentina.AddDays(7) 
                        && s.IsAvailable)
                .OrderBy(s => s.Room.Complex.Id)
                .ThenBy(s => s.Date)
                .Select(s => new
                    {
                        s.Room.Complex.Id,
                        s.Room.Complex.Name,
                        s.Room.Complex.Address,
                        DateAndInfo = new
                        {
                            s.Date,
                            Info = new ShowInfoViewModel
                            {
                                Id = s.Id,
                                Date = s.Date.ToString("yyyy/MM/dd"),
                                Day = s.Date.DayOfWeek.InSpanish(),
                                Time = s.Date.ToString("HH:mm"),
                                RoomId = s.RoomId,
                                ShowPrice = s.Price
                            }
                        }
                    })
                .GroupBy(s => new { s.Id, s.Name, s.Address });

            var showsPerComplex = new List<ShowsPerComplexViewModel>();

            foreach (var groupedShow in groupedShows)
            {
                var showsForThisComplex = new ShowsPerComplexViewModel { ComplexId = groupedShow.Key.Id, ComplexName = groupedShow.Key.Name, ComplexAddress = groupedShow.Key.Address };

                foreach (var show in groupedShow)
                {
                    showsForThisComplex.Functions.Add(
                        new ShowInfoViewModel
                        {
                            Id = show.DateAndInfo.Info.Id,
                            Date = show.DateAndInfo.Info.Date,
                            Day = show.DateAndInfo.Info.Day,
                            Time = show.DateAndInfo.Info.Time,
                            RoomId = show.DateAndInfo.Info.RoomId,
                            ShowPrice = show.DateAndInfo.Info.ShowPrice
                        });
                }

                showsPerComplex.Add(showsForThisComplex);
            }

            return showsPerComplex;
        }
    }
}