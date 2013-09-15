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
    public class UsersControllerTests
    {
        private MockRepository mockRepository;

        private Mock<IUserService> usersService;

        [TestInitialize]
        public void Initialize()
        {
            this.mockRepository = new MockRepository(MockBehavior.Default);
            this.usersService = this.mockRepository.Create<IUserService>();
        }

        [TestMethod]
        public async Task ShouldReturnItemsOnPageOneWhenIndexIsCalledIfNoPageIsProvided()
        {
            var users = new List<User>();
            var items = 70;

            for (int i = 0; i < items; i++)
            {
                users.Add(
                    new User
                        {
                            BirthDate = DateTime.Today.AddYears(-30).AddDays(i),
                            CellPhoneNumber = i.ToString(CultureInfo.InvariantCulture),
                            EmailAddress = string.Format("email@{0}.com", i),
                            FirstName = "First" + i,
                            LastName = "Last" + i,
                            Id = i,
                            PasswordHash = new[] { (byte)i }
                        });
            }

            this.usersService.Setup(us => us.GetUsersAsync()).Returns(Task.FromResult((IEnumerable<User>)users)).Verifiable();

            var controller = this.CreateController();

            var result = (ViewResult)await controller.Index(null);

            var pagedList = (IPagedList<ListUserViewModel>)result.ViewData.Model;

            var pages = Math.Ceiling((double)items / pagedList.PageSize);

            Assert.AreEqual(items, pagedList.TotalItemCount);
            Assert.IsTrue(pagedList.HasNextPage);
            Assert.IsFalse(pagedList.HasPreviousPage);
            Assert.IsTrue(pagedList.IsFirstPage);
            Assert.AreEqual(pages, pagedList.PageCount);
        }

        [TestMethod]
        public async Task ShouldReturnItemsOnPageNumberWhenIndexIsCalledIfWithPageNumberIsProvided()
        {
            var users = new List<User>();
            const int Items = 70;
            const int PageNumber = 3;

            for (int i = 0; i < Items; i++)
            {
                users.Add(
                    new User
                    {
                        BirthDate = DateTime.Today.AddYears(-30).AddDays(i),
                        CellPhoneNumber = i.ToString(CultureInfo.InvariantCulture),
                        EmailAddress = string.Format("email@{0}.com", i),
                        FirstName = "First" + i,
                        LastName = "Last" + i,
                        Id = i,
                        PasswordHash = new[] { (byte)i }
                    });
            }

            this.usersService.Setup(us => us.GetUsersAsync()).Returns(Task.FromResult((IEnumerable<User>)users)).Verifiable();

            var controller = this.CreateController();

            var result = (ViewResult)await controller.Index(PageNumber);

            var pagedList = (IPagedList<ListUserViewModel>)result.ViewData.Model;

            var pages = Math.Ceiling((double)Items / pagedList.PageSize);

            Assert.AreEqual(Items, pagedList.TotalItemCount);
            Assert.IsTrue(pagedList.HasNextPage);
            Assert.IsTrue(pagedList.HasPreviousPage);
            Assert.AreEqual(PageNumber, pagedList.PageNumber);
            Assert.AreEqual(pages, pagedList.PageCount);
        }

        private UsersController CreateController()
        {
            return new UsersController(this.usersService.Object);
        }
    }
}
