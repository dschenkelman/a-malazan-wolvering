namespace PhoneTicket.Web.Tests.Controllers.Api
{
    using System;
    using System.Collections.Generic;
    using System.Linq;
    using System.Threading.Tasks;

    using Microsoft.VisualStudio.TestTools.UnitTesting;

    using Moq;

    using PhoneTicket.Web.Services;
    using PhoneTicket.Web.Models;
    using PhoneTicket.Web.Controllers.Api;
    using System.Linq.Expressions;

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
            const int id = 1;
            const string name = "C";
            const string address = "asd";

            var complex1 = new Complex { Id = id, Name = name, Address = address };

            this.complexService.Setup(cs => cs.GetAsync()).Returns(Task.FromResult((IEnumerable<Complex>)new List<Complex> { complex1 })).Verifiable();

            var controller = this.CreateController();

            var response = await controller.Get();

            this.complexService.Verify(cs => cs.GetAsync(), Times.Once());

            Assert.AreEqual(id, response.ToList()[0].Id);
            Assert.AreEqual(name, response.ToList()[0].Name);
            Assert.AreEqual(address, response.ToList()[0].Address);

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
