namespace PhoneTicket.Web.Tests.Controllers.Api
{
    using Microsoft.VisualStudio.TestTools.UnitTesting;
    using Moq;
    using PhoneTicket.Web.Controllers.Api;
    using PhoneTicket.Web.Models;
    using PhoneTicket.Web.Services;
    using PhoneTicket.Web.ViewModels.Api;
    using System;
    using System.Collections.Generic;
    using System.Collections.ObjectModel;
    using System.Linq;
    using System.Linq.Expressions;
    using System.Net;
    using System.Security.Principal;
    using System.Text;
    using System.Threading;
    using System.Threading.Tasks;

    [TestClass]
    public class OperationsControllerTests
    {
        private MockRepository mockRepository;

        private Mock<IUserService> userService;

        private Mock<IOperationService> operationService;

        private Mock<IOccupiedSeatsService> occupiedSeatsService;

        [TestInitialize]
        public void Initialize()
        {
            this.mockRepository = new MockRepository(MockBehavior.Default);

            this.userService = this.mockRepository.Create<IUserService>();

            this.operationService = this.mockRepository.Create<IOperationService>();

            this.occupiedSeatsService = this.mockRepository.Create<IOccupiedSeatsService>();
        }

        [TestMethod]
        public async Task ShouldReturnHttpConflictMessageWhenNewReservationIsCalled()
        {
            const int OldOperationId = 1;
            const int ShowId = 1;
            const int Row = 1;
            const int Col = 1;
            var ArmChairs = new ArmChairViewModel { Column = Col, Row = Row };
            var OperationVM = new NewOperationViewModel { ShowId = ShowId, ArmChairs = new List<ArmChairViewModel>{ArmChairs} };

            var occupiedSeat = new OccupiedSeat{ OperationId = OldOperationId, Row = Row, Column = Col};
            var OldOperation = new Operation { OccupiedSeats = new Collection<OccupiedSeat> { occupiedSeat } };

            this.operationService.Setup(os => os.GetAsync(It.IsAny<Expression<Func<Operation, bool>>>()))
                .Returns(Task.FromResult((IEnumerable<Operation>)new List<Operation> { OldOperation }))
                .Verifiable();

            var controller = this.CreateController();

            var response = await controller.NewReservation(OperationVM);

            Assert.AreEqual(HttpStatusCode.Conflict, response.StatusCode);

            this.operationService.Verify(os => os.GetAsync(It.IsAny<Expression<Func<Operation, bool>>>()), Times.Once());
        }

        [TestMethod]
        public async Task ShouldReturnHttpCreatedMessageWhenNewReservationIsCalled()
        {
            const string Email = "e@mail";
            const int UserId = 1;
            const int OldOperationId = 1;
            const int NewOperationId = 2;
            const int ShowId = 1;
            const int Row1 = 1;
            const int Col1 = 1;
            const int Row2 = 2;
            const int Col2 = 2;
            var ArmChairs = new ArmChairViewModel { Column = Col1, Row = Row1 };
            var OperationVM = new NewOperationViewModel { ShowId = ShowId, ArmChairs = new List<ArmChairViewModel> { ArmChairs } };

            var occupiedSeat = new OccupiedSeat { OperationId = OldOperationId, Row = Row2, Column = Col2 };
            var OldOperation = new Operation { OccupiedSeats = new Collection<OccupiedSeat> { occupiedSeat } };

            this.operationService.Setup(os => os.GetAsync(It.IsAny<Expression<Func<Operation, bool>>>()))
                .Returns(Task.FromResult((IEnumerable<Operation>)new List<Operation> { OldOperation }))
                .Verifiable();

            this.userService.Setup(us => us.GetIdAsync(Email)).Returns(Task.FromResult(UserId)).Verifiable();

            this.operationService.Setup(os => os.CreateAsync(It.IsAny<Operation>())).Returns(Task.FromResult(NewOperationId)).Verifiable();

            this.occupiedSeatsService.Setup(ocs => ocs.CreateAsync(It.IsAny<OccupiedSeat>())).Returns(Task.FromResult<object>(null)).Verifiable();
            
            var oldPrincipal = Thread.CurrentPrincipal;
            var identity = new GenericIdentity(Email);
            Thread.CurrentPrincipal = new GenericPrincipal(identity, null);

            var controller = this.CreateController();

            var response = await controller.NewReservation(OperationVM);

            Thread.CurrentPrincipal = oldPrincipal;

            Assert.AreEqual(HttpStatusCode.Created, response.StatusCode);

            this.operationService.Verify(os => os.GetAsync(It.IsAny<Expression<Func<Operation, bool>>>()), Times.Once());

            this.operationService.Verify(os => os.CreateAsync(It.IsAny<Operation>()), Times.Once());

            this.userService.Verify(us => us.GetIdAsync(Email), Times.Once());

            this.occupiedSeatsService.Verify(ocs => ocs.CreateAsync(It.IsAny<OccupiedSeat>()), Times.Once());
        }

        private OperationsController CreateController()
        {
            return new OperationsController(this.operationService.Object,this.occupiedSeatsService.Object,this.userService.Object);
        }
    }
}
