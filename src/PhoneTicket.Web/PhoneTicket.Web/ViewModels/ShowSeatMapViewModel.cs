namespace PhoneTicket.Web.ViewModels
{
    using PhoneTicket.Web.Models;

    public class ShowSeatMapViewModel
    {
        public int ShowId { get; set; }

        public ReportShowSeats ReportShowSeats { get; set; }

        public string Complex { get; set; }

        public string Movie { get; set; }

        public string Show { get; set; }
    }
}