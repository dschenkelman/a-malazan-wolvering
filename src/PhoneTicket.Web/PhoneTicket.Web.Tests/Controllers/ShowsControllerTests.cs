namespace PhoneTicket.Web.Tests.Controllers
{
    using System;
    using System.Collections.Generic;
    using System.Linq;
    using System.Text;
    using System.Threading.Tasks;

    using Microsoft.VisualStudio.TestTools.UnitTesting;

    using Moq;

    using PhoneTicket.Web.Services;
    using PhoneTicket.Web.Models;
    using System.Web.Mvc;
    using PhoneTicket.Web.Controllers;
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
        public async Task ShouldSetShowPropertiesToEditShowViewModelPropertiesWhenCallingEdit()
        {
            const int showId = 1;
            const int movieId = 1;
            const int roomId = 1;
            var date = new DateTime(2013, 10, 10);
            var dateToString = date.ToString("yyyy-MM-dd");
            const double price = 10.0d;
            const bool isAvailable = true;

            var movies = new List<SelectListItem>();
            var rooms = new List<SelectListItem>();

            var show = new Show {   Id = showId, 
                                    MovieId = movieId, 
                                    RoomId = roomId, 
                                    Date = date, 
                                    Price = price, 
                                    IsAvailable = isAvailable };

            this.showService.Setup(ss => ss.GetAsync(showId)).Returns(Task.FromResult(show));
            this.roomService.Setup(rs => rs.SameComplexRoomsListAsync(It.IsAny<int>())).Returns(Task.FromResult<IEnumerable<SelectListItem>>(rooms));
            this.movieService.Setup(rs => rs.ListAsync(It.IsAny<int>())).Returns(Task.FromResult<IEnumerable<SelectListItem>>(movies));

            var controller = this.CreateController();

            var result = (ViewResult) await controller.Edit(showId);

            var showObtained = (EditShowViewModel)result.Model;

            Assert.AreEqual(showId, showObtained.Id);
            Assert.AreEqual(isAvailable, showObtained.IsAvailable);
            Assert.AreEqual(movieId, showObtained.MovieId);
            Assert.AreEqual(roomId, showObtained.RoomId);
            Assert.AreEqual(price, showObtained.Price);
            Assert.AreEqual(dateToString, showObtained.Date);

            Assert.AreSame(rooms, showObtained.AvailableRooms);
            Assert.AreSame(movies, showObtained.AvailableMovies);
        }

        [TestMethod]
        public async Task ShouldUpdateShowRetrievedFromServiceBasedOnUpdatedShowProperties()
        {
            const int showId = 1;
            const int movieId = 1;
            const int roomId = 1;
            var date = new DateTime(2013, 10, 10);
            var dateToString = date.ToString("yyyy-MM-dd");
            const double price = 10.0d;
            const bool isAvailable = true;

            var movies = new List<SelectListItem>();
            var rooms = new List<SelectListItem>();

            var updatedShow = new Show
            {
                Id = showId,
                MovieId = movieId,
                RoomId = roomId,
                Date = date,
                Price = price,
                IsAvailable = isAvailable
            };

            var existingShow = new Show { Id = showId };

            var controller = this.CreateController();

            this.showService.Setup(ss => ss.GetAsync(showId)).Returns(Task.FromResult(existingShow)).Verifiable();
            this.showService.Setup(ss => ss.UpdateAsync(existingShow)).Returns(Task.FromResult<object>(null)).Verifiable();

            await controller.EditShow(EditShowViewModel.FromShow(updatedShow));

            this.showService.Verify(rs => rs.GetAsync(updatedShow.Id), Times.Once());

            this.showService.Verify(rs => rs.UpdateAsync(It.Is<Show>(s => s.Id == showId
                                                                        && s.Date == date
                                                                        && s.IsAvailable == isAvailable
                                                                        && s.RoomId == roomId
                                                                        && s.Price == price
                                                                        && s.MovieId == movieId)),
                                                                    Times.Once());
        }

        private ShowsController CreateController()
        {
            return new ShowsController(this.showService.Object, this.roomService.Object, this.movieService.Object);
        }
    }
}
