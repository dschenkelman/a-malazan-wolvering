namespace PhoneTicket.Web.Tests.Controllers
{
    using System;
    using System.Collections.Generic;
    using System.Diagnostics.CodeAnalysis;
    using System.Threading.Tasks;
    using System.Web.Mvc;

    using Microsoft.VisualStudio.TestTools.UnitTesting;

    using Moq;

    using PhoneTicket.Web.Controllers;
    using PhoneTicket.Web.Models;
    using PhoneTicket.Web.Services;
    using PhoneTicket.Web.ViewModels;

    [TestClass]
    public class ShowsControllerTests
    {
        private MockRepository mockRepository;
        private Mock<IShowService> showService;
        private Mock<IRoomService> roomService;
        private Mock<IMovieService> movieService;

        [TestInitialize]
        public void Initialize()
        {
            this.mockRepository = new MockRepository(MockBehavior.Default);
            this.showService = this.mockRepository.Create<IShowService>();
            this.roomService = this.mockRepository.Create<IRoomService>();
            this.movieService = this.mockRepository.Create<IMovieService>();
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

        private ShowsController CreateController()
        {
            return new ShowsController(this.showService.Object, this.roomService.Object, this.movieService.Object);
        }
    }
}
