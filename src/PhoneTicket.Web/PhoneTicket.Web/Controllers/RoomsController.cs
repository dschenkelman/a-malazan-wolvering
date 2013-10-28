namespace PhoneTicket.Web.Controllers
{
    using System.Collections.Generic;
    using System.IO;
    using System.Linq;
    using System.Threading.Tasks;
    using System.Web.Mvc;

    using PagedList;

    using PhoneTicket.Web.Handlers;
    using PhoneTicket.Web.Models;
    using PhoneTicket.Web.ViewModels;
    using PhoneTicket.Web.Services;
    using PhoneTicket.Web.Helpers;

    [Authorize]
    [RequireSsl]
    public class RoomsController : Controller
    {
        private readonly IRoomService roomService;
        private readonly IComplexService complexService;
        private readonly ICurrentUserRole currentUserRole;

        private readonly IRoomXmlParser roomXmlParser;

        private const int PageSize = 5;

        public RoomsController(IRoomService roomService, IComplexService complexService, ICurrentUserRole currentUserRole, IRoomXmlParser roomXmlParser)
        {
            this.roomService = roomService;
            this.complexService = complexService;
            this.currentUserRole = currentUserRole;
            this.roomXmlParser = roomXmlParser;
        }

        public async Task<ActionResult> Index(string roomSearch, int? page)
        {
            IEnumerable<Room> rooms;

            if (string.IsNullOrEmpty(roomSearch))
            {
                rooms = await this.roomService.GetAsync();
            }
            else
            {
                rooms = await this.roomService.GetAsync(r => r.Name.Contains(roomSearch));
            }

            var userCanEdit = this.currentUserRole.IsAdmin();

            ViewBag.CanEdit = userCanEdit;

            var roomsViewModels = rooms.Select(r => ListRoomViewModel.FromRoom(r, userCanEdit));

            return this.View(roomsViewModels.ToPagedList(page ?? 1, PageSize));
        }

        public async Task<ActionResult> Add()
        {
            var room = new Room { Id = -1, Name = string.Empty, ComplexId = -1, Complex = new Complex { Name = string.Empty }, Capacity = 0 };
            var availableComplexes = await this.complexService.ListAsync(room.ComplexId);

            var userCanEdit = this.currentUserRole.IsAdmin();

            var roomViewModel = ListRoomViewModel.FromRoom(room, userCanEdit);
            roomViewModel.AvailableComplexes = availableComplexes;

            return this.View(roomViewModel);
        }

        public async Task<ActionResult> CreateRoom(ListRoomViewModel roomViewModel)
        {
            var xml = string.Empty;

            using (var reader = new StreamReader(roomViewModel.RoomFile.InputStream))
            {
                xml = await reader.ReadToEndAsync();
            }

            var errors = this.roomXmlParser.Validate(xml);

            if (errors.Any())
            {
                foreach (RoomXmlError error in errors)
                {
                    var fullMessage = string.Format("{0} Linea: {1}", error.Message, error.Line);
                    this.ModelState.AddModelError(fullMessage, fullMessage);    
                }

                var availableComplexes = await this.complexService.ListAsync(roomViewModel.ComplexId);

                roomViewModel.AvailableComplexes = availableComplexes;

                return this.View("Add", roomViewModel);
            }

            var room = ListRoomViewModel.FromRoomViewModel(roomViewModel);
            room.File = xml;

            var seats = this.roomXmlParser.Parse(xml);

            room.Capacity = seats.Capacity;

            await this.roomService.CreateAsync(room);

            this.ViewBag.Message = string.Format("La sala \"{0}\" se ha guardado con éxito.", room.Name);
            this.ViewBag.LinkText = "Aceptar";
            this.ViewBag.Action = "Index";
            this.ViewBag.Controller = "Rooms";

            return this.View("~/Views/Shared/Confirmation.cshtml");
        }

        public async Task<ActionResult> Edit(int roomId)
        {
            var room = await this.roomService.GetAsync(roomId);
            var availableComplexes = await this.complexService.ListAsync(room.ComplexId);

            var userCanEdit = this.currentUserRole.IsAdmin();

            var roomViewModel = ListRoomViewModel.FromRoom(room, userCanEdit);
            roomViewModel.AvailableComplexes = availableComplexes;

            return this.View(roomViewModel);
        }

        public async Task<ActionResult> EditRoom(ListRoomViewModel roomViewModel)
        {
            var updatedRoom = ListRoomViewModel.FromRoomViewModel(roomViewModel);

            var existingRoom = await this.roomService.GetAsync(updatedRoom.Id);

            existingRoom.Name = updatedRoom.Name;
            existingRoom.ComplexId = updatedRoom.ComplexId;
            existingRoom.Capacity = updatedRoom.Capacity;

            await this.roomService.UpdateAsync(existingRoom);

            this.ViewBag.Message = string.Format("La sala \"{0}\" se ha modificado con éxito.", existingRoom.Name);
            this.ViewBag.LinkText = "Aceptar";
            this.ViewBag.Action = "Index";
            this.ViewBag.Controller = "Rooms";

            return this.View("~/Views/Shared/Confirmation.cshtml");
        }

        public async Task<ActionResult> Delete(int roomId)
        {
            await this.roomService.DeleteAsync(roomId);

            this.ViewBag.Message = string.Format("La sala ha sido borrada.");
            this.ViewBag.LinkText = "Aceptar";
            this.ViewBag.Action = "Index";
            this.ViewBag.Controller = "Rooms";
            this.ViewBag.RouteValues = new { page = 1 };

            return this.View("~/Views/Shared/Confirmation.cshtml");
        }

     
    }
}
