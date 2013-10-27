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
    using PhoneTicket.Web.Controllers.Api;
    using PhoneTicket.Web.Models;
    using System.Threading;
    using System.Security.Principal;

    [TestClass]
    public class CurrentUserControllerTests
    {
        private MockRepository mockRepository;

        private Mock<IUserService> userService;

        [TestInitialize]
        public void Initialize()
        {
            this.mockRepository = new MockRepository(MockBehavior.Default);

            this.userService = this.mockRepository.Create<IUserService>();
        }

        [TestMethod]
        public async Task ShouldReturnUserInformationBasedOnUserFromCurrentPrincipalIdentityWhenAuthIsCalled()
        {
            const int Id = 1;
            const string Email = "e@mail.com";
            const string FirstName = "f";
            const string LastName = "l";
            const string CellPhoneNumber = "123456789";
            const bool IsValid = true;

            var User = new User { Id = Id, EmailAddress = Email, FirstName = FirstName, LastName = LastName, CellPhoneNumber = CellPhoneNumber, IsValid = IsValid };

            this.userService.Setup(us => us.GetUserAsync(Email)).Returns(Task.FromResult(User)).Verifiable();

            var controller = this.CreateController();

            var oldPrincipal = Thread.CurrentPrincipal;

            var identity = new GenericIdentity(Email);
            Thread.CurrentPrincipal = new GenericPrincipal(identity, null);

            var response = await controller.Info();

            Assert.AreEqual(Id, response.Id);
            Assert.AreEqual(FirstName, response.FirstName);
            Assert.AreEqual(LastName, response.LastName);
            Assert.AreEqual(Email, response.EmailAddress);
            Assert.AreEqual(CellPhoneNumber, response.CellPhoneNumber);
            Assert.AreEqual(string.Empty, response.Bithday);

            Thread.CurrentPrincipal = oldPrincipal;

            this.userService.Verify(us => us.GetUserAsync(Email), Times.Once);
        }

        private CurrentUserController CreateController()
        {
            return new CurrentUserController(this.userService.Object);
        }
    }
}
