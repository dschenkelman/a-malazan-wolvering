namespace PhoneTicket.Web.ViewModels
{
    using System.Collections.Generic;

    public class CreateShowsViewModel
    {
        public double Price { get; set; }

        public string BeginDate { get; set; }

        public string EndDate { get; set; }

        public int Movie { get; set; }

        public List<DayViewModel> Days { get; set; }

        public List<TimesAndRoomViewModel> TimesAndRooms { get; set; }
    }
}