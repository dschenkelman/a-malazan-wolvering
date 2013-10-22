namespace PhoneTicket.Web.Tests.Controllers
{
    using System;
    using System.Collections.Generic;
    using System.Linq;
    using System.Threading.Tasks;
    using System.Web.Mvc;

    using Microsoft.VisualStudio.TestTools.UnitTesting;

    using Moq;

    using PhoneTicket.Web.Controllers;
    using PhoneTicket.Web.Models;
    using PhoneTicket.Web.Services;
    using PhoneTicket.Web.ViewModels;
    using PhoneTicket.Web.Helpers;

    [TestClass]
    public class ShowsControllerTests
    {
        private MockRepository mockRepository;
        private Mock<IShowService> showService;
        private Mock<IRoomService> roomService;
        private Mock<IMovieService> movieService;
        private Mock<ICurrentUserRole> currentUserRole;

        [TestInitialize]
        public void Initialize()
        {
            this.mockRepository = new MockRepository(MockBehavior.Default);
            this.showService = this.mockRepository.Create<IShowService>();
            this.roomService = this.mockRepository.Create<IRoomService>();
            this.movieService = this.mockRepository.Create<IMovieService>();
            this.currentUserRole = this.mockRepository.Create<ICurrentUserRole>();
        }

        [TestMethod]
        public async Task ShouldCreateNoShowsIfDaysSelectedDoNotExistWithinDatesSelected()
        {
            const string DateFormat = "yyyy-MM-dd";
            // tuesday
            var beginDate = new DateTime(2013, 10, 15);
            // thursday
            var endDate = new DateTime(2013, 10, 17);
            const int MovieId = 2;
            const double Price = 20.0;

            var daysViewModel = new List<DayViewModel>
                                    {
                                        new DayViewModel { Day = "monday", IsChecked = true },
                                        new DayViewModel { Day = "saturday", IsChecked = true }
                                    };

            var timesAndRooms = new List<TimesAndRoomViewModel>
                                    {
                                        new TimesAndRoomViewModel()
                                            {
                                                Hour = 1,
                                                Minutes = 1,
                                                Room = 1
                                            }
                                    };

            var viewModel = new CreateShowsViewModel()
                                {
                                    BeginDate = beginDate.ToString(DateFormat),
                                    EndDate = endDate.ToString(DateFormat),
                                    Movie = MovieId,
                                    Days = daysViewModel,
                                    Price = Price,
                                    TimesAndRooms = timesAndRooms
                                };

            this.showService.Setup(ss => ss.Add(It.Is<Show[]>(s => s.Length == 0))).Returns(Task.FromResult<object>(null)).Verifiable();

            var controller = this.CreateController();

            var result = await controller.Create(viewModel);

            this.showService.Verify(ss => ss.Add(It.Is<Show[]>(s => s.Length == 0)), Times.Once());

            Assert.AreEqual(0, (int)result.Data);
        }

        [TestMethod]
        public async Task ShouldCreateShowsForDaysSelectedWithinDatesSelected()
        {
            const string DateFormat = "yyyy-MM-dd";
            // tuesday
            var beginDate = new DateTime(2013, 10, 15);
            // monday
            var endDate = new DateTime(2013, 10, 21);
            const int MovieId = 2;
            const double Price = 20.0;

            var timesAndRooms = new List<TimesAndRoomViewModel>
                                    {
                                        new TimesAndRoomViewModel()
                                            {
                                                Hour = 12,
                                                Minutes = 10,
                                                Room = 1
                                            },
                                            new TimesAndRoomViewModel()
                                            {
                                                Hour = 22,
                                                Minutes = 45,
                                                Room = 2
                                            }
                                    };

            var daysViewModel = new List<DayViewModel>
                                    {
                                        new DayViewModel { Day = "monday", IsChecked = true },
                                        new DayViewModel { Day = "tuesday", IsChecked = false },
                                        new DayViewModel { Day = "wednesday", IsChecked = false },
                                        new DayViewModel { Day = "thursday", IsChecked = true },
                                        new DayViewModel { Day = "friday", IsChecked = false },
                                        new DayViewModel { Day = "saturday", IsChecked = true },
                                        new DayViewModel { Day = "sunday", IsChecked = false },
                                    };

            var thursdayFirstShow = beginDate.AddDays(2).AddHours(timesAndRooms[0].Hour).AddMinutes(timesAndRooms[0].Minutes);
            var thursdaySecondShow = beginDate.AddDays(2).AddHours(timesAndRooms[1].Hour).AddMinutes(timesAndRooms[1].Minutes);
            var saturdayFirstShow = beginDate.AddDays(4).AddHours(timesAndRooms[0].Hour).AddMinutes(timesAndRooms[0].Minutes);
            var saturdaySecondShow = beginDate.AddDays(4).AddHours(timesAndRooms[1].Hour).AddMinutes(timesAndRooms[1].Minutes);
            var mondayFirstShow = beginDate.AddDays(6).AddHours(timesAndRooms[0].Hour).AddMinutes(timesAndRooms[0].Minutes);
            var mondaySecondShow = beginDate.AddDays(6).AddHours(timesAndRooms[1].Hour).AddMinutes(timesAndRooms[1].Minutes);

            var viewModel = new CreateShowsViewModel()
            {
                BeginDate = beginDate.ToString(DateFormat),
                EndDate = endDate.ToString(DateFormat),
                Movie = MovieId,
                Days = daysViewModel,
                Price = Price,
                TimesAndRooms = timesAndRooms
            };

            this.showService.Setup(ss => ss.Add(It.Is<Show[]>(s =>
                s.Length == 6
                && s[0].Date == thursdayFirstShow && s[0].RoomId == 1 && s[0].IsAvailable && s[0].Price == Price && s[0].MovieId == MovieId
                && s[1].Date == thursdaySecondShow && s[1].RoomId == 2 && s[1].IsAvailable && s[1].Price == Price && s[1].MovieId == MovieId
                && s[2].Date == saturdayFirstShow && s[2].RoomId == 1 && s[2].IsAvailable && s[2].Price == Price && s[2].MovieId == MovieId
                && s[3].Date == saturdaySecondShow && s[3].RoomId == 2 && s[3].IsAvailable && s[3].Price == Price && s[3].MovieId == MovieId
                && s[4].Date == mondayFirstShow && s[4].RoomId == 1 && s[4].IsAvailable && s[4].Price == Price && s[4].MovieId == MovieId
                && s[5].Date == mondaySecondShow && s[5].RoomId == 2 && s[5].IsAvailable && s[5].Price == Price && s[5].MovieId == MovieId)))
                .Returns(Task.FromResult<object>(null))
                .Verifiable();

            var controller = this.CreateController();

            var result = await controller.Create(viewModel);

            this.showService.VerifyAll();

            Assert.AreEqual(6, (int)result.Data);
        }

        public async Task ShouldSetShowPropertiesToEditShowViewModelPropertiesWhenCallingEdit()
        {
            const int ShowId = 1;
            const int MovieId = 1;
            const int RoomId = 1;
            var date = new DateTime(2013, 10, 10);
            var dateToString = date.ToString("yyyy-MM-dd");
            const double Price = 10.0;
            const bool IsAvailable = true;

            var movies = new List<SelectListItem>();
            var rooms = new List<SelectListItem>();

            var show = new Show
                           {
                               Id = ShowId,
                               MovieId = MovieId,
                               RoomId = RoomId,
                               Date = date,
                               Price = Price,
                               IsAvailable = IsAvailable
                           };

            this.showService.Setup(ss => ss.GetAsync(ShowId)).Returns(Task.FromResult(show));
            this.roomService.Setup(rs => rs.SameComplexRoomsListAsync(It.IsAny<int>()))
                .Returns(Task.FromResult<IEnumerable<SelectListItem>>(rooms));
            this.movieService.Setup(rs => rs.ListAsync(It.IsAny<int>()))
                .Returns(Task.FromResult<IEnumerable<SelectListItem>>(movies));

            var controller = this.CreateController();

            var result = (ViewResult)await controller.Edit(ShowId);

            var showObtained = (EditShowViewModel)result.Model;

            Assert.AreEqual(ShowId, showObtained.Id);
            Assert.AreEqual(IsAvailable, showObtained.IsAvailable);
            Assert.AreEqual(MovieId, showObtained.MovieId);
            Assert.AreEqual(RoomId, showObtained.RoomId);
            Assert.AreEqual(Price, showObtained.Price);
            Assert.AreEqual(dateToString, showObtained.Date);

            Assert.AreSame(rooms, showObtained.AvailableRooms);
            Assert.AreSame(movies, showObtained.AvailableMovies);
        }

        [TestMethod]
        public async Task ShouldUpdateShowRetrievedFromServiceBasedOnUpdatedShowProperties()
        {
            const int ShowId = 1;
            const int MovieId = 1;
            const int RoomId = 1;
            var date = new DateTime(2013, 10, 10);
            date = date.AddHours(12).AddMinutes(30);
            const double Price = 10.0;
            const bool IsAvailable = true;

            var updatedShow = new Show
            {
                Id = ShowId,
                MovieId = MovieId,
                RoomId = RoomId,
                Date = date,
                Price = Price,
                IsAvailable = IsAvailable
            };

            var existingShow = new Show { Id = ShowId };

            var controller = this.CreateController();

            this.showService.Setup(ss => ss.GetAsync(ShowId)).Returns(Task.FromResult(existingShow)).Verifiable();
            this.showService.Setup(ss => ss.UpdateAsync(existingShow)).Returns(Task.FromResult<object>(null)).Verifiable();

            await controller.EditShow(EditShowViewModel.FromShow(updatedShow));

            this.showService.Verify(rs => rs.GetAsync(updatedShow.Id), Times.Once());

            this.showService.Verify(rs => rs.UpdateAsync(It.Is<Show>(s => s.Id == ShowId
                                                                        && s.Date == date
                                                                        && s.IsAvailable == IsAvailable
                                                                        && s.RoomId == RoomId
                                                                        && s.Price == Price
                                                                        && s.MovieId == MovieId)),
                                                                    Times.Once());
        }

        [TestMethod]
        public async Task ShouldReturnMoviesOrderedAndGroupedByDateWhenCallingByMovie()
        {
            var day1Show1Date = new DateTime(2013, 10, 14);
            var day1Show2Date = day1Show1Date.AddHours(3);
            var day2Show1Date = new DateTime(2013, 10, 15);
            var day2Show2Date = day2Show1Date.AddHours(15);
            var day3Show1Date = new DateTime(2013, 10, 16);
            var day3Show2Date = day3Show1Date.AddHours(7);

            const int MovieId = 1;

            var shows = new List<Show>
                            {
                                new Show { Date = day1Show1Date },
                                new Show { Date = day2Show2Date },
                                new Show { Date = day3Show2Date },
                                new Show { Date = day3Show1Date },
                                new Show { Date = day2Show1Date },
                                new Show { Date = day1Show2Date },
                            };
            
            var movies = new List<Movie>();

            this.movieService.Setup(ms => ms.GetMoviesAsync()).Returns(Task.FromResult((IEnumerable<Movie>)movies));

            this.showService.Setup(ss => ss.GetForMovieAsync(MovieId)).Returns(Task.FromResult((IEnumerable<Show>)shows)).Verifiable();

            var controller = this.CreateController();

            var result = (ViewResult)await controller.ByMovie(1);

            var viewModel = (ListShowsByMovieViewModel)result.Model;
            var returnedShows = viewModel.ShowsPerDay;

            Assert.AreEqual(3, returnedShows.Count());

            var firstDayShows = returnedShows.ElementAt(0);

            Assert.AreEqual(day1Show1Date, firstDayShows.First().Date);
            Assert.AreEqual(day1Show2Date, firstDayShows.Last().Date);

            var secondDayShows = returnedShows.ElementAt(1);

            Assert.AreEqual(day2Show1Date, secondDayShows.First().Date);
            Assert.AreEqual(day2Show2Date, secondDayShows.Last().Date);

            var thirdDayShows = returnedShows.ElementAt(2);

            Assert.AreEqual(day3Show1Date, thirdDayShows.First().Date);
            Assert.AreEqual(day3Show2Date, thirdDayShows.Last().Date);

            this.showService.VerifyAll();
        }

        [TestMethod]
        public async Task ShouldNotBeAbleToEditIfDateIsInThePast()
        {
            // ideally we should mock the DateTime.Today component for this test
            // if it fails, check the date in your OS
            var showDate = new DateTime(2013, 10, 13);
            const int MovieId = 1;

            var shows = new List<Show>
                            {
                                new Show { Date = showDate },
                            };

            var movies = new List<Movie>();

            this.movieService.Setup(ms => ms.GetMoviesAsync()).Returns(Task.FromResult((IEnumerable<Movie>)movies));
            this.showService.Setup(ss => ss.GetForMovieAsync(MovieId)).Returns(Task.FromResult((IEnumerable<Show>)shows)).Verifiable();

            var controller = this.CreateController();

            var result = (ViewResult)await controller.ByMovie(MovieId);
            var viewModel = (ListShowsByMovieViewModel)result.Model;
            var returnedShows = viewModel.ShowsPerDay;

            Assert.AreEqual(1, returnedShows.Count());

            Assert.IsFalse(returnedShows.First().First().CanEdit);

            this.showService.VerifyAll();
        }

        [TestMethod]
        public async Task ShouldBeAbleToEditIfDateIsInTheFuture()
        {
            // ideally we should mock the DateTime.Today component for this test
            // if it fails, check the date in your OS
            var showDate = new DateTime(2020, 10, 13);
            const int MovieId = 1;

            var shows = new List<Show>
                            {
                                new Show { Date = showDate },
                            };

            var movies = new List<Movie>();

            this.movieService.Setup(ms => ms.GetMoviesAsync()).Returns(Task.FromResult((IEnumerable<Movie>)movies));

            this.showService.Setup(ss => ss.GetForMovieAsync(MovieId)).Returns(Task.FromResult((IEnumerable<Show>)shows)).Verifiable();

            this.currentUserRole.Setup(ur => ur.UserIsAdmin()).Returns(true);

            var controller = this.CreateController();

            var result = (ViewResult)await controller.ByMovie(MovieId);
            var viewModel = (ListShowsByMovieViewModel)result.Model;
            var returnedShows = viewModel.ShowsPerDay;

            Assert.AreEqual(1, returnedShows.Count());

            Assert.IsTrue(returnedShows.First().First().CanEdit);

            this.showService.VerifyAll();
        }

        [TestMethod]
        public async Task ShouldSetViewModelPropertiesFromShow()
        {
            var showDate = new DateTime(2020, 10, 13);
            const bool IsAvailable = true;
            const int MovieId = 1;
            const int ShowId = 20;

            var room = new Room { Complex = new Complex { Name = "Caballito" }, Name = "Room1" };

            var shows = new List<Show>
                            {
                                new Show 
                                { 
                                    Date = showDate, 
                                    IsAvailable = IsAvailable, 
                                    Id = ShowId, 
                                    Room = room
                                },
                            };

            var movies = new List<Movie>();

            this.movieService.Setup(ms => ms.GetMoviesAsync()).Returns(Task.FromResult((IEnumerable<Movie>)movies));

            this.showService.Setup(ss => ss.GetForMovieAsync(MovieId)).Returns(Task.FromResult((IEnumerable<Show>)shows)).Verifiable();

            var controller = this.CreateController();

            var result = (ViewResult)await controller.ByMovie(MovieId);
            var viewModel = (ListShowsByMovieViewModel)result.Model;
            var returnedShowDays = viewModel.ShowsPerDay;

            Assert.AreEqual(1, returnedShowDays.Count());

            var show = returnedShowDays.First().First();

            Assert.AreEqual("Caballito", show.Complex);
            Assert.AreEqual("Room1", show.Room);
            Assert.AreEqual("Room1", show.Room);
            Assert.AreEqual(showDate, show.Date);
            Assert.AreEqual(showDate.ToString("HH:mm"), show.Time);
            Assert.IsTrue(show.IsAvailable);

            this.showService.VerifyAll();
        }

        [TestMethod]
        public async Task ShouldDeleteShowFromServiceWhenDeletingMovie()
        {
            const int MovieId = 1;
            const int ShowId = 20;

            var controller = this.CreateController();

            this.showService.Setup(ss => ss.DeleteAsync(ShowId)).Returns(Task.FromResult<object>(null)).Verifiable();

            await controller.Delete(MovieId, ShowId);

            this.showService.VerifyAll();
        }

        [TestMethod]
        public async Task ShouldCallChangeAvailabilityInShowServiceWhenChangeAvailabilityIsCalled()
        {
            const int MovieId = 1;
            const int ShowId = 20;

            this.showService.Setup(ss => ss.ChangeAvailability(ShowId)).Returns(Task.FromResult<object>(null)).Verifiable();

            var controller = this.CreateController();

            await controller.ChangeAvailability(MovieId, ShowId);

            this.showService.VerifyAll();
        }

        [TestMethod]
        public async Task ShouldRetrieveShowFromServiceWhenDetailsIsCalled()
        {
            const int MovieId = 1;
            const int ShowId = 20;

            this.showService.Setup(ss => ss.GetAsync(ShowId)).Returns(Task.FromResult(new Show())).Verifiable();

            var controller = this.CreateController();

            await controller.Details(MovieId, ShowId);

            this.showService.VerifyAll();
        }

        [TestMethod]
        public async Task ShouldSetViewModelPropertiesBasedOnShowReturnedFromService()
        {
            const int MovieId = 1;
            const int ShowId = 20;

            var show = new Show 
                            {
                               Date = new DateTime(2013, 10, 14),
                               Id = 1,
                               IsAvailable = true,
                               Movie = new Movie { Title = "Title" },
                               Room = new Room { Complex = new Complex { Name = "Complex" }, Name = "Room" },
                               Price = 20
                            };

            this.showService.Setup(ss => ss.GetAsync(ShowId)).Returns(Task.FromResult(show)).Verifiable();

            var controller = this.CreateController();

            var result = (ViewResult) await controller.Details(MovieId, ShowId);

            var viewModel = (ShowReadOnlyViewModel)result.Model;

            Assert.AreEqual(show.Room.Name, viewModel.Room);
            Assert.AreEqual(show.Room.Complex.Name, viewModel.Complex);
            Assert.AreEqual(show.Price, viewModel.Price);
            Assert.AreEqual(show.Date, viewModel.Date);
            Assert.AreEqual(show.Date.ToString("HH:mm"), viewModel.Time);
            Assert.AreEqual(show.Movie.Title, viewModel.Movie);
            Assert.AreEqual(MovieId, viewModel.MovieId);

            this.showService.VerifyAll();
        }

        [TestMethod]
        public async Task ShouldReturnMovieListInComboWhenByMovieIsInvoked()
        {
            const int MovieId = 2;

            var movies = new List<Movie> { new Movie { Id = 1 }, new Movie { Id = 2 }, new Movie { Id = 3 } };

            this.movieService.Setup(ms => ms.GetMoviesAsync()).Returns(Task.FromResult((IEnumerable<Movie>)movies)).Verifiable();

            this.showService.Setup(ss => ss.GetForMovieAsync(MovieId)).Returns(Task.FromResult(Enumerable.Empty<Show>())).Verifiable();

            var controller = this.CreateController();

            var result = (ViewResult)await controller.ByMovie(2);

            var viewModel = (ListShowsByMovieViewModel)result.Model;

            Assert.AreEqual(3, viewModel.Movies.Count());

            var movie1 = viewModel.Movies.ElementAt(0);
            Assert.AreEqual("1", movie1.Value);
            Assert.IsFalse(movie1.Selected);

            var movie2 = viewModel.Movies.ElementAt(1);
            Assert.AreEqual("2", movie2.Value);
            Assert.IsTrue(movie2.Selected);

            var movie3 = viewModel.Movies.ElementAt(2);
            Assert.AreEqual("3", movie3.Value);
            Assert.IsFalse(movie3.Selected);
            
            this.movieService.VerifyAll();

            this.showService.VerifyAll();
        }

        private ShowsController CreateController()
        {
            return new ShowsController(this.showService.Object, this.roomService.Object, this.movieService.Object, this.currentUserRole.Object);
        }
    }
}
