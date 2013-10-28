namespace PhoneTicket.Web.Controllers.Api
{
    using System.Threading.Tasks;
    using System.Web.Http;

    using PhoneTicket.Web.Models;
    using PhoneTicket.Web.Services;

    [RoutePrefix("api/shows")]
    public class ShowsController : ApiController
    {
        private readonly IShowService showService;

        private readonly IRoomService roomService;

        private readonly IRoomXmlParser roomXmlParser;

        public ShowsController(IShowService showService, IRoomService roomService, IRoomXmlParser roomXmlParser)
        {
            this.showService = showService;
            this.roomService = roomService;
            this.roomXmlParser = roomXmlParser;
        }

        [HttpGet("{id}/seats")]
        public async Task<SeatState[][]> Seats(int id)
        {
            var show = await this.showService.GetAsync(id);

            var room = await this.roomService.GetAsync(show.RoomId);

            var showSeats = this.roomXmlParser.Parse(room.File);

            var occupiedSeats = await this.showService.GetOccupiedSeats(id);

            foreach (var seat in occupiedSeats)
            {
                showSeats.MarkTaken(seat.Row, seat.Column);
            }

            return showSeats.Seats;
        }
    }
}