namespace PhoneTicket.Web.Tests.Controllers.Api
{
    using System;
    using System.Net;
    using System.Net.Http;
    using System.Net.Mail;
    using System.Security.Cryptography;
    using System.Security.Principal;
    using System.Text;
    using System.Threading;
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

        private Mock<IUserService> userService;

        private Mock<IEmailService> emailService;

        [TestInitialize]
        public void Initialize()
        {
            this.mockRepository = new MockRepository(MockBehavior.Default);
            this.temporaryUserService = this.mockRepository.Create<ITemporaryUserService>();
            this.userService = this.mockRepository.Create<IUserService>();
            this.emailService = this.mockRepository.Create<IEmailService>();
        }

        [TestMethod]
        public async Task ShouldCallTemporaryUserServiceToAddUserInformationWhenCreateIsCalled()
        {
            const string FirstName = "first";
            const string LastName = "last";
            const string Password = "password";
            var birthDate = new DateTime(1989, 3, 2);
            const string CellPhoneNumber = "1536548978";
            const int Id = 1231;
            const string Email = "e@mail.com";

            var userViewModel = new NewUserViewModel()
                                    {
                                        BirthDate = birthDate.ToString("yyyy/MM/dd"),
                                        CellPhoneNumber = CellPhoneNumber,
                                        Dni = Id,
                                        EmailAddress = Email,
                                        FirstName = FirstName,
                                        LastName = LastName,
                                        Password = Password
                                    };

            this.userService.Setup(us => us.HasConflict(It.IsAny<User>())).Returns(Task.FromResult(false));

            this.temporaryUserService.Setup(
                tus =>
                tus.CreateUserAsync(
                    It.Is<User>(
                        u => u.BirthDate == birthDate && u.EmailAddress == Email && u.FirstName == FirstName
                             && u.LastName == LastName && u.Id == Id && u.CellPhoneNumber == CellPhoneNumber
                             && Encoding.UTF8.GetString(u.PasswordHash) == Encoding.UTF8.GetString(new SHA256CryptoServiceProvider().ComputeHash(Encoding.UTF8.GetBytes(Password))))))
                .Returns(Task.FromResult(Guid.NewGuid()))
                .Verifiable();

            this.emailService.Setup(es => es.SendAsync(It.IsAny<MailMessage>())).Returns(Task.FromResult<object>(null));

            var controller = this.CreateController();
            await controller.Create(userViewModel);

            this.temporaryUserService.Verify(
                tus =>
                tus.CreateUserAsync(It.Is<User>(
                        u => u.BirthDate == birthDate && u.EmailAddress == Email && u.FirstName == FirstName
                             && u.LastName == LastName && u.Id == Id && u.CellPhoneNumber == CellPhoneNumber
                             && Encoding.UTF8.GetString(u.PasswordHash) == Encoding.UTF8.GetString(new SHA256CryptoServiceProvider().ComputeHash(Encoding.UTF8.GetBytes(Password))))),
                Times.Once());
        }

        [TestMethod]
        public async Task ShouldReturn201CreateWhenCreateIsCalledIfNoErrorsOccur()
        {
            const string FirstName = "first";
            const string LastName = "last";
            const string Password = "password";
            var birthDate = new DateTime(1989, 3, 2);
            const string CellPhoneNumber = "1536548978";
            const int Id = 1231;
            const string Email = "e@mail.com";

            var userViewModel = new NewUserViewModel()
            {
                BirthDate = birthDate.ToString("yyyy/MM/dd"),
                CellPhoneNumber = CellPhoneNumber,
                Dni = Id,
                EmailAddress = Email,
                FirstName = FirstName,
                LastName = LastName,
                Password = Password
            };

            this.userService.Setup(us => us.HasConflict(It.IsAny<User>())).Returns(Task.FromResult(false));

            this.temporaryUserService.Setup(tus => tus.CreateUserAsync(It.IsAny<User>())).Returns(Task.FromResult(Guid.NewGuid())).Verifiable();

            this.emailService.Setup(es => es.SendAsync(It.IsAny<MailMessage>())).Returns(Task.FromResult<object>(null));

            var controller = this.CreateController();

            var response = await controller.Create(userViewModel);
            
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

            this.userService.Setup(us => us.GetIdAsync(Email)).Returns(Task.FromResult(Id)).Verifiable();

            var oldPrincipal = Thread.CurrentPrincipal;

            var identity = new GenericIdentity(Email);
            Thread.CurrentPrincipal = new GenericPrincipal(identity, null);

            Assert.AreEqual(Id, await controller.Auth());

            Thread.CurrentPrincipal = oldPrincipal;

            this.userService.Verify(us => us.GetIdAsync(Email), Times.Once);
        }

