namespace PhoneTicket.Web.Tests.Controllers
{
    using System;
    using System.Net;
    using System.Security.Principal;
    using System.Threading;
    using System.Threading.Tasks;
    using System.Web.Http;

    using Microsoft.VisualStudio.TestTools.UnitTesting;

    using Moq;

    using PhoneTicket.Web.Controllers;
    using PhoneTicket.Web.Services;
    using PhoneTicket.Web.ViewModels;

    [TestClass]
    public class UsersControllerTests
    {
        private MockRepository mockRepository;

        private Mock<ITemporaryUserService> temporaryUserService;

        private Mock<IUserService> userService;

        [TestInitialize]
        public void Initialize()
        {
            this.mockRepository = new MockRepository(MockBehavior.Default);
            this.temporaryUserService = this.mockRepository.Create<ITemporaryUserService>();
            this.userService = this.mockRepository.Create<IUserService>();
        }

        [TestMethod]
        public async Task ShouldCallTemporaryUserServiceToAddUserInformationWhenCreateIsCalled()
        {
            var user = new NewUserViewModel();

            this.temporaryUserService.Setup(tus => tus.CreateUser(user)).Returns(Task.FromResult(Guid.NewGuid())).Verifiable();

            var controller = this.CreateController();
            await controller.Create(user);

            this.temporaryUserService.Verify(tus => tus.CreateUser(user), Times.Once());
        }

        [TestMethod]
        public async Task ShouldReturn201CreateWhenCreateIsCalledIfNoErrorsOccur()
        {
            var user = new NewUserViewModel();

            this.temporaryUserService.Setup(tus => tus.CreateUser(user)).Returns(Task.FromResult(Guid.NewGuid())).Verifiable();

            var controller = this.CreateController();
            
            var response = await controller.Create(user);
            
            Assert.AreEqual(HttpStatusCode.Created, response.StatusCode);
        }

        [TestMethod]
        public async Task ShouldCallTemporaryUserServicePassingSecretIfTemporaryUserWithSecretExists()
        {
            var secret = Guid.NewGuid();
            const int UserId = 33222349;

            this.temporaryUserService.Setup(tus => tus.IsSecretValid(UserId, secret)).Returns(Task.FromResult(true)).Verifiable();
            this.temporaryUserService.Setup(tus => tus.ConfirmUser(UserId)).Returns(Task.FromResult<object>(null)).Verifiable();

            var controller = this.CreateController();

            var responseMessage = await controller.Confirm(UserId, secret);

            var content = await responseMessage.Content.ReadAsStringAsync();

            this.temporaryUserService.Verify(tus => tus.IsSecretValid(UserId, secret), Times.Once());
            this.temporaryUserService.Verify(tus => tus.ConfirmUser(UserId), Times.Once());

            Assert.IsTrue(content.Contains("Gracias por su confirmacion!"));
        }

        [TestMethod]
        [ExpectedException(typeof(HttpResponseException))]
        public async Task ShouldThrowIfSecretIsNotValid()
        {
            var secret = Guid.NewGuid();
            const int UserId = 33222349;

            this.temporaryUserService.Setup(tus => tus.IsSecretValid(UserId, secret)).Returns(Task.FromResult(false)).Verifiable();

            var controller = this.CreateController();
            await controller.Confirm(UserId, secret);
        }

        [TestMethod]
        public async Task ShouldReturnUserIdBasedOnUserFromCurrentPrincipalIdentityWhenAuthIsCalled()
        {
            const int Id = 1;
            const string Email = "e@mail.com";
            var controller = this.CreateController();

            this.userService.Setup(us => us.GetId(Email)).Returns(Task.FromResult(Id)).Verifiable();

            var oldPrincipal = Thread.CurrentPrincipal;

            var identity = new GenericIdentity(Email);
            Thread.CurrentPrincipal = new GenericPrincipal(identity, null);

            Assert.AreEqual(Id, await controller.Auth());

            Thread.CurrentPrincipal = oldPrincipal;

            this.userService.Verify(us => us.GetId(Email), Times.Once);
        }
        
        private UsersController CreateController()
        {
            return new UsersController(this.userService.Object, this.temporaryUserService.Object);
        }
    }
}