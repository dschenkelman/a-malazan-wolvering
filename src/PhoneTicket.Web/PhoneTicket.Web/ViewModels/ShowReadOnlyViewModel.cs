namespace PhoneTicket.Web.ViewModels
{
    using System;
    using System.ComponentModel;

    using PhoneTicket.Web.Models;

    public class ShowReadOnlyViewModel
    {
        public int MovieId { get; set; }

        [DisplayName("Fecha")]
        public DateTime Date { get; set; }

        [DisplayName("Horario")]
        public string Time { get; set; }

        [DisplayName("Sala")]
        public string Room { get; set; }

        [DisplayName("Complejo")]
        public string Complex { get; set; }

        [DisplayName("Película")]
        public string Movie { get; set; }

        [DisplayName("Precio")]
        public double Price { get; set; }

        public static ShowReadOnlyViewModel FromShow(Show show)
        {
            return new ShowReadOnlyViewModel
                       {
                           Price = show.Price,
                           Date = show.Date,
                           Time = show.Date.ToString("hh:mm"),
                           Movie = show.Movie.Title,
                           Room = show.Room.Name,
                           Complex = show.Room.Complex.Name
                       };
        }
    }
}