        [TestMethod]
        public async Task ShouldSendEmailWithConfirmationLinkIncludingSecretAndUserIdWhenRegistering()
        {
            var secret = Guid.NewGuid();
            const int UserId = 12345678;
            const string UserEmail = "e@mail.com";
            var expectedLink = string.Format("https://phoneticket.apphb.com/api/users/{0}/confirm?secret={1}", UserId, secret);
            var mailMessage = new MailMessage();

            var controller = this.CreateController();

            this.userService.Setup(us => us.HasConflict(It.IsAny<User>())).Returns(Task.FromResult(false));

            this.temporaryUserService.Setup(tus => tus.CreateUserAsync(It.IsAny<User>()))
                .Returns(Task.FromResult(secret))
                .Verifiable();

            this.emailService.Setup(
                es => es.CreateMessage(It.IsAny<string>(), It.Is<string>(b => b.Contains(expectedLink)), UserEmail))
                .Returns(mailMessage)
                .Verifiable();

            this.emailService.Setup(es => es.SendAsync(mailMessage)).Returns(Task.FromResult<object>(null)).Verifiable();

            await
                controller.Create(
                    new NewUserViewModel
                        {
                            Dni = UserId,
                            EmailAddress = UserEmail,
                            Password = "password",
                            BirthDate = "2013/05/04",
                            FirstName = "First",
                            LastName = "Last"
                        });

            this.emailService.Verify(
                es => es.CreateMessage(It.IsAny<string>(), It.Is<string>(b => b.Contains(expectedLink)), UserEmail),
                Times.Once());

            this.emailService.Verify(es => es.SendAsync(mailMessage), Times.Once());
        }

        [TestMethod]
        public async Task ShouldUpdateIsValidPropertyWhenEditIsInvoked()
        {
            const int Id = 351231;
            var viewModel = new EditUserViewModel { IsValid = false };
            var user = new User { IsValid = true };

            this.userService.Setup(us => us.GetUserAsync(Id)).Returns(Task.FromResult(user)).Verifiable();

            this.userService.Setup(us => us.UpdateAsync(user)).Returns(Task.FromResult<object>(null)).Verifiable();

            var controller = this.CreateController();

            await controller.Edit(Id, viewModel);

            this.userService.Verify(us => us.GetUserAsync(Id), Times.Once());
            this.userService.Verify(us => us.UpdateAsync(user), Times.Once());

            Assert.IsFalse(user.IsValid);
        }

        [TestMethod]
        public async Task ShouldReturn409IfUserServiceReturnsThatThereIsAConflictWithTheUserInfo()
        {
            const string FirstName = "first";
            const string LastName = "last";
            const string Password = "password";
            var birthDate = new DateTime(1989, 3, 2);
            const string CellPhoneNumber = "1536548978";
            const int Id = 1231;
            const string Email = "e@mail.com";

            var userViewModel = new NewUserViewModel()
            {
                BirthDate = birthDate.ToString("yyyy/MM/dd"),
                CellPhoneNumber = CellPhoneNumber,
                Dni = Id,
                EmailAddress = Email,
                FirstName = FirstName,
                LastName = LastName,
                Password = Password
            };

            this.userService.Setup(us => us.HasConflict(It.IsAny<User>())).Returns(Task.FromResult(true)).Verifiable();

            var controller = this.CreateController();
            var response = await controller.Create(userViewModel);

            Assert.AreEqual(HttpStatusCode.Conflict, response.StatusCode);

            this.userService.Verify(us => us.HasConflict(It.IsAny<User>()), Times.Once());
        }

        private UsersController CreateController()
        {
            return new UsersController(this.userService.Object, this.temporaryUserService.Object, this.emailService.Object);
        }
    }
}