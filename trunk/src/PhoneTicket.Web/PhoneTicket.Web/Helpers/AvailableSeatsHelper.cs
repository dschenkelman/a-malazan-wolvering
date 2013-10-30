namespace PhoneTicket.Web.Helpers
{
    using System.Collections.Generic;
    using System.Linq;

    using PhoneTicket.Web.Models;
    using PhoneTicket.Web.ViewModels.Api;

    public class AvailableSeatsHelper
    {
        public static bool ValidateSeats(List<ArmChairViewModel> wantedSeats, IEnumerable<OccupiedSeat> occupiedSeats)
        {
            return (wantedSeats.Count(ws => occupiedSeats.Any(os => os.Row == ws.Row && os.Column == ws.Column)) == 0);
        }
    }
}