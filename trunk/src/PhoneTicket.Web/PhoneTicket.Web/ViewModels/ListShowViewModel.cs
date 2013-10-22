namespace PhoneTicket.Web.ViewModels
{
    using System;

    using PhoneTicket.Web.Models;
    using PhoneTicket.Web.Helpers;

    public class ListShowViewModel
    {
        public int Id { get; set; }
        
        public string Time { get; set; }

        public string Complex { get; set; }

        public string Room { get; set; }

        public bool CanEdit { get; set; }

        public DateTime Date { get; set; }

        public bool IsAvailable { get; set; }

        public double Price { get; set; }

        public static ListShowViewModel FromShow(Show show, bool userCanEdit)
        {
            return new ListShowViewModel 
                        {
                           CanEdit = ((show.Date > DateTime.Now)&&(userCanEdit)),
                           Complex = show.Room != null && show.Room.Complex != null ? show.Room.Complex.Name : string.Empty,
                           Room = show.Room != null ? show.Room.Name : string.Empty,
                           Id = show.Id,
                           Date = show.Date,
                           IsAvailable = show.IsAvailable,
                           Time = show.Date.ToString("HH:mm"),
                           Price = show.Price,
                       };
        }
    }
}