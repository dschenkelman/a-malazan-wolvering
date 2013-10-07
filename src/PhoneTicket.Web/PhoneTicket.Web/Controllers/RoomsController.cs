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

        private const int PageSize = 5;

        public RoomsController(IRoomService roomService)
        {
            this.roomService = roomService;
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
            return this.View();
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
