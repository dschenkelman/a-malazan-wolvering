namespace PhoneTicket.Web.Tests.Controllers.Api
{
    using System;
    using System.Collections.Generic;
    using System.Linq;
    using System.Threading.Tasks;

    using Microsoft.VisualStudio.TestTools.UnitTesting;

    using Moq;

    using PhoneTicket.Web.Controllers.Api;
    using PhoneTicket.Web.Helpers;
    using PhoneTicket.Web.Models;
    using PhoneTicket.Web.Services;

    [TestClass]
    public class MoviesControllerTests
    {
        private MockRepository mockRepository;

        private Mock<IMovieService> movieService;

        private Mock<IShowService> showService;

        [TestInitialize]
        public void Initialize()
        {
            this.mockRepository = new MockRepository(MockBehavior.Default);
            this.movieService = this.mockRepository.Create<IMovieService>();
            this.showService = this.mockRepository.Create<IShowService>();
        }


        [TestMethod]
        public async Task ShouldCallMovieServiceAndReturnAllAvailableMoviesWhenGetIsCalled()
        {
            var movie1 = this.CreateMovie();

            this.movieService.Setup(ms => ms.GetMoviesAsync()).Returns(Task.FromResult((IEnumerable<Movie>)new List<Movie> { movie1 })).Verifiable();

            var controller = this.CreateController();

            var response = await controller.Get();

            this.movieService.Verify(ms => ms.GetMoviesAsync(), Times.Once());

            Assert.AreEqual(response.ToList()[0].Id, movie1.Id);
        }

        [TestMethod]
        public async Task ShouldCallMovieServiceAndReturnTheRightMovieWhenGetIsCalledWithAnId()
        {
            var movie1 = this.CreateMovie();

            this.movieService.Setup(ms => ms.GetAsync(It.IsAny<int>())).Returns(Task.FromResult(movie1)).Verifiable();

            var controller = this.CreateController();

            var response = await controller.Get(movie1.Id);

            this.movieService.Verify(ms => ms.GetAsync(It.IsAny<int>()), Times.Once());

            Assert.AreEqual(response.Id, movie1.Id);
        }

        [TestMethod]
        public async Task ShouldNotReturnInfoAboutShowsThatAreNotWithinWeekWhenWeekShowsForMovieIsCalled()
        {
            const int MovieId = 2;

            var show = new Show() { Date = DateTime.Today.AddDays(7) };

            this.showService.Setup(ss => ss.GetForMovieAsync(MovieId))
                .Returns(Task.FromResult((IEnumerable<Show>)new List<Show> { show }));

            var controller = this.CreateController();

            var showsByComplex = await controller.WeekShowsForMovie(MovieId);

            Assert.AreEqual(0, showsByComplex.Count());
        }

        [TestMethod]
        public async Task ShouldReturnCorrectInfoForShowWithinWeekWhenWeekShowsForMovieIsCalled()
        {
            const int MovieId = 2;

            var showTime = DateTime.Today.AddDays(1).AddHours(22).AddMinutes(30);
            const string ComplexName = "Complex3";
            const int ComplexId = 3;
            const int ShowId = 8;

            var show = new Show { Id = ShowId, Date = showTime, Room = new Room {Complex = new Complex { Id = ComplexId, Name = ComplexName } } };

            this.showService.Setup(ss => ss.GetForMovieAsync(MovieId))
                .Returns(Task.FromResult((IEnumerable<Show>)new List<Show> { show }));

            var controller = this.CreateController();

            var showsByComplex = await controller.WeekShowsForMovie(MovieId);

            Assert.AreEqual(1, showsByComplex.Count());

            var complex3Shows = showsByComplex.First();

            Assert.AreEqual(ComplexId, complex3Shows.CinemaId);
            Assert.AreEqual(ComplexName, complex3Shows.CinemaName);

            Assert.AreEqual(1, complex3Shows.Functions.Count);

            var showInfo = complex3Shows.Functions.First();

            Assert.AreEqual(showTime.ToString("HH:mm"), showInfo.Time);
            Assert.AreEqual(showTime.DayOfWeek.InSpanish(), showInfo.Day);
            Assert.AreEqual(ShowId, showInfo.Id);
        }

        [TestMethod]
        public async Task ShouldSortShowsWithinSampleComplexWhenWeekShowsForMovieIsCalled()
        {
            const int MovieId = 2;

            var show1Time = DateTime.Today.AddDays(1).AddHours(22).AddMinutes(30);
            var show2Time = DateTime.Today.AddHours(10).AddMinutes(15);
            const string ComplexName = "Complex3";
            const int ComplexId = 3;
            const int Show1Id = 8;
            const int Show2Id = 31;

            var show1 = new Show { Id = Show1Id, Date = show1Time, Room = new Room { Complex = new Complex { Id = ComplexId, Name = ComplexName } } };
            var show2 = new Show { Id = Show2Id, Date = show2Time, Room = new Room { Complex = new Complex { Id = ComplexId, Name = ComplexName } } };

            this.showService.Setup(ss => ss.GetForMovieAsync(MovieId))
                .Returns(Task.FromResult((IEnumerable<Show>)new List<Show> { show1, show2 }));

            var controller = this.CreateController();

            var showsByComplex = await controller.WeekShowsForMovie(MovieId);

            Assert.AreEqual(1, showsByComplex.Count());

            var complex3Shows = showsByComplex.First();

            Assert.AreEqual(ComplexId, complex3Shows.CinemaId);
            Assert.AreEqual(ComplexName, complex3Shows.CinemaName);

            Assert.AreEqual(2, complex3Shows.Functions.Count);

            var show2Info = complex3Shows.Functions.First();
            var show1Info = complex3Shows.Functions.Last();

            Assert.AreEqual(Show1Id, show1Info.Id);
            Assert.AreEqual(Show2Id, show2Info.Id);
        }


        [TestMethod]
        public async Task ShouldGroupShowsByComplexWhenWeekShowsForMovieIsCalled()
        {
            const int MovieId = 2;

            var show1Time = DateTime.Today.AddDays(1).AddHours(22).AddMinutes(30);
            var show2Time = DateTime.Today.AddHours(10).AddMinutes(15);
            const string Complex1Name = "Complex3";
            const string Complex2Name = "Complex4";
            const int Complex1Id = 3;
            const int Complex2Id = 4;
            const int Show1Id = 8;
            const int Show2Id = 31;

            var show1 = new Show { Id = Show1Id, Date = show1Time, Room = new Room { Complex = new Complex { Id = Complex1Id, Name = Complex1Name } } };
            var show2 = new Show { Id = Show2Id, Date = show2Time, Room = new Room { Complex = new Complex { Id = Complex2Id, Name = Complex2Name } } };

            this.showService.Setup(ss => ss.GetForMovieAsync(MovieId))
                .Returns(Task.FromResult((IEnumerable<Show>)new List<Show> { show1, show2 }));

            var controller = this.CreateController();

            var showsByComplex = await controller.WeekShowsForMovie(MovieId);

            Assert.AreEqual(2, showsByComplex.Count());

            var complex3Shows = showsByComplex.First();
            var complex4Shows = showsByComplex.Last();

            Assert.AreEqual(Complex1Id, complex3Shows.CinemaId);
            Assert.AreEqual(Complex1Name, complex3Shows.CinemaName);

            Assert.AreEqual(Complex2Id, complex4Shows.CinemaId);
            Assert.AreEqual(Complex2Name, complex4Shows.CinemaName);

            Assert.AreEqual(1, complex3Shows.Functions.Count);
            Assert.AreEqual(1, complex4Shows.Functions.Count);

            var show1Info = complex3Shows.Functions.First();
            var show2Info = complex4Shows.Functions.First();

            Assert.AreEqual(Show1Id, show1Info.Id);
            Assert.AreEqual(Show2Id, show2Info.Id);
        }

        private Movie CreateMovie()
        {
            var genre = new Genre { Id = 1, Name = "Genre1" };
            var rating = new Rating { Id = 1, Description = "Rating1" };

            return new Movie()
            {
                Id = 1,
                Title = "Movie",
                GenreId = genre.Id,
                Genre = genre,
                RatingId = rating.Id,
                Rating = rating,
                DurationInMinutes = 123,
                ImageUrl = "imageURL",
                Synopsis = "Movie Synopsis",
                TrailerUrl = "trailerURL"
            };
        }

        private MoviesController CreateController()
        {
            return new MoviesController(this.movieService.Object, this.showService.Object);
        }

    }
}

