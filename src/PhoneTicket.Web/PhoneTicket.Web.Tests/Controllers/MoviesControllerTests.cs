namespace PhoneTicket.Web.Tests.Controllers
{
    using System;
    using System.Collections.Generic;
    using System.Globalization;
    using System.Linq.Expressions;
    using System.Threading.Tasks;
    using System.Web.Mvc;

    using Microsoft.VisualStudio.TestTools.UnitTesting;

    using Moq;

    using PagedList;

    using PhoneTicket.Web.Controllers;
    using PhoneTicket.Web.Models;
    using PhoneTicket.Web.Services;
    using PhoneTicket.Web.ViewModels;

    [TestClass]
    public class MoviesControllerTests
    {
        private MockRepository mockRepository;

        private Mock<IMovieService> moviesService;

        private Mock<IRatingService> ratingService;

        private Mock<IGenreService> genreService;

        [TestInitialize]
        public void Initialize()
        {
            this.mockRepository = new MockRepository(MockBehavior.Default);
            this.moviesService = this.mockRepository.Create<IMovieService>();
            this.genreService = this.mockRepository.Create<IGenreService>();
            this.ratingService = this.mockRepository.Create<IRatingService>();
        }

        [TestMethod]
        public async Task ShouldReturnItemsOnPageOneWhenIndexIsCalledIfNoPageIsProvided()
        {
            const string ImageUrlFormat = "http://site.com/images/{0}";
            const string TrailerUrlFormat = "http://site.com/trailers/{0}";
            var movies = new List<Movie>();
            const int Items = 25;

            for (int i = 0; i < Items; i++)
            {
                movies.Add(
                    new Movie()
                    {
                        Id = i,
                        DurationInMinutes = i,
                        Genre = new Genre { Id = i, Name = string.Format("Genre{0}", i)},
                        Rating = new Rating { Id = i, Description = string.Format("Rating{0}", i) },
                        ImageUrl = string.Format(ImageUrlFormat, i),
                        TrailerUrl = string.Format(TrailerUrlFormat, i),
                    });
            }

            this.moviesService.Setup(us => us.GetMoviesAsync()).Returns(Task.FromResult((IEnumerable<Movie>)movies)).Verifiable();

            var controller = this.CreateController();

            var result = (ViewResult)await controller.Index(string.Empty, null);

            var pagedList = (IPagedList<ListMovieViewModel>)result.ViewData.Model;

            var pages = Math.Ceiling((double)Items / pagedList.PageSize);

            Assert.AreEqual(Items, pagedList.TotalItemCount);
            Assert.IsTrue(pagedList.HasNextPage);
            Assert.IsFalse(pagedList.HasPreviousPage);
            Assert.IsTrue(pagedList.IsFirstPage);
            Assert.AreEqual(pages, pagedList.PageCount);

            for (int i = 0; i < pagedList.Count; i++)
            {
                var item = pagedList[i];
                Assert.AreEqual(i, item.Id);
                Assert.AreEqual(i, item.DurationInMinutes);
                Assert.AreEqual(string.Format(ImageUrlFormat, i), item.ImageUrl);
                Assert.AreEqual(string.Format("Genre{0}", i), item.Genre);
                Assert.AreEqual(string.Format("Rating{0}", i), item.Rating);
            }
        }

        [TestMethod]
        public async Task ShouldReturnItemsOnPageNumberWhenIndexIsCalledIfWithPageNumberIsProvided()
        {
            const string ImageUrlFormat = "http://site.com/images/{0}";
            const string TrailerUrlFormat = "http://site.com/trailers/{0}";
            const int PageNumber = 3;
            var movies = new List<Movie>();
            const int Items = 25;

            for (int i = 0; i < Items; i++)
            {
                movies.Add(
                    new Movie()
                    {
                        Id = i,
                        DurationInMinutes = i,
                        Genre = new Genre { Id = i, Name = string.Format("Genre{0}", i) },
                        Rating = new Rating { Id = i, Description = string.Format("Rating{0}", i) },
                        ImageUrl = string.Format(ImageUrlFormat, i),
                        TrailerUrl = string.Format(TrailerUrlFormat, i),
                    });
            }

            this.moviesService.Setup(us => us.GetMoviesAsync()).Returns(Task.FromResult((IEnumerable<Movie>)movies)).Verifiable();

            var controller = this.CreateController();

            var result = (ViewResult)await controller.Index(string.Empty, PageNumber);

            var pagedList = (IPagedList<ListMovieViewModel>)result.ViewData.Model;

            var pages = Math.Ceiling((double)Items / pagedList.PageSize);

            Assert.AreEqual(Items, pagedList.TotalItemCount);
            Assert.IsFalse(pagedList.HasNextPage);
            Assert.IsTrue(pagedList.HasPreviousPage);
            Assert.IsFalse(pagedList.IsFirstPage);
            Assert.AreEqual(pages, pagedList.PageCount);

            for (int i = 10; i < pagedList.Count; i++)
            {
                var item = pagedList[i];
                Assert.AreEqual(i, item.Id);
                Assert.AreEqual(i, item.DurationInMinutes);
                Assert.AreEqual(string.Format(ImageUrlFormat, i), item.ImageUrl);
                Assert.AreEqual(string.Format("Genre{0}", i), item.Genre);
                Assert.AreEqual(string.Format("Rating{0}", i), item.Rating);
            }
        }

        [TestMethod]
        public async Task ShouldProvideTitleFilterToMovieServiceIfTitleSearchIsProvided()
        {
            var movies = new List<Movie>();
            const int PageNumber = 1;

            var matchesFilter = new Movie() { Title = "Batman, el caballero de la noche" };
            var doesNotMatchFilter = new Movie { Title = "Batman inicia" };

            this.moviesService.Setup(
                ms =>
                ms.GetMoviesAsync(
                    It.Is<Expression<Func<Movie, bool>>>(
                        e =>
                        e.Compile().Invoke(matchesFilter)
                        && !e.Compile().Invoke(doesNotMatchFilter))))
                .Returns(Task.FromResult((IEnumerable<Movie>)movies)).Verifiable();

            var controller = this.CreateController();

            await controller.Index("caba", PageNumber);

            this.moviesService.Verify(
                ms =>
                ms.GetMoviesAsync(
                    It.Is<Expression<Func<Movie, bool>>>(
                        e => e.Compile().Invoke(matchesFilter) && !e.Compile().Invoke(doesNotMatchFilter))),
                Times.Once());
        }

        [TestMethod]
        public async Task ShouldSetModelToMovieWithDefaultValuesWhenCallingAdd()
        {
            const int Undefined = -1;

            this.ratingService.Setup(rs => rs.ListAsync(It.IsAny<int?>())).Returns(Task.FromResult<IEnumerable<SelectListItem>>(null));
            this.genreService.Setup(rs => rs.ListAsync(It.IsAny<int?>())).Returns(Task.FromResult<IEnumerable<SelectListItem>>(null));

            var controller = this.CreateController();

            var result = (ViewResult)await controller.Add(100);

            var movie = (Movie)result.Model;

            Assert.AreEqual(Undefined, movie.Id);
            Assert.AreEqual(string.Empty, movie.Title);
            Assert.AreEqual(Undefined, movie.GenreId);
            Assert.AreEqual(Undefined, movie.RatingId);
            Assert.AreEqual(string.Empty, movie.Synopsis);
            Assert.AreEqual(string.Empty, movie.TrailerUrl);
            Assert.AreEqual(0, movie.DurationInMinutes);
            Assert.AreEqual(string.Empty, movie.ImageUrl);
        }

        [TestMethod]
        public async Task ShouldSetMovieGenreTypeToListReturnedFromGenreServiceWhenCallingAdd()
        {
            var genres = new List<SelectListItem>();

            this.ratingService.Setup(rs => rs.ListAsync(It.IsAny<int?>())).Returns(Task.FromResult<IEnumerable<SelectListItem>>(null));
            this.genreService.Setup(rs => rs.ListAsync(It.IsAny<int?>())).Returns(Task.FromResult<IEnumerable<SelectListItem>>(genres));

            var controller = this.CreateController();

            var result = (ViewResult)await controller.Add(100);

            Assert.AreSame(genres, result.ViewBag.MovieGenreType);
        }

        [TestMethod]
        public async Task ShouldSetMovieRatingTypeToListReturnedFromRatingServiceWhenCallingAdd()
        {
            var ratings = new List<SelectListItem>();

            this.ratingService.Setup(rs => rs.ListAsync(It.IsAny<int?>())).Returns(Task.FromResult<IEnumerable<SelectListItem>>(ratings));
            this.genreService.Setup(rs => rs.ListAsync(It.IsAny<int?>())).Returns(Task.FromResult<IEnumerable<SelectListItem>>(null));

            var controller = this.CreateController();

            var result = (ViewResult)await controller.Add(100);

            Assert.AreSame(ratings, result.ViewBag.MovieRatingType);
        }

        [TestMethod]
        public async Task ShouldSetMovieIdToMinusOneWhenCallingAdd()
        {
            this.ratingService.Setup(rs => rs.ListAsync(It.IsAny<int?>())).Returns(Task.FromResult<IEnumerable<SelectListItem>>(null));
            this.genreService.Setup(rs => rs.ListAsync(It.IsAny<int?>())).Returns(Task.FromResult<IEnumerable<SelectListItem>>(null));

            var controller = this.CreateController();

            var result = (ViewResult)await controller.Add(100);

            Assert.AreEqual(-1, result.ViewBag.MovieId);
        }

        [TestMethod]
        public async Task ShouldSetMovieIdToMovieIdWhenCallingEdit()
        {
            const int MovieId = 100;
            var movie = new Movie { Id = MovieId };

            this.ratingService.Setup(rs => rs.ListAsync(It.IsAny<int?>())).Returns(Task.FromResult<IEnumerable<SelectListItem>>(null));
            this.genreService.Setup(rs => rs.ListAsync(It.IsAny<int?>())).Returns(Task.FromResult<IEnumerable<SelectListItem>>(null));

            this.moviesService.Setup(ms => ms.GetAsync(MovieId)).Returns(Task.FromResult(movie)).Verifiable();

            var controller = this.CreateController();

            var result = (ViewResult)await controller.Edit(100);

            this.moviesService.Verify(ms => ms.GetAsync(MovieId), Times.Once());
            Assert.AreEqual(MovieId, result.ViewBag.MovieId);
        }

        [TestMethod]
        public async Task ShouldSetMovieRatingTypeToListReturnedFromRatingServiceWhenCallingEdit()
        {
            const int MovieId = 100;
            const int RatingId = 4;
            var movie = new Movie { Id = MovieId, RatingId = RatingId };
            var ratings = new List<SelectListItem>();
            this.moviesService.Setup(ms => ms.GetAsync(MovieId)).Returns(Task.FromResult(movie));

            this.ratingService.Setup(rs => rs.ListAsync(RatingId)).Returns(Task.FromResult<IEnumerable<SelectListItem>>(ratings)).Verifiable();
            this.genreService.Setup(rs => rs.ListAsync(It.IsAny<int?>())).Returns(Task.FromResult<IEnumerable<SelectListItem>>(null));

            var controller = this.CreateController();

            var result = (ViewResult)await controller.Edit(MovieId);

            this.ratingService.Verify(rs => rs.ListAsync(RatingId), Times.Once());
            Assert.AreSame(ratings, result.ViewBag.MovieRatingType);
        }

        [TestMethod]
        public async Task ShouldSetMovieGenreTypeToListReturnedFromGenreServiceWhenCallingEdit()
        {
            const int MovieId = 100;
            const int GenreId = 4;
            var movie = new Movie { Id = MovieId, GenreId = GenreId };
            var genres = new List<SelectListItem>();
            this.moviesService.Setup(ms => ms.GetAsync(MovieId)).Returns(Task.FromResult(movie));

            this.ratingService.Setup(rs => rs.ListAsync(It.IsAny<int?>())).Returns(Task.FromResult<IEnumerable<SelectListItem>>(null));
            this.genreService.Setup(rs => rs.ListAsync(GenreId)).Returns(Task.FromResult<IEnumerable<SelectListItem>>(genres)).Verifiable();

            var controller = this.CreateController();

            var result = (ViewResult)await controller.Edit(MovieId);

            this.genreService.Verify(rs => rs.ListAsync(GenreId), Times.Once());
            Assert.AreSame(genres, result.ViewBag.MovieGenreType);
        }

        [TestMethod]
        public async Task ShouldSetModelToMovieRetrievedFromServiceWhenCallingEdit()
        {
            const int MovieId = 100;
            var movie = new Movie();
            this.moviesService.Setup(ms => ms.GetAsync(MovieId)).Returns(Task.FromResult(movie));

            this.ratingService.Setup(rs => rs.ListAsync(It.IsAny<int?>())).Returns(Task.FromResult<IEnumerable<SelectListItem>>(null));
            this.genreService.Setup(rs => rs.ListAsync(It.IsAny<int?>())).Returns(Task.FromResult<IEnumerable<SelectListItem>>(null));

            var controller = this.CreateController();

            var result = (ViewResult)await controller.Edit(MovieId);

            Assert.AreSame(movie, result.Model);
        }

        [TestMethod]
        public async Task ShouldCreateMovieWithRatingAndGenre()
        {
            const int GenreId = 4;
            const int RatingId = 2;
            var movie = new Movie();

            var controller = this.CreateController();

            this.moviesService.Setup(ms => ms.CreateAsync(movie)).Returns(Task.FromResult<object>(null)).Verifiable();

            await controller.CreateMovie(movie, GenreId, RatingId);

            this.moviesService.Verify(ms => ms.CreateAsync(It.Is<Movie>(m => m == movie && m.RatingId == RatingId && m.GenreId == GenreId)), Times.Once());
        }

        [TestMethod]
        public async Task ShouldRedirectToIndexViewWhenCreatingMovie()
        {
            const int GenreId = 4;
            const int RatingId = 2;
            var movie = new Movie();

            var controller = this.CreateController();

            this.moviesService.Setup(ms => ms.CreateAsync(movie)).Returns(Task.FromResult<object>(null));

            var result = (RedirectToRouteResult)await controller.CreateMovie(movie, GenreId, RatingId);

            Assert.AreEqual("Index", result.RouteValues["action"]);
            Assert.AreEqual("Movies", result.RouteValues["controller"]);
        }

        [TestMethod]
        public async Task ShouldUpdateMovieRetrievedFromServiceBasedOnUpdatedMovieProperties()
        {
            const int GenreId = 4;
            const int RatingId = 2;
            var updatedMovie = new Movie
                            {
                                Id = 20,
                                ImageUrl = "updatedImage",
                                Synopsis = "updateSynopsis",
                                Title = "updatedTitle",
                                DurationInMinutes = 200,
                                TrailerUrl = "updatedTrailer"
                            };

            var existingMovie = new Movie { Id = 20 };

            var controller = this.CreateController();

            this.moviesService.Setup(ms => ms.GetAsync(updatedMovie.Id)).Returns(Task.FromResult(existingMovie)).Verifiable();
            this.moviesService.Setup(ms => ms.UpdateAsync(existingMovie)).Returns(Task.FromResult<object>(null)).Verifiable();

            await controller.EditMovie(updatedMovie, GenreId, RatingId);

            this.moviesService.Verify(ms => ms.GetAsync(updatedMovie.Id), Times.Once());

            this.moviesService.Verify(
                ms => ms.UpdateAsync(It.Is<Movie>(m => 
                    m == existingMovie 
                    && m.RatingId == RatingId 
                    && m.GenreId == GenreId
                    && m.DurationInMinutes == updatedMovie.DurationInMinutes
                    && m.ImageUrl == updatedMovie.ImageUrl
                    && m.TrailerUrl == updatedMovie.TrailerUrl
                    && m.Synopsis == updatedMovie.Synopsis
                    && m.Title == updatedMovie.Title)),
                Times.Once());
        }

        [TestMethod]
        public async Task ShouldRedirectToIndexViewWhenEditingMovie()
        {
            const int GenreId = 4;
            const int RatingId = 2;
            var movie = new Movie { Id = 2 };

            var controller = this.CreateController();

            this.moviesService.Setup(ms => ms.GetAsync(movie.Id)).Returns(Task.FromResult(movie));
            this.moviesService.Setup(ms => ms.UpdateAsync(movie)).Returns(Task.FromResult<object>(null));

            var result = (RedirectToRouteResult)await controller.EditMovie(movie, GenreId, RatingId);

            Assert.AreEqual("Index", result.RouteValues["action"]);
            Assert.AreEqual("Movies", result.RouteValues["controller"]);
        }

        [TestMethod]
        public async Task ShouldDeletUsingIdWhenDeleteMovieIsCalled()
        {
            const int MovieId = 1;
          
            this.moviesService.Setup(ms => ms.DeleteAsync(MovieId))
                .Returns(Task.FromResult<object>(null))
                .Verifiable();

            var controller = this.CreateController();

            await controller.DeleteMovie(MovieId);

            this.moviesService.Verify(ms => ms.DeleteAsync(MovieId), Times.Once());
        }

        [TestMethod]
        public async Task ShouldRedirectToIndexViewWhenDeletingMovie()
        {
            const int MovieId = 1;

            this.moviesService.Setup(ms => ms.DeleteAsync(MovieId))
                .Returns(Task.FromResult<object>(null))
                .Verifiable();

            var controller = this.CreateController();

            var result = (RedirectToRouteResult)await controller.DeleteMovie(MovieId);

            Assert.AreEqual("Index", result.RouteValues["action"]);
            Assert.AreEqual("Movies", result.RouteValues["controller"]);
        }

        private MoviesController CreateController()
        {
            return new MoviesController(this.moviesService.Object, this.genreService.Object, this.ratingService.Object);
        }
    }
}
