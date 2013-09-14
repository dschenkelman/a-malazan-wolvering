namespace PhoneTicket.Web.Tests.Controllers
{
    using System;
    using System.Net;
    using System.Threading.Tasks;
    using System.Web.Http;

    using Microsoft.VisualStudio.TestTools.UnitTesting;

    using Moq;

    using PhoneTicket.Web.Controllers.Api;
    using PhoneTicket.Web.Models;
    using PhoneTicket.Web.Services;
    using PhoneTicket.Web.ViewModels;

    [TestClass]
    public class UsersControllerTests
    {
        private MockRepository mockRepository;

        private Mock<ITemporaryUserService> temporaryUserService;

        [TestInitialize]
        public void Initialize()
        {
            this.mockRepository = new MockRepository(MockBehavior.Default);
            this.temporaryUserService = this.mockRepository.Create<ITemporaryUserService>();
        }

        [TestMethod]
        public async Task ShouldCallTemporaryUserServiceToAddUserInformationWhenPostIsCalled()
        {
            var user = new NewUserViewModel();

            this.temporaryUserService.Setup(tus => tus.CreateUser(user)).Returns(Task.FromResult(Guid.NewGuid())).Verifiable();

            var controller = this.CreateController();
            await controller.Post(user);

            this.temporaryUserService.Verify(tus => tus.CreateUser(user), Times.Once());
        }

        [TestMethod]
        public async Task ShouldReturn201CreateWhenPostIsCalledIfNoErrorsOccur()
        {
            var user = new NewUserViewModel();

            this.temporaryUserService.Setup(tus => tus.CreateUser(user)).Returns(Task.FromResult(Guid.NewGuid())).Verifiable();

            var controller = this.CreateController();
            
            var response = await controller.Post(user);
            
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
        
        private UsersController CreateController()
        {
            return new UsersController(this.temporaryUserService.Object);
        }
    }
}
