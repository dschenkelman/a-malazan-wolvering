namespace PhoneTicket.Web.Tests.Controllers
{
    
    using System;
    using System.Collections.Generic;
    using System.Linq;
    using System.Text;
    using System.Threading.Tasks;

    using Microsoft.VisualStudio.TestTools.UnitTesting;
    
    using Moq;
    
    using PhoneTicket.Web.Services;
    using PhoneTicket.Web.Models;
    using PhoneTicket.Web.Controllers;
    using PhoneTicket.Web.ViewModels;
    using PagedList;
    using System.Web.Mvc;
    using System.Linq.Expressions;

    [TestClass]
    public class RoomsControllerTests
    {
        private MockRepository mockRepository;

        private Mock<IRoomService> roomService;
        private Mock<IComplexService> complexService;
        private Mock<IRoomTypeService> roomTypeService;

        [TestInitialize]
        public void Initialize()
        {
            this.mockRepository = new MockRepository(MockBehavior.Default);
            this.roomService = this.mockRepository.Create<IRoomService>();
            this.complexService = this.mockRepository.Create<IComplexService>();
            this.roomTypeService = this.mockRepository.Create<IRoomTypeService>();
        }

        [TestMethod]
        public async Task ShouldReturnItemsOnPageOneWhenRoomIndexIsCalledIfNoPageIsProvided()
        {
            const string RoomFormat = "Room {0}";
            var rooms = new List<Room>();
            const int Items = 25;

            for (int i = 0; i < Items; i++)
            {
                rooms.Add(
                    new Room()
                    {
                        Id = i,
                        Name = string.Format(RoomFormat, i),
                        Complex = new Complex { Id = i, Name = string.Format("Complex{0}",i)},
                        Capacity = i,
                        Type = new RoomType { Id = i, Description = string.Format("RoomType{0}",i)}
                    });
            }

            this.roomService.Setup(rs => rs.GetAsync()).Returns(Task.FromResult((IEnumerable<Room>)rooms)).Verifiable();

            var controller = this.CreateController();

            var result = (ViewResult)await controller.Index(string.Empty, null);

            var pagedList = (IPagedList<ListRoomViewModel>)result.ViewData.Model;

            var pages = Math.Ceiling((double)Items / pagedList.PageSize);

            Assert.AreEqual(Items, pagedList.TotalItemCount);
            Assert.IsTrue(pagedList.HasNextPage);
            Assert.IsFalse(pagedList.HasPreviousPage);
            Assert.IsTrue(pagedList.IsFirstPage);
            Assert.AreEqual(pages, pagedList.PageCount);

            for (int i = 0; i < pagedList.Count; i++)
            {
                var item = pagedList[i];
                Assert.AreEqual(i, item.Id);
                Assert.AreEqual(i, item.Capacity);
                Assert.AreEqual(string.Format(RoomFormat, i), item.Name);
                Assert.AreEqual(string.Format("Complex{0}", i), item.ComplexName);
                Assert.AreEqual(string.Format("RoomType{0}", i), item.Type);
            }
        }

        [TestMethod]
        public async Task ShouldReturnItemsOnPageNumberWhenRoomIndexIsCalledIfWithPageNumberIsProvided()
        {
            const string RoomFormat = "Room {0}";
            const int PageNumber = 3;
            var rooms = new List<Room>();
            const int Items = 25;

            for (int i = 0; i < Items; i++)
            {
                rooms.Add(
                    new Room()
                    {
                        Id = i,
                        Name = string.Format(RoomFormat, i),
                        Complex = new Complex { Id = i, Name = string.Format("Complex{0}", i) },
                        Capacity = i,
                        Type = new RoomType { Id = i, Description = string.Format("RoomType{0}", i) }
                    });
            }

            this.roomService.Setup(rs => rs.GetAsync()).Returns(Task.FromResult((IEnumerable<Room>)rooms)).Verifiable();

            var controller = this.CreateController();

            var result = (ViewResult)await controller.Index(string.Empty, PageNumber);

            var pagedList = (IPagedList<ListRoomViewModel>)result.ViewData.Model;

            var pages = Math.Ceiling((double)Items / pagedList.PageSize);

            Assert.AreEqual(Items, pagedList.TotalItemCount);
            Assert.IsTrue(pagedList.HasNextPage);
            Assert.IsTrue(pagedList.HasPreviousPage);
            Assert.IsFalse(pagedList.IsFirstPage);
            Assert.AreEqual(pages, pagedList.PageCount);

            for (int i = 10; i < pagedList.Count; i++)
            {
                var item = pagedList[i];
                Assert.AreEqual(i, item.Id);
                Assert.AreEqual(i, item.Capacity);
                Assert.AreEqual(string.Format(RoomFormat, i), item.Name);
                Assert.AreEqual(string.Format("Complex{0}", i), item.ComplexName);
                Assert.AreEqual(string.Format("RoomType{0}", i), item.Type);
            }
        }

        [TestMethod]
        public async Task ShouldProvideNameFilterToRoomServiceIfTitleSearchIsProvided()
        {
            const int PageNumber = 1;
            var rooms = new List<Room>();

            var matchesFilter = new Room() { Name = "Room 1" };
            var doesNotMatchFilter = new Room { Name = "Room 2" };


            this.roomService.Setup(
                rs =>
                rs.GetAsync(
                    It.Is<Expression<Func<Room, bool>>>(
                        e =>
                        e.Compile().Invoke(matchesFilter)
                        && !e.Compile().Invoke(doesNotMatchFilter))))
                .Returns(Task.FromResult((IEnumerable<Room>)rooms)).Verifiable();

            var controller = this.CreateController();

            await controller.Index("1", PageNumber);

            this.roomService.Verify(
                rs =>
                rs.GetAsync(
                    It.Is<Expression<Func<Room, bool>>>(
                        e => e.Compile().Invoke(matchesFilter) && !e.Compile().Invoke(doesNotMatchFilter))),
                Times.Once());
        }

        [TestMethod]
        public async Task ShouldDeletUsingIdWhenDeleteIsCalled()
        {
            const int RoomId = 1;

            this.roomService.Setup(rs => rs.DeleteAsync(RoomId))
                .Returns(Task.FromResult<object>(null))
                .Verifiable();

            var controller = this.CreateController();

            await controller.Delete(RoomId);

            this.roomService.Verify(ms => ms.DeleteAsync(RoomId), Times.Once());
        }

        [TestMethod]
        public async Task ShouldRedirectToRoomIndexViewWhenDeletingARoom()
        {
            const int RoomId = 1;

            this.roomService.Setup(rs => rs.DeleteAsync(RoomId))
                .Returns(Task.FromResult<object>(null))
                .Verifiable();

            var controller = this.CreateController();

            var result = (RedirectToRouteResult)await controller.Delete(RoomId);

            Assert.AreEqual("Index", result.RouteValues["action"]);
            Assert.AreEqual("Rooms", result.RouteValues["controller"]);
        }

        private RoomsController CreateController()
        {
            return new RoomsController(this.roomService.Object, this.complexService.Object, this.roomTypeService.Object);
        }

        
    }
}
