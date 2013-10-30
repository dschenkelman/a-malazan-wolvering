namespace PhoneTicket.Web.Tests.Controllers.Api
{
    using System;
    using System.Collections.Generic;
    using System.Collections.ObjectModel;
    using System.Linq.Expressions;
    using System.Net;
    using System.Net.Mail;
    using System.Security.Principal;
    using System.Threading;
    using System.Threading.Tasks;
    using System.Web.Http;
    using System.Web.Http.Results;

    using Microsoft.VisualStudio.TestTools.UnitTesting;

    using Moq;

    using PhoneTicket.Web.Controllers.Api;
    using PhoneTicket.Web.Helpers;
    using PhoneTicket.Web.Models;
    using PhoneTicket.Web.Services;
    using PhoneTicket.Web.ViewModels.Api;

    [TestClass]
    public class ReservationsControllerTests
    {
        private MockRepository mockRepository;

        private Mock<IUserService> userService;

        private Mock<IOperationService> operationService;

        private Mock<IOccupiedSeatsService> occupiedSeatsService;

        private Mock<IEmailService> emailService;

        private Mock<IShowService> showService;

        [TestInitialize]
        public void Initialize()
        {
            this.mockRepository = new MockRepository(MockBehavior.Default);

            this.userService = this.mockRepository.Create<IUserService>();

            this.operationService = this.mockRepository.Create<IOperationService>();

            this.occupiedSeatsService = this.mockRepository.Create<IOccupiedSeatsService>();

            this.emailService = this.mockRepository.Create<IEmailService>();

            this.showService = this.mockRepository.Create<IShowService>();
        }

        [TestMethod]
        public async Task ShouldReturnHttpConflictMessageWhenNewReservationIsCalled()
        {
            var oldOperationId = Guid.NewGuid();
            const int ShowId = 1;
            const int Row = 1;
            const int Col = 1;
            
            var armChairs = new ArmChairViewModel { Column = Col, Row = Row };
            var operationVm = new NewOperationViewModel { ShowId = ShowId, ArmChairs = new List<ArmChairViewModel> { armChairs } };

            var occupiedSeat = new OccupiedSeat { OperationId = oldOperationId, Row = Row, Column = Col };
            var oldOperation = new Operation { OccupiedSeats = new Collection<OccupiedSeat> { occupiedSeat } };

            this.operationService.Setup(os => os.GetAsync(It.IsAny<Expression<Func<Operation, bool>>>()))
                .Returns(Task.FromResult((IEnumerable<Operation>)new List<Operation> { oldOperation }))
                .Verifiable();

            var controller = this.CreateController();

            var response = await controller.NewReservation(operationVm);

            Assert.AreEqual(HttpStatusCode.Conflict, response.StatusCode);

            this.operationService.Verify(os => os.GetAsync(It.IsAny<Expression<Func<Operation, bool>>>()), Times.Once());
        }

        [TestMethod]
        public async Task ShouldReturnHttpCreatedMessageWhenNewReservationIsCalled()
        {
            const string Email = "e@mail";
            var oldOperationId = Guid.NewGuid();
            var newOperationId = Guid.NewGuid();
            const int ShowId = 1;
            const int Row1 = 1;
            const int Col1 = 1;
            const int Row2 = 2;
            const int Col2 = 2;
            
            var armChairs = new ArmChairViewModel { Column = Col1, Row = Row1 };
            var operationVm = new NewOperationViewModel { ShowId = ShowId, ArmChairs = new List<ArmChairViewModel> { armChairs } };

            var occupiedSeat = new OccupiedSeat { OperationId = oldOperationId, Row = Row2, Column = Col2 };
            var oldOperation = new Operation { OccupiedSeats = new Collection<OccupiedSeat> { occupiedSeat } };

            var showDate = DateTimeHelpers.DateTimeInArgentina;

            var show = new Show
                           {
                               Id = ShowId,
                               Date = showDate,
                               Room = new Room { Complex = new Complex { Name = "ComplexName" } },
                               Movie = new Movie { Title = "MovieTitle" }
                           };

            var user = new User { EmailAddress = Email };

            this.operationService.Setup(os => os.GetAsync(It.IsAny<Expression<Func<Operation, bool>>>()))
                .Returns(Task.FromResult((IEnumerable<Operation>)new List<Operation> { oldOperation }))
                .Verifiable();

            this.userService.Setup(us => us.GetUserAsync(Email)).Returns(Task.FromResult(user)).Verifiable();

            this.operationService.Setup(os => os.CreateAsync(It.IsAny<Operation>())).Returns(Task.FromResult(newOperationId)).Verifiable();

            this.occupiedSeatsService.Setup(ocs => ocs.CreateAsync(It.IsAny<OccupiedSeat>())).Returns(Task.FromResult<object>(null)).Verifiable();

            this.showService.Setup(ss => ss.GetAsync(ShowId)).Returns(Task.FromResult(show)).Verifiable();

            var emailMessage = new MailMessage();

            this.emailService.Setup(
                es => es.CreateMessage("[CinemAR] Confirmación de operación", It.IsAny<string>(), Email)).Returns(emailMessage).Verifiable();
            
            this.emailService.Setup(es => es.SendAsync(emailMessage))
                .Callback<MailMessage>(mm => Assert.AreEqual("CodigoQR", mm.Attachments[0].Name))
                .Returns(Task.FromResult<object>(null))
                .Verifiable();

            var oldPrincipal = Thread.CurrentPrincipal;
            var identity = new GenericIdentity(Email);
            Thread.CurrentPrincipal = new GenericPrincipal(identity, null);

            var controller = this.CreateController();

            var response = await controller.NewReservation(operationVm);

            Thread.CurrentPrincipal = oldPrincipal;

            Assert.AreEqual(HttpStatusCode.Created, response.StatusCode);

            this.mockRepository.VerifyAll();
        }

        [TestMethod]
        public async Task ShouldReturnHttpOkMessageWhenCancelReservationIsCalled()
        {
            const int ShowId = 1;

            var operationNumber = Guid.NewGuid();

            var operation = new Operation { ShowId = ShowId };

            this.operationService.Setup(os => os.GetAsync(operationNumber)).Returns(Task.FromResult < Operation > (operation) );

            this.operationService.Setup(os => os.DeleteAsync(operationNumber)).Returns(Task.FromResult<object>(null)).Verifiable();

            this.showService.Setup(ss => ss.ManageAvailability(It.IsAny<int>())).Returns(Task.FromResult<object>(null)).Verifiable();

            var controller = this.CreateController();

            var response = await controller.CancelReservation(operationNumber);

            Assert.AreEqual(HttpStatusCode.OK, response.StatusCode);

            this.operationService.Verify(os => os.DeleteAsync(operationNumber), Times.Once());
            this.showService.Verify(ss => ss.ManageAvailability(It.IsAny<int>()), Times.Once());
        }

        [TestMethod]
        public async Task ShouldReturnBadRequestIfOperationIsNotReservationWhenConfirmReservationIsCalled()
        {
            var opNumber = Guid.NewGuid();

            this.operationService.Setup(os => os.GetAsync(opNumber))
                .Returns(Task.FromResult(new Operation() { Type = OperationType.PurchaseWithoutReservation })).Verifiable();

            var controller = this.CreateController();

            var result = (BadRequestErrorMessageResult)await controller.ConfirmReservation(opNumber, null);

            Assert.AreEqual("Solo se puede confirmar una reserva", result.Message);

            this.mockRepository.VerifyAll();
        }

        [TestMethod]
        public async Task ShouldSaveOperationWithCreditCardDataWhenReservationIsCalled()
        {
            var opNumber = Guid.NewGuid();

            var operation = new Operation { Type = OperationType.Reservation };

            this.operationService.Setup(os => os.GetAsync(opNumber))
                .Returns(Task.FromResult(operation))
                .Verifiable();

            this.operationService.Setup(os => os.SaveAsync(operation))
                .Returns(Task.FromResult<object>(null))
                .Verifiable();

            var controller = this.CreateController();

            var viewModel = new ConfirmReservationViewModel 
                                {
                                    CreditCardCompanyId = 30,
                                    CreditCardExpiration = "2013-10-30",
                                    CreditCardNumber = "2000-3001-4002-5003",
                                    CreditCardSecurityCode = "987",
                                };

            var result = await controller.ConfirmReservation(opNumber, viewModel);

            Assert.IsInstanceOfType(result, typeof(OkResult));

            this.mockRepository.VerifyAll();

            Assert.AreEqual(viewModel.CreditCardCompanyId, operation.CreditCardCompanyId);
            Assert.AreEqual(viewModel.CreditCardExpirationDate, operation.CreditCardExpirationDate);
            Assert.AreEqual(viewModel.CreditCardNumber, operation.CreditCardNumber);
            Assert.AreEqual(viewModel.CreditCardSecurityCode, operation.CreditCardSecurityCode);
            Assert.AreEqual(OperationType.PurchaseWithReservation, operation.Type);
        }

        private ReservationsController CreateController()
        {
            return new ReservationsController(this.operationService.Object,
                this.occupiedSeatsService.Object,
                this.userService.Object,
                this.emailService.Object,
                this.showService.Object);
        }
    }
}
