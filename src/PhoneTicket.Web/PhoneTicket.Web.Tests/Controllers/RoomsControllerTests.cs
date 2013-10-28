namespace PhoneTicket.Web.Tests.Controllers
{
    using System;
    using System.Collections.Generic;
    using System.IO;
    using System.Linq;
    using System.Linq.Expressions;
    using System.Text;
    using System.Threading.Tasks;
    using System.Web;
    using System.Web.Mvc;

    using Microsoft.VisualStudio.TestTools.UnitTesting;

    using Moq;

    using PagedList;

    using PhoneTicket.Web.Controllers;
    using PhoneTicket.Web.Helpers;
    using PhoneTicket.Web.Models;
    using PhoneTicket.Web.Services;
    using PhoneTicket.Web.ViewModels;

    [TestClass]
    public class RoomsControllerTests
    {
        private MockRepository mockRepository;

        private Mock<IRoomService> roomService;
        private Mock<IComplexService> complexService;
        private Mock<ICurrentUserRole> currentUserRole;

        private Mock<IRoomXmlParser> roomXmlParser;

        [TestInitialize]
        public void Initialize()
        {
            this.mockRepository = new MockRepository(MockBehavior.Default);
            this.roomService = this.mockRepository.Create<IRoomService>();
            this.complexService = this.mockRepository.Create<IComplexService>();
            this.currentUserRole = this.mockRepository.Create<ICurrentUserRole>();
            this.roomXmlParser = this.mockRepository.Create<IRoomXmlParser>();
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
                Assert.AreEqual(string.Format(RoomFormat, i), item.Name);
                Assert.AreEqual(string.Format("Complex{0}", i), item.ComplexName);
                Assert.AreEqual(i, item.Capacity);
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
                Assert.AreEqual(string.Format(RoomFormat, i), item.Name);
                Assert.AreEqual(string.Format("Complex{0}", i), item.ComplexName);
                Assert.AreEqual(i, item.Capacity);
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
        public async Task ShouldSetCanEditDependingOnUserRoleWhenRoomIndexIsCalled()
        {
            const bool canEdit = false;
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
                    });
            }
            

            this.roomService.Setup(rs => rs.GetAsync()).Returns(Task.FromResult((IEnumerable<Room>)rooms));

            this.currentUserRole.Setup(ur => ur.IsAdmin()).Returns(canEdit);

            var controller = this.CreateController();

            var result = (ViewResult)await controller.Index(string.Empty, PageNumber);

            var pagedList = (IPagedList<ListRoomViewModel>)result.ViewData.Model;

            for (int i = 10; i < pagedList.Count; i++)
            {
                var item = pagedList[i];
                Assert.AreEqual(canEdit, item.CanEdit);
                Assert.AreEqual(i, item.Capacity);
            }
        }

        [TestMethod]
        public async Task ShouldSetModelToRoomWithDefaultValuesWhenCallingAdd()
        {
            const int Undefined = -1;

            this.complexService.Setup(rs => rs.ListAsync(It.IsAny<int?>())).Returns(Task.FromResult<IEnumerable<SelectListItem>>(null));

            var controller = this.CreateController();

            var result = (ViewResult)await controller.Add();

            var room = (ListRoomViewModel)result.Model;

            Assert.AreEqual(Undefined, room.Id);
            Assert.AreEqual(string.Empty, room.Name);
            Assert.AreEqual(string.Empty, room.ComplexName);
        }

        [TestMethod]
        public async Task ShouldSetAvailableComplexesToListReturnedFromComplexServiceWhenCallingAdd()
        {
            var complexes = new List<SelectListItem>();

            this.complexService.Setup(rs => rs.ListAsync(It.IsAny<int?>())).Returns(Task.FromResult<IEnumerable<SelectListItem>>(complexes));

            var controller = this.CreateController();

            var result = (ViewResult)await controller.Add();

            var room = (ListRoomViewModel)result.Model;

            Assert.AreSame(complexes, room.AvailableComplexes);
        }

        [TestMethod]
        public async Task ShouldSetRoomIdToMinusOneWhenCallingAdd()
        {
            this.complexService.Setup(rs => rs.ListAsync(It.IsAny<int?>())).Returns(Task.FromResult<IEnumerable<SelectListItem>>(null));

            var controller = this.CreateController();

            var result = (ViewResult)await controller.Add();

            var room = (ListRoomViewModel)result.Model;

            Assert.AreEqual(-1, room.Id);
        }

        [TestMethod]
        public async Task ShouldSetRoomIdToRoomViewModelIdWhenCallingEdit()
        {
            const int RoomId = 100;
            var room = new Room()
                           {
                               Id = RoomId,
                               Name = string.Empty,
                               ComplexId = -1,
                               Complex = new Complex { Name = string.Empty },
                               Capacity = 0,
                           };

            this.complexService.Setup(rs => rs.ListAsync(It.IsAny<int?>()))
                .Returns(Task.FromResult<IEnumerable<SelectListItem>>(null));

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
            };

            var complexes = new List<SelectListItem>();
            this.roomService.Setup(ms => ms.GetAsync(RoomId)).Returns(Task.FromResult(room));

            this.complexService.Setup(rs => rs.ListAsync(ComplexId)).Returns(Task.FromResult<IEnumerable<SelectListItem>>(complexes)).Verifiable();

            var controller = this.CreateController();

            var result = (ViewResult)await controller.Edit(RoomId);

            this.complexService.Verify(rs => rs.ListAsync(RoomId), Times.Once());
            Assert.AreSame(complexes, ((ListRoomViewModel)result.Model).AvailableComplexes);
        }

        [TestMethod]
        public async Task ShouldSetModelToRoomRetrievedFromServiceWhenCallingEdit()
        {
            const bool canEdit = true;
            const int RoomId = 100;
            var room = new Room()
            {
                Id = RoomId,
                Name = string.Empty,
                ComplexId = -1,
                Complex = new Complex { Name = string.Empty },
                Capacity = 0,
            };

            this.roomService.Setup(ms => ms.GetAsync(RoomId)).Returns(Task.FromResult(room));
            this.complexService.Setup(rs => rs.ListAsync(It.IsAny<int?>())).Returns(Task.FromResult<IEnumerable<SelectListItem>>(null));
            this.currentUserRole.Setup(ur => ur.IsAdmin()).Returns(canEdit);

            var roomVM = ListRoomViewModel.FromRoom(room,canEdit);

            var controller = this.CreateController();

            var result = (ViewResult)await controller.Edit(RoomId);

            var roomObtained = (ListRoomViewModel)result.Model;

            Assert.AreEqual(roomVM.Id, roomObtained.Id);
            Assert.AreEqual(roomVM.ComplexId, roomObtained.ComplexId);
        }

        [TestMethod]
        public async Task ShouldUpdateRoomRetrievedFromServiceBasedOnUpdatedRoomProperties()
        {
            const bool CanEdit = true;
            const int RoomId = 100;
            const int ComplexId = 1;
            var updatedRoom = new Room()
            {
                Id = RoomId,
                Name = string.Empty,
                ComplexId = ComplexId,
                Complex = new Complex { Name = string.Empty },
                Capacity = 0,
            };

            var existingRoom = new Room { Id = RoomId };

            var controller = this.CreateController();

            this.roomService.Setup(ms => ms.GetAsync(RoomId)).Returns(Task.FromResult(existingRoom)).Verifiable();
            this.roomService.Setup(ms => ms.UpdateAsync(existingRoom)).Returns(Task.FromResult<object>(null)).Verifiable();
            this.currentUserRole.Setup(ur => ur.IsAdmin()).Returns(CanEdit);

            await controller.EditRoom(ListRoomViewModel.FromRoom(updatedRoom,CanEdit));

            this.roomService.Verify(rs => rs.GetAsync(updatedRoom.Id), Times.Once());

            this.roomService.Verify(
                rs => rs.UpdateAsync(It.Is<Room>(r =>
                    r.Id == RoomId
                    && r.ComplexId == ComplexId
                    && r.Capacity == updatedRoom.Capacity)),
                Times.Once());
        }

        [TestMethod]
        public async Task ShouldRedirectToIndexViewWhenEditingRoomIsFinished()
        {
            const bool CanEdit = true;
            const int ComplexId = 4;
            const int TypeId = 2;
            var room = new Room()
            {
                Id = 2,
                Name = string.Empty,
                ComplexId = ComplexId,
                Complex = new Complex { Name = string.Empty },
                Capacity = 0,
            };

            var controller = this.CreateController();

            this.roomService.Setup(ms => ms.GetAsync(room.Id)).Returns(Task.FromResult(room));
            this.roomService.Setup(ms => ms.UpdateAsync(It.IsAny<Room>())).Returns(Task.FromResult<object>(null));
            this.currentUserRole.Setup(ur => ur.IsAdmin()).Returns(CanEdit);

            var result = (ViewResult)await controller.EditRoom(ListRoomViewModel.FromRoom(room,CanEdit));

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

            var result = (ViewResult)await controller.Delete(RoomId);

            Assert.AreEqual("~/Views/Shared/Confirmation.cshtml", result.ViewName);
        }

        [TestMethod]
        public async Task ShouldSetModelErrorsAndReturnSameViewIfXmlValidationFails()
        {
            // arrange
            const string FileContent = "<Sala></Sala>";

            var memoryStream = new MemoryStream();
            var bytes = Encoding.UTF8.GetBytes(FileContent);
            await memoryStream.WriteAsync(bytes, 0, bytes.Length);
            memoryStream.Seek(0, SeekOrigin.Begin);

            var viewModel = new ListRoomViewModel() { ComplexId = 3 };
            var file = this.mockRepository.Create<HttpPostedFileBase>();

            file.Setup(f => f.InputStream).Returns(memoryStream).Verifiable();

            viewModel.RoomFile = file.Object;

            var errors = new List<RoomXmlError> { new RoomXmlError(1, "M1"), new RoomXmlError(2, "M2") };

            this.roomXmlParser.Setup(rxp => rxp.Validate(FileContent)).Returns(errors).Verifiable();

            this.complexService.Setup(cs => cs.ListAsync(viewModel.ComplexId)).Returns(Task.FromResult(Enumerable.Empty<SelectListItem>())).Verifiable();

            var controller = this.CreateController();
            
            // act
            var result = (ViewResult)await controller.CreateRoom(viewModel);

            // assert
            Assert.AreEqual(1, controller.ModelState.Count);
            Assert.AreEqual("Add", result.ViewName);
            Assert.AreSame(viewModel, result.Model);

            const string FullMessageFormat = "{0} Linea: {1}";
            var fullMessage1 = string.Format(FullMessageFormat, errors[0].Message, errors[0].Line);
            Assert.AreEqual(fullMessage1, controller.ModelState[string.Empty].Errors[0].ErrorMessage);

            var fullMessage2 = string.Format(FullMessageFormat, errors[1].Message, errors[1].Line);
            Assert.AreEqual(fullMessage2, controller.ModelState[string.Empty].Errors[1].ErrorMessage);

            this.mockRepository.VerifyAll();
        }

        [TestMethod]
        public async Task ShouldSetFileContentToRoomFileProperty()
        {
            // arrange
            const string FileContent = "<Sala></Sala>";

            var memoryStream = new MemoryStream();
            var bytes = Encoding.UTF8.GetBytes(FileContent);
            await memoryStream.WriteAsync(bytes, 0, bytes.Length);
            memoryStream.Seek(0, SeekOrigin.Begin);

            var viewModel = new ListRoomViewModel();
            var file = this.mockRepository.Create<HttpPostedFileBase>();

            file.Setup(f => f.InputStream).Returns(memoryStream).Verifiable();

            viewModel.RoomFile = file.Object;

            this.roomXmlParser.Setup(rxp => rxp.Validate(FileContent)).Returns(Enumerable.Empty<RoomXmlError>()).Verifiable();

            this.roomXmlParser.Setup(rxp => rxp.Parse(FileContent)).Returns(new ShowSeats()).Verifiable();

            this.roomService.Setup(rs => rs.CreateAsync(It.Is<Room>(r => r.File == FileContent))).Returns(Task.FromResult<object>(null)).Verifiable();

            var controller = this.CreateController();

            // act
            await controller.CreateRoom(viewModel);

            this.mockRepository.VerifyAll();
        }

        [TestMethod]
        public async Task ShouldSetRoomCapacityFromParsedSeats()
        {
            // arrange
            const string FileContent = "<Sala></Sala>";

            var memoryStream = new MemoryStream();
            var bytes = Encoding.UTF8.GetBytes(FileContent);
            await memoryStream.WriteAsync(bytes, 0, bytes.Length);
            memoryStream.Seek(0, SeekOrigin.Begin);

            var viewModel = new ListRoomViewModel();
            var file = this.mockRepository.Create<HttpPostedFileBase>();

            file.Setup(f => f.InputStream).Returns(memoryStream).Verifiable();

            var seats = new ShowSeats();

            for (int i = 0; i < 17; i++)
            {
                for (int j = 0; j < 22; j++)
                {
                    if (i % 2 == 0)
                    {
                        seats.MarkFree(i + 1, j + 1);
                    }
                }
            }

            Assert.AreEqual(198, seats.Capacity);

            viewModel.RoomFile = file.Object;

            this.roomXmlParser.Setup(rxp => rxp.Validate(FileContent)).Returns(Enumerable.Empty<RoomXmlError>()).Verifiable();

            this.roomXmlParser.Setup(rxp => rxp.Parse(FileContent)).Returns(seats).Verifiable();

            this.roomService.Setup(rs => rs.CreateAsync(It.Is<Room>(r => r.Capacity == 198))).Returns(Task.FromResult<object>(null)).Verifiable();

            var controller = this.CreateController();

            // act
            await controller.CreateRoom(viewModel);

            this.mockRepository.VerifyAll();
        }

        private RoomsController CreateController()
        {
            return new RoomsController(this.roomService.Object, this.complexService.Object, this.currentUserRole.Object, this.roomXmlParser.Object);
        }
    }
}
