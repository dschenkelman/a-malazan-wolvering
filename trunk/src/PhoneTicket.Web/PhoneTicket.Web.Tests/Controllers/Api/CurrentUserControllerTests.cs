namespace PhoneTicket.Web.Tests.Controllers.Api
{
    using System;
    using System.Security.Principal;
    using System.Threading;
    using System.Threading.Tasks;
    using System.Linq;
    using System.Linq.Expressions;
    using System.Collections.Generic;

    using Microsoft.VisualStudio.TestTools.UnitTesting;

    using Moq;

    using PhoneTicket.Web.Controllers.Api;
    using PhoneTicket.Web.Models;
    using PhoneTicket.Web.Services;
    using System.Collections.ObjectModel;
    
    

    [TestClass]
    public class CurrentUserControllerTests
    {
        private MockRepository mockRepository;

        private Mock<IUserService> userService;

        private Mock<IOperationService> operationService;

        [TestInitialize]
        public void Initialize()
        {
            this.mockRepository = new MockRepository(MockBehavior.Default);

            this.userService = this.mockRepository.Create<IUserService>();

            this.operationService = this.mockRepository.Create<IOperationService>();
        }

        [TestMethod]
        public async Task ShouldReturnUserInformationBasedOnUserFromCurrentPrincipalIdentityWhenInfoIsCalled()
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

        [TestMethod]
        public async Task ShouldReturnUserPurchaseAndReservationOperationsBasedOnUserFromCurrentPrincipalIdentityWhenOperationsIsCalled()
        {
            const string Email = "e@mail";
            
            var number = Guid.NewGuid();
            const int UserId = 0;
            const OperationType Type0 = OperationType.Reservation;
            const OperationType Type1 = OperationType.PurchaseWithoutReservation;

            const int ShowId = 1;
            const string MovieTitle = "asd";
            var showDate = new DateTime(2013, 10, 29);
            const string ComplexAddress = "address";

            var show = new Show { Id = 1, Movie = new Movie { Title = MovieTitle }, Date = showDate, Room = new Room { Complex = new Complex { Address = ComplexAddress } } };

            var operation1 = new Operation
            {
                Number = number,
                UserId = UserId,
                ShowId = ShowId,
                Show = show,
                Type = Type0,
            };

            var operation2 = new Operation
            {
                Number = number,
                UserId = UserId,
                ShowId = ShowId,
                Show = show,
                Type = Type1,
            };

            this.userService.Setup(us => us.GetIdAsync(Email)).Returns(Task.FromResult(UserId)).Verifiable();

            this.operationService.Setup(os => os.GetAsync(It.IsAny<Expression<Func<Operation, bool>>>()))
                .Returns(Task.FromResult((IEnumerable<Operation>)new List<Operation> { operation1, operation2 }))
                .Verifiable();


            var controller = this.CreateController();

            var oldPrincipal = Thread.CurrentPrincipal;

            var identity = new GenericIdentity(Email);
            Thread.CurrentPrincipal = new GenericPrincipal(identity, null);

            var response = await controller.Operations();

            for (int i = 0; i < response.Count(); i++)
            {
                Assert.AreEqual(number, response.ElementAt(i).Id);
                Assert.AreEqual(MovieTitle, response.ElementAt(i).MovieTitle);
                Assert.AreEqual(showDate.ToString("dd/MM HH:mm") + "Hs", response.ElementAt(i).ShowDateAndTime);
                Assert.AreEqual(ComplexAddress, response.ElementAt(i).ComplexAddress);
            }
            
            Assert.AreEqual((!Type0.Equals(OperationType.Reservation)), response.ElementAt(0).IsBought);
            Assert.AreEqual((!Type1.Equals(OperationType.Reservation)), response.ElementAt(1).IsBought);

            Thread.CurrentPrincipal = oldPrincipal;

            this.userService.Verify(us => us.GetIdAsync(Email), Times.Once);
            this.operationService.Verify(us => us.GetAsync(It.IsAny<Expression<Func<Operation, bool>>>()), Times.Once);

        }

        [TestMethod]
        public async Task ShouldReturnCorrectOperationDetailViewModelWhenOperationsIsCalledWithOperationId()
        {
            var operationNumber = Guid.NewGuid();
            const int Row = 1;
            const int Col = 1;
            const int DiscountId = 1;
            const int DiscountCount = 1;

            const int ShowId = 1;
            const string MovieTitle = "asd";
            var showDate = new DateTime(2013, 10, 29, 12, 30, 00);
            const string ComplexAddress = "address";
            const double ShowPrice = 10;

            var show = new Show
            {
                Id = ShowId,
                Movie = new Movie { Title = MovieTitle },
                Date = showDate,
                Room = new Room { Complex = new Complex { Address = ComplexAddress } },
                Price = ShowPrice
            };

            var seat = new OccupiedSeat { OperationId = operationNumber, Column = Col, Row = Row};
            var discount = new OperationDiscount { DiscountId = DiscountId, Discount = new Discount { Id = DiscountId }, Count = DiscountCount };

            var operation = new Operation
            {
                Number = operationNumber,
                Show = show,
                OccupiedSeats = new Collection<OccupiedSeat> { seat },
                OperationDiscounts = new Collection<OperationDiscount> { discount }
            };


            this.operationService.Setup(os => os.GetAsync(operationNumber)).Returns(Task.FromResult(operation)).Verifiable();

            var controller = this.CreateController();

            var response = await controller.Operations(operationNumber);

            Assert.AreEqual(MovieTitle,response.MovieTitle);
            Assert.AreEqual(showDate.ToString("dd/MM HH:mm") + "Hs", response.ShowDateAndTime);
            Assert.AreEqual(ComplexAddress, response.ComplexAddress);
            Assert.AreEqual(ShowPrice, response.ShowPrice);

            Assert.AreEqual(1, response.Seats.Count());
            Assert.AreEqual(seat.Row, response.Seats.ElementAt(0).Row);
            Assert.AreEqual(seat.Column, response.Seats.ElementAt(0).Column);

            Assert.AreEqual(1, response.Discounts.Count());
            Assert.AreEqual(discount.DiscountId, response.Discounts.ElementAt(0).DiscountId);
            Assert.AreEqual(discount.Count, response.Discounts.ElementAt(0).Count);

            this.operationService.Verify(os => os.GetAsync(operationNumber), Times.Once);
        }

        private CurrentUserController CreateController()
        {
            return new CurrentUserController(this.userService.Object, this.operationService.Object);
        }
    }
}
