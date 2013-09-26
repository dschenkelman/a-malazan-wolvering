namespace PhoneTicket.Web.Tests.Controllers
{
    using System;
    using System.Collections.Generic;
    using System.Globalization;
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

        [TestInitialize]
        public void Initialize()
        {
            this.mockRepository = new MockRepository(MockBehavior.Default);
            this.moviesService = this.mockRepository.Create<IMovieService>();
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

            var result = (ViewResult)await controller.Index(null);

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

            var result = (ViewResult)await controller.Index(PageNumber);

            var pagedList = (IPagedList<ListMovieViewModel>)result.ViewData.Model;

            var pages = Math.Ceiling((double)Items / pagedList.PageSize);

            Assert.AreEqual(Items, pagedList.TotalItemCount);
            Assert.IsTrue(pagedList.HasNextPage);
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

        private MoviesController CreateController()
        {
            return new MoviesController(this.moviesService.Object, null, null);
        }
    }
}
