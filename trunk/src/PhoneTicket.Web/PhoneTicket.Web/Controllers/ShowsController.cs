namespace PhoneTicket.Web.Controllers
{
    using System.Threading.Tasks;
    using System.Web.Mvc;

    using PhoneTicket.Web.Models;
    using PhoneTicket.Web.ViewModels;
    using PhoneTicket.Web.Services;
using System;

    public class ShowsController : Controller
    {
        private readonly IShowService showService;

        private readonly IRoomService roomService;

        private readonly IMovieService movieService;

        public ShowsController(IShowService showService, IRoomService roomService, IMovieService movieService)
        {
            this.showService = showService;
            this.roomService = roomService;
            this.movieService = movieService;
        }

        public ActionResult Index()
        {
            return this.View();
        }

        [HttpGet]
        public ActionResult Create()
        {
            return this.View();
        }

        [HttpPost]
        public JsonResult Create(CreateShowsViewModel viewModel)
        {
            return this.Json(1);
        }

        [HttpGet]
        public async Task<ActionResult> Edit(int showId)
        {
            //-------------
            //TODO - remove mock
            //var show = await this.showService.GetAsync(showId);
            var show = new Show { Id = 1, MovieId = 1, RoomId = 1, Date = new DateTime(2013, 10, 10), Price = 10.0d, IsAvailable = true };
            //-------------
            
            var availableRooms = await this.roomService.SameComplexRoomsListAsync(show.RoomId);
            var availableMovies = await this.movieService.ListAsync(show.MovieId);

            var showViewModel = EditShowViewModel.FromShow(show);
            showViewModel.AvailableMovies = availableMovies;
            showViewModel.AvailableRooms = availableRooms;

            return this.View(showViewModel);
        }

        public async Task<ActionResult> EditShow(EditShowViewModel showViewModel)
        {
            var updatedShow = showViewModel.FromViewModel();


            //--------
            //TODO - remove comments
            //var existingShow = await this.showService.GetAsync(updatedShow.Id);

            //existingShow.MovieId = updatedShow.MovieId;
            //existingShow.RoomId = updatedShow.RoomId;
            //existingShow.Price = updatedShow.Price;
            //existingShow.IsAvailable = updatedShow.IsAvailable;
            //existingShow.Date = updatedShow.Date;

            //await this.showService.UpdateAsync(existingShow);
            //--------

            this.ViewBag.Message = string.Format("La función se ha modificado con éxito.");
            this.ViewBag.LinkText = "Aceptar";
            this.ViewBag.Action = "Index";
            this.ViewBag.Controller = "Shows";

            return this.View("~/Views/Shared/Confirmation.cshtml");
        }

        [HttpGet]
        public ActionResult CreationConfirmation()
        {
            this.ViewBag.Message = "Las funciones se han creado con éxito.";
            this.ViewBag.LinkText = "Aceptar";
            this.ViewBag.Action = "Index";
            this.ViewBag.Controller = "Shows";
            return this.View("~/Views/Shared/Confirmation.cshtml");
        }
    }
}