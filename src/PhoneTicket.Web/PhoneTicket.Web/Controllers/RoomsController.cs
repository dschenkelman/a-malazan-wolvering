namespace PhoneTicket.Web.Controllers
{
    using System.Collections.Generic;
    using System.Linq;
    using System.Threading.Tasks;
    using System.Web.Mvc;

    using PagedList;

    using PhoneTicket.Web.Handlers;
    using PhoneTicket.Web.Models;
    using PhoneTicket.Web.ViewModels;
    using PhoneTicket.Web.Services;
    

    [Authorize]
    [RequireSsl]
    public class RoomsController : Controller
    {
        private readonly IRoomService roomService;
        private readonly IComplexService complexService;
        private readonly IRoomTypeService roomTypeService;

        private const int PageSize = 5;

        public RoomsController(IRoomService roomService, IComplexService complexService, IRoomTypeService roomTypeService)
        {
            this.roomService = roomService;
            this.complexService = complexService;
            this.roomTypeService = roomTypeService;
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

            var roomsViewModels = rooms.Select(ListRoomViewModel.FromRoom);

            return this.View(roomsViewModels.ToPagedList(page ?? 1, PageSize));
        }

        public async Task<ActionResult> Add()
        {
            var room = new Room() { Id = -1, Name = string.Empty, ComplexId = -1, Complex = new Complex { Name = "" }, Capacity = 0, TypeId = -1, Type = new RoomType{ Description = "" } };
            var availableComplexes = await this.complexService.ListAsync(room.ComplexId);
            var availableRoomTypes = await this.roomTypeService.ListAsync(room.TypeId);
            
            var roomViewModel = ListRoomViewModel.FromRoom(room);
            roomViewModel.AvailableComplexes = availableComplexes;
            roomViewModel.AvailableRoomTypes = availableRoomTypes;

            return this.View(roomViewModel);
        }

        public async Task<ActionResult> CreateRoom(ListRoomViewModel roomViewModel)
        {
            var room = ListRoomViewModel.FromRoomViewModel(roomViewModel);

            await this.roomService.CreateAsync(room);

            this.ViewBag.Message = string.Format("La sala \"{0}\" se ha guardado con éxito.", room.Name);
            this.ViewBag.LinkText = "Aceptar";
            this.ViewBag.Action = "Index";
            this.ViewBag.Controller = "Rooms";

            return this.View("~/Views/Shared/Confirmation.cshtml");
        }

        public async Task<ActionResult> Edit(int roomId)
        {
            return this.View();
        }

        public async Task<ActionResult> Delete(int roomId)
        {
            await this.roomService.DeleteAsync(roomId);

            return RedirectToAction("Index", "Rooms", new { page = 1 });
        }

     
    }
}
