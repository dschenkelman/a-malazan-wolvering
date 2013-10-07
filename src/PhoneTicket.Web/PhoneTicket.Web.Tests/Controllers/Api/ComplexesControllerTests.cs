namespace PhoneTicket.Web.Tests.Controllers.Api
{
    using System;
    using System.Collections.Generic;
    using System.Linq.Expressions;
    using System.Threading.Tasks;

    using Microsoft.VisualStudio.TestTools.UnitTesting;

    using Moq;

    using PhoneTicket.Web.Controllers.Api;
    using PhoneTicket.Web.Models;
    using PhoneTicket.Web.Services;

    [TestClass]
    public class ComplexesControllerTests
    {
        private MockRepository repository;

        private Mock<IComplexService> complexService;

        private Mock<IRoomService> roomService;

        [TestInitialize]
        public void Initialize()
        {
            this.repository = new MockRepository(MockBehavior.Default);
            this.complexService = this.repository.Create<IComplexService>();
            this.roomService = this.repository.Create<IRoomService>();
        }

        [TestMethod]
        public async Task ShouldReturnComplexesFromComplexServiceWhenGetIsCalled()
        {
            var complexes = new List<Complex>();

            this.complexService.Setup(cs => cs.GetAsync()).Returns(Task.FromResult((IEnumerable<Complex>)complexes)).Verifiable();

            var controller = this.CreateController();

            var returnedComplexes = await controller.Get();

            Assert.AreSame(complexes, returnedComplexes);

            this.repository.Verify();
        }

        [TestMethod]
        public async Task ShouldReturnRoomsProvidedByServiceWhenGetRoomsIsCalled()
        {
            var rooms = new List<Room>();

            this.roomService.Setup(cs => cs.GetAsync(It.IsAny<Expression<Func<Room, bool>>>())).Returns(Task.FromResult((IEnumerable<Room>)rooms)).Verifiable();

            var controller = this.CreateController();

            var returnedRooms = await controller.GetRooms(1);

            Assert.AreSame(rooms, returnedRooms);

            this.repository.Verify();
        }

        [TestMethod]
        public async Task ShouldFilterRoomsByComplexIdWhenGetRoomsIsCalled()
        {
            var rooms = new List<Room>();

            const int Id = 1;

            var matches = new Room { ComplexId = Id };
            var doesNotMatch = new Room { ComplexId = Id + 2 };

            this.roomService.Setup(cs => cs.GetAsync(It.Is<Expression<Func<Room, bool>>>(e => 
                e.Compile().Invoke(matches) && !e.Compile().Invoke(doesNotMatch))))
                .Returns(Task.FromResult((IEnumerable<Room>)rooms))
                .Verifiable();

            var controller = this.CreateController();

            await controller.GetRooms(1);

            this.repository.Verify();
        }

        public ComplexesController CreateController()
        {
            return new ComplexesController(this.complexService.Object, this.roomService.Object);
        }
    }
}
