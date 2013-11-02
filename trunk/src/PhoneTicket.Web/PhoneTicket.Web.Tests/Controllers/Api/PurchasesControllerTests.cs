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

    using Microsoft.VisualStudio.TestTools.UnitTesting;

    using Moq;

    using PhoneTicket.Web.Controllers.Api;
    using PhoneTicket.Web.Helpers;
    using PhoneTicket.Web.Models;
    using PhoneTicket.Web.Services;
    using PhoneTicket.Web.ViewModels.Api;

    [TestClass]
    public class PurchasesControllerTests
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

            bool exceptionThrown = false;

            try
            {
                var response = await controller.NewPurchase(operationVm);
            }
            catch (HttpResponseException e)
            {
                Assert.AreEqual(HttpStatusCode.Conflict, e.Response.StatusCode);
                exceptionThrown = true;
            }

            Assert.IsTrue(exceptionThrown);

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
            var operationVm = new NewOperationViewModel
                                  {
                                      ShowId = ShowId,
                                      ArmChairs = new List<ArmChairViewModel> { armChairs },
                                      CreditCardCompanyId = 30,
                                      CreditCardExpiration = "2013-10-20",
                                      CreditCardNumber = "2000-3001-4002-5003",
                                      CreditCardSecurityCode = "987",
                                  };

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

            this.operationService.Setup(os => os.CreateAsync(It.IsAny<Operation>()))
                .Callback<Operation>(o =>
                    {
                        Assert.AreEqual(operationVm.CreditCardCompanyId, o.CreditCardCompanyId);
                        Assert.AreEqual(DateTime.Parse(operationVm.CreditCardExpiration), o.CreditCardExpirationDate);
                        Assert.AreEqual(operationVm.CreditCardNumber, o.CreditCardNumber);
                        Assert.AreEqual(operationVm.CreditCardSecurityCode, o.CreditCardSecurityCode);
                        Assert.AreEqual(OperationType.PurchaseWithReservation, o.Type);
                    })
                .Returns(Task.FromResult(newOperationId))
                .Verifiable();

            this.occupiedSeatsService.Setup(ocs => ocs.CreateAsync(It.IsAny<OccupiedSeat>()))
                .Returns(Task.FromResult<object>(null))
                .Verifiable();

            this.showService.Setup(ss => ss.GetAsync(ShowId)).Returns(Task.FromResult(show)).Verifiable();

            var emailMessage = new MailMessage();

            this.emailService.Setup(
                es => es.CreateMessage("[CinemAR] Confirmación de operación", It.IsAny<string>(), Email))
                .Returns(emailMessage)
                .Verifiable();

            this.emailService.Setup(es => es.SendAsync(emailMessage))
                .Callback<MailMessage>(mm => Assert.AreEqual("CodigoQR", mm.Attachments[0].Name))
                .Returns(Task.FromResult<object>(null))
                .Verifiable();

            var oldPrincipal = Thread.CurrentPrincipal;
            var identity = new GenericIdentity(Email);
            Thread.CurrentPrincipal = new GenericPrincipal(identity, null);

            var controller = this.CreateController();

            var response = await controller.NewPurchase(operationVm);

            Thread.CurrentPrincipal = oldPrincipal;

            Assert.AreEqual(newOperationId, response);

            this.mockRepository.VerifyAll();
        }

        private PurchasesController CreateController()
        {
            return new PurchasesController(this.operationService.Object,
                this.occupiedSeatsService.Object,
                this.userService.Object,
                this.emailService.Object,
                this.showService.Object);
        }
    }
}
