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
                Assert.AreEqual(string.Format("RoomType{0}", i), item.TypeDescription);
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
                Assert.AreEqual(string.Format("RoomType{0}", i), item.TypeDescription);
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
        public async Task ShouldSetModelToRoomWithDefaultValuesWhenCallingAdd()
        {
            const int Undefined = -1;

            this.complexService.Setup(rs => rs.ListAsync(It.IsAny<int?>())).Returns(Task.FromResult<IEnumerable<SelectListItem>>(null));
            this.roomTypeService.Setup(rs => rs.ListAsync(It.IsAny<int?>())).Returns(Task.FromResult<IEnumerable<SelectListItem>>(null));

            var controller = this.CreateController();

            var result = (ViewResult)await controller.Add();

            var room = (ListRoomViewModel)result.Model;

            Assert.AreEqual(Undefined, room.Id);
            Assert.AreEqual(string.Empty, room.Name);
            Assert.AreEqual(string.Empty, room.ComplexName);
            Assert.AreEqual(0, room.Capacity);
            Assert.AreEqual(string.Empty, room.TypeDescription);
        }

        [TestMethod]
        public async Task ShouldSetAvailableComplexesToListReturnedFromComplexServiceWhenCallingAdd()
        {
            var complexes = new List<SelectListItem>();

            this.complexService.Setup(rs => rs.ListAsync(It.IsAny<int?>())).Returns(Task.FromResult<IEnumerable<SelectListItem>>(complexes));
            this.roomTypeService.Setup(rs => rs.ListAsync(It.IsAny<int?>())).Returns(Task.FromResult<IEnumerable<SelectListItem>>(null));

            var controller = this.CreateController();

            var result = (ViewResult)await controller.Add();

            var room = (ListRoomViewModel)result.Model;

            Assert.AreSame(complexes, room.AvailableComplexes);
        }

        [TestMethod]
        public async Task ShouldSetAvailableRoomTypesToListReturnedFromRoomTypeServiceWhenCallingAdd()
        {
            var roomTypes = new List<SelectListItem>();

            this.complexService.Setup(rs => rs.ListAsync(It.IsAny<int?>())).Returns(Task.FromResult<IEnumerable<SelectListItem>>(null));
            this.roomTypeService.Setup(rs => rs.ListAsync(It.IsAny<int?>())).Returns(Task.FromResult<IEnumerable<SelectListItem>>(roomTypes));

            var controller = this.CreateController();

            var result = (ViewResult)await controller.Add();

            var room = (ListRoomViewModel)result.Model;

            Assert.AreSame(roomTypes, room.AvailableRoomTypes);
        }

        [TestMethod]
        public async Task ShouldSetRoomIdToMinusOneWhenCallingAdd()
        {
            this.complexService.Setup(rs => rs.ListAsync(It.IsAny<int?>())).Returns(Task.FromResult<IEnumerable<SelectListItem>>(null));
            this.roomTypeService.Setup(rs => rs.ListAsync(It.IsAny<int?>())).Returns(Task.FromResult<IEnumerable<SelectListItem>>(null));

            var controller = this.CreateController();

            var result = (ViewResult)await controller.Add();

            var room = (ListRoomViewModel)result.Model;

            Assert.AreEqual(-1, room.Id);
        }

        [TestMethod]
        public async Task ShouldSetRoomIdToRoomViewModelIdWhenCallingEdit()
        {
            const int RoomId = 100;
            var room = new Room() { Id = RoomId, Name = string.Empty, ComplexId = -1, 
                                    Complex = new Complex { Name = string.Empty }, Capacity = 0, 
                                    TypeId = -1, Type = new RoomType { Description = string.Empty } };

            this.complexService.Setup(rs => rs.ListAsync(It.IsAny<int?>())).Returns(Task.FromResult<IEnumerable<SelectListItem>>(null));
            this.roomTypeService.Setup(rs => rs.ListAsync(It.IsAny<int?>())).Returns(Task.FromResult<IEnumerable<SelectListItem>>(null));

            this.roomService.Setup(rs => rs.GetAsync(RoomId)).Returns(Task.FromResult(room)).Verifiable();

            var controller = this.CreateController();

            var result = (ViewResult)await controller.Edit(100);

            var roomViewModel = (ListRoomViewModel)result.Model;

            this.roomService.Verify(rs => rs.GetAsync(RoomId), Times.Once());
            Assert.AreEqual(RoomId, roomViewModel.Id);
        }

        [TestMethod]
        public async Task ShouldSetAvailableComplexesToListReturnedFromComplexesServiceWhenCallingEdit()
        {
            const int ComplexId = 100;
            const int RoomId = 100;
            var room = new Room()
            {
                Id = RoomId,
                Name = string.Empty,
                ComplexId = ComplexId,
                Complex = new Complex { Name = string.Empty },
                Capacity = 0,
                TypeId = -1,
                Type = new RoomType { Description = string.Empty }
            };

            var complexes = new List<SelectListItem>();
            this.roomService.Setup(ms => ms.GetAsync(RoomId)).Returns(Task.FromResult(room));

            this.complexService.Setup(rs => rs.ListAsync(ComplexId)).Returns(Task.FromResult<IEnumerable<SelectListItem>>(complexes)).Verifiable();
            this.roomTypeService.Setup(rs => rs.ListAsync(It.IsAny<int?>())).Returns(Task.FromResult<IEnumerable<SelectListItem>>(null));

            var controller = this.CreateController();

            var result = (ViewResult)await controller.Edit(RoomId);

            this.complexService.Verify(rs => rs.ListAsync(RoomId), Times.Once());
            Assert.AreSame(complexes, ((ListRoomViewModel)result.Model).AvailableComplexes);
        }

        [TestMethod]
        public async Task ShouldSetAvailableRoomTypesToListReturnedFromRoomTypesServiceWhenCallingEdit()
        {
            const int TypeId = 100;
            const int RoomId = 100;
            var room = new Room()
            {
                Id = RoomId,
                Name = string.Empty,
                ComplexId = -1,
                Complex = new Complex { Name = string.Empty },
                Capacity = 0,
                TypeId = TypeId,
                Type = new RoomType { Description = string.Empty }
            };

            var roomTypes = new List<SelectListItem>();

            this.roomService.Setup(ms => ms.GetAsync(RoomId)).Returns(Task.FromResult(room));

            this.complexService.Setup(rs => rs.ListAsync(It.IsAny<int?>())).Returns(Task.FromResult<IEnumerable<SelectListItem>>(null)).Verifiable();
            this.roomTypeService.Setup(rs => rs.ListAsync(TypeId)).Returns(Task.FromResult<IEnumerable<SelectListItem>>(roomTypes));

            var controller = this.CreateController();

            var result = (ViewResult)await controller.Edit(RoomId);

            this.roomTypeService.Verify(rs => rs.ListAsync(RoomId), Times.Once());
            Assert.AreSame(roomTypes, ((ListRoomViewModel)result.Model).AvailableRoomTypes);
        }

        [TestMethod]
        public async Task ShouldSetModelToRoomRetrievedFromServiceWhenCallingEdit()
        {
            const int RoomId = 100;
            var room = new Room()
            {
                Id = RoomId,
                Name = string.Empty,
                ComplexId = -1,
                Complex = new Complex { Name = string.Empty },
                Capacity = 0,
                TypeId = -1,
                Type = new RoomType { Description = string.Empty }
            };

            var roomVM = ListRoomViewModel.FromRoom(room);

            this.roomService.Setup(ms => ms.GetAsync(RoomId)).Returns(Task.FromResult(room));
            this.complexService.Setup(rs => rs.ListAsync(It.IsAny<int?>())).Returns(Task.FromResult<IEnumerable<SelectListItem>>(null));
            this.roomTypeService.Setup(rs => rs.ListAsync(It.IsAny<int?>())).Returns(Task.FromResult<IEnumerable<SelectListItem>>(null));
            
            var controller = this.CreateController();

            var result = (ViewResult)await controller.Edit(RoomId);

            var roomObtained = (ListRoomViewModel)result.Model;

            Assert.AreEqual(roomVM.Id, roomObtained.Id);
            Assert.AreEqual(roomVM.Capacity, roomObtained.Capacity);
            Assert.AreEqual(roomVM.ComplexId, roomObtained.ComplexId);
            Assert.AreEqual(roomVM.TypeId, roomObtained.TypeId);
        }

        [TestMethod]
        public async Task ShouldUpdateRoomRetrievedFromServiceBasedOnUpdatedRoomProperties()
        {
            const int RoomId = 100;
            const int ComplexId = 1;
            const int TypeId = 1;
            var updatedRoom = new Room()
            {
                Id = RoomId,
                Name = string.Empty,
                ComplexId = ComplexId,
                Complex = new Complex { Name = string.Empty },
                Capacity = 0,
                TypeId = TypeId,
                Type = new RoomType { Description = string.Empty }
            };

            var existingRoom = new Room { Id = RoomId };

            var controller = this.CreateController();

            this.roomService.Setup(ms => ms.GetAsync(RoomId)).Returns(Task.FromResult(existingRoom)).Verifiable();
            this.roomService.Setup(ms => ms.UpdateAsync(existingRoom)).Returns(Task.FromResult<object>(null)).Verifiable();

            await controller.EditRoom(ListRoomViewModel.FromRoom(updatedRoom));

            this.roomService.Verify(rs => rs.GetAsync(updatedRoom.Id), Times.Once());

            this.roomService.Verify(
                rs => rs.UpdateAsync(It.Is<Room>(r =>
                    r.Id == RoomId
                    && r.ComplexId == ComplexId
                    && r.Capacity == updatedRoom.Capacity
                    && r.TypeId == updatedRoom.TypeId)),
                Times.Once());
        }

        [TestMethod]
        public async Task ShouldRedirectToIndexViewWhenEditingRoomIsFinished()
        {
            const int ComplexId = 4;
            const int TypeId = 2;
            var room = new Room()
            {
                Id = 2,
                Name = string.Empty,
                ComplexId = ComplexId,
                Complex = new Complex { Name = string.Empty },
                Capacity = 0,
                TypeId = TypeId,
                Type = new RoomType { Description = string.Empty }
            };

            var controller = this.CreateController();

            this.roomService.Setup(ms => ms.GetAsync(room.Id)).Returns(Task.FromResult(room));
            this.roomService.Setup(ms => ms.UpdateAsync(It.IsAny<Room>())).Returns(Task.FromResult<object>(null));

            var result = (ViewResult)await controller.EditRoom(ListRoomViewModel.FromRoom(room));

            Assert.AreEqual("~/Views/Shared/Confirmation.cshtml", result.ViewName);
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
