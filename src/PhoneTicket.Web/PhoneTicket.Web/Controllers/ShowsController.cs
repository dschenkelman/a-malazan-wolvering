﻿namespace PhoneTicket.Web.Controllers
{
    using System;
    using System.Collections.Generic;
    using System.Globalization;
    using System.Linq;
    using System.Threading.Tasks;
    using System.Web.Mvc;

    using PhoneTicket.Web.Constants;
    using PhoneTicket.Web.Handlers;
    using PhoneTicket.Web.Models;
    using PhoneTicket.Web.Services;
    using PhoneTicket.Web.ViewModels;
    using PhoneTicket.Web.Helpers;

    [Authorize]
    [RequireSsl]
    public class ShowsController : Controller
    {
        private readonly IShowService showService;

        private readonly IRoomService roomService;

        private readonly IMovieService movieService;

        private readonly ICurrentUserRole currentUserRole;

        private readonly ISettingsService settingsService;

        public ShowsController(
            IShowService showService,
            IRoomService roomService,
            IMovieService movieService,
            ICurrentUserRole currentUserRole,
            ISettingsService settingsService)
        {
            this.showService = showService;
            this.roomService = roomService;
            this.movieService = movieService;
            this.currentUserRole = currentUserRole;
            this.settingsService = settingsService;
        }
        
        [HttpGet]
        public async Task<ActionResult> Details(int movieId, int showId)
        {
            var show = await this.showService.GetAsync(showId);
            var viewModel = ShowReadOnlyViewModel.FromShow(show);
            viewModel.MovieId = movieId;

            return this.View(viewModel);
        }

        [HttpGet]
        public async Task<ActionResult> ByMovie(int? movieId)
        {
            if (!movieId.HasValue)
            {
                movieId = 1;
            }

            var userCanEdit = this.currentUserRole.IsAdmin();

            ViewBag.CanEdit = userCanEdit;

            var showsGroupedByDate = (await this.showService.GetForMovieAsync(movieId.Value))
                .Select(s => ListShowViewModel.FromShow(s, userCanEdit))
               .OrderBy(s => s.Date)
               .GroupBy(s => s.Date.Date);

            var movies = (await this.movieService.GetMoviesAsync())
                .Select(m => new SelectListItem { Selected = m.Id == movieId, Text = m.Title, Value = m.Id.ToString() });

            return this.View(new ListShowsByMovieViewModel
                                 {
                                     MovieId = movieId.Value, ShowsPerDay = showsGroupedByDate, Movies = movies
                                 });
        }

        [HttpGet]
        public async Task<ActionResult> Create()
        {
            var value = await this.settingsService.GetAsync(SettingsConstants.DefaultShowPrice);

            double defaultPrice = double.Parse(value, CultureInfo.InvariantCulture);

            return this.View(defaultPrice);
        }

        [HttpPost]
        public async Task<JsonResult> Create(CreateShowsViewModel viewModel)
        {
            var beginDate = DateTime.Parse(viewModel.BeginDate);
            var endDate = DateTime.Parse(viewModel.EndDate);

            var selectedDays = viewModel.Days.Where(d => d.IsChecked).ToArray();

            List<Show> showsToAdd = new List<Show>();

            for (var currentDate = beginDate; currentDate <= endDate; currentDate = currentDate.AddDays(1))
            {
                if (!selectedDays.Any(
                        d => d.Day.Equals(currentDate.DayOfWeek.ToString(), StringComparison.CurrentCultureIgnoreCase)))
                {
                    continue;
                }

                foreach (var timeAndRoom in viewModel.TimesAndRooms)
                {
                   var showDate = currentDate
                       .AddHours(timeAndRoom.Hour)
                       .AddMinutes(timeAndRoom.Minutes);

                   var show = new Show 
                                {
                                   Date = showDate,
                                   IsAvailable = true,
                                   MovieId = viewModel.Movie,
                                   RoomId = timeAndRoom.Room,
                                   Price = viewModel.Price
                                };

                    showsToAdd.Add(show);
                }
            }

            await this.showService.Add(showsToAdd.ToArray());

            return this.Json(showsToAdd.Count);
        }

        [HttpGet]
        public async Task<ActionResult> Edit(int showId)
        {
            var show = await this.showService.GetAsync(showId);
            
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

            var existingShow = await this.showService.GetAsync(updatedShow.Id);

            existingShow.MovieId = updatedShow.MovieId;
            existingShow.RoomId = updatedShow.RoomId;
            existingShow.Price = updatedShow.Price;
            existingShow.IsAvailable = updatedShow.IsAvailable;
            existingShow.Date = updatedShow.Date;

            await this.showService.UpdateAsync(existingShow);

            this.ViewBag.Message = string.Format("La función se ha modificado con éxito.");
            this.ViewBag.LinkText = "Aceptar";
            this.ViewBag.Action = "ByMovie";
            this.ViewBag.Controller = "Shows";

            return this.View("~/Views/Shared/Confirmation.cshtml");
        }

        [HttpGet]
        public ActionResult CreationConfirmation()
        {
            this.ViewBag.Message = "Las funciones se han creado con éxito.";
            this.ViewBag.LinkText = "Aceptar";
            this.ViewBag.Action = "ByMovie";
            this.ViewBag.Controller = "Shows";
            return this.View("~/Views/Shared/Confirmation.cshtml");
        }

        [HttpGet]
        public async Task<ActionResult> Delete(int movieId, int showId)
        {
            await this.showService.DeleteAsync(showId);

            this.ViewBag.Message = string.Format("La función ha sido borrada.");
            this.ViewBag.LinkText = "Aceptar";
            this.ViewBag.Action = "ByMovie";
            this.ViewBag.Controller = "Shows";
            this.ViewBag.RouteValues = new { movieId };

            return this.View("~/Views/Shared/Confirmation.cshtml");
        }

        [HttpGet]
        public async Task<ActionResult> ChangeAvailability(int movieId, int showId)
        {
            await this.showService.ChangeAvailability(showId);

            return this.RedirectToAction("ByMovie", new { movieId });
        }
    }
}