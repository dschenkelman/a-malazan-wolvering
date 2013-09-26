namespace PhoneTicket.Web.Tests.Controllers.Api
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
    using PhoneTicket.Web.Controllers.Api;


    [TestClass]
    public class MoviesControllerTests
    {
        private MockRepository mockRepository;

        private Mock<IMovieService> movieService;

        [TestInitialize]
        public void Initialize()
        {
            this.mockRepository = new MockRepository(MockBehavior.Default);
            this.movieService = this.mockRepository.Create<IMovieService>();
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

            this.movieService.Setup(ms => ms.GetMovie(It.IsAny<int>())).Returns(Task.FromResult(movie1)).Verifiable();

            var controller = this.CreateController();

            var response = await controller.Get(movie1.Id);

            this.movieService.Verify(ms => ms.GetMovie(It.IsAny<int>()), Times.Once());

            Assert.AreEqual(response.Id, movie1.Id);
        }

        private Movie CreateMovie()
        {
            var genre = new Genre() { Id = 1, Name = "Genre1" };
            var rating = new Rating() { Id = 1, Description = "Rating1" };

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
            return new MoviesController(this.movieService.Object);
        }

    }
}
