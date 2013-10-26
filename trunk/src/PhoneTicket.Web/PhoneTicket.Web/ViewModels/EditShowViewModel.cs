namespace PhoneTicket.Web.ViewModels
{
    using System;
    using System.Collections.Generic;
    using System.ComponentModel.DataAnnotations;
    using System.Linq;
    using System.Web;
    using System.Web.Mvc;

    using PhoneTicket.Web.Properties;
    using PhoneTicket.Web.Models;
    using System.Globalization;

    public class EditShowViewModel
    {
        public int Id { get; set; }

        [UIHint("DropDownList")]
        [Required(ErrorMessage = "Seleccione una película")]
        public int MovieId { get; set; }

        public IEnumerable<SelectListItem> AvailableMovies { get; set; }

        [Required(ErrorMessageResourceName = "RequiredField", ErrorMessageResourceType = typeof(Resources), ErrorMessage = null)]
        public string Date { get; set; }

        [Required(ErrorMessageResourceName = "RequiredField", ErrorMessageResourceType = typeof(Resources), ErrorMessage = null)]
        [Range(1, 24, ErrorMessage = "Ingrese una hora entre 1 y 24.")]
        public int Hour { get; set; }

        [Required(ErrorMessageResourceName = "RequiredField", ErrorMessageResourceType = typeof(Resources), ErrorMessage = null)]
        [Range(1, 59, ErrorMessage = "Ingrese los minutos entre 1 y 59.")]
        public int Minutes { get; set; }

        [UIHint("DropDownList")]
        [Required(ErrorMessage = "Seleccione una sala")]
        public int RoomId { get; set; }

        public IEnumerable<SelectListItem> AvailableRooms { get; set; }

        public bool IsAvailable { get; set; }

        [Required(ErrorMessageResourceName = "RequiredField", ErrorMessageResourceType = typeof(Resources), ErrorMessage = null)]
        [Range(1.0d, double.MaxValue, ErrorMessage = "Ingrese un precio con valor mayor a uno.")]
        public double Price { get; set; }

        public static EditShowViewModel FromShow(Show show)
        {
            var vm = new EditShowViewModel
            {
                Id = show.Id,
                Date = show.Date.ToString("yyyy-MM-dd"),
                Hour = Convert.ToInt32(show.Date.ToString("HH")),
                Minutes = Convert.ToInt32(show.Date.ToString("mm")),
                MovieId = show.MovieId,
                RoomId = show.RoomId,
                IsAvailable = show.IsAvailable,
                Price = show.Price
            };

            return vm;
        }

        public Show FromViewModel()
        {
            var auxDate = DateTime.ParseExact(this.Date, "yyyy-MM-dd", null);
            var updatedDate = auxDate.AddHours(this.Hour).AddMinutes(this.Minutes);

            return new Show
            {
                Id = this.Id,
                Date = updatedDate,
                MovieId = this.MovieId,
                RoomId = this.RoomId,
                Price = this.Price,
                IsAvailable = this.IsAvailable
            };
        }

    }
}