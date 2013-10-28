namespace PhoneTicket.Web.ViewModels
{
    using System;
    using System.Collections.Generic;
    using System.ComponentModel.DataAnnotations;
    using System.Web.Mvc;

    using PhoneTicket.Web.Models;
    using PhoneTicket.Web.Properties;

    public class ListRoomViewModel
    {
        public int Id { get; set; }

        [Required(ErrorMessageResourceName = "RequiredField", ErrorMessageResourceType = typeof(Resources), ErrorMessage = null)]
        public string Name { get; set; }

        public string ComplexName { get; set; }

        [UIHint("DropDownList")]
        [Required(ErrorMessage = "Seleccione un Complejo")]
        public int? ComplexId { get; set; }

        public IEnumerable<SelectListItem> AvailableComplexes { get; set; }

        [Required(ErrorMessageResourceName = "RequiredField", ErrorMessageResourceType = typeof(Resources), ErrorMessage = null)]
        [Range(1, int.MaxValue, ErrorMessage = "Ingrese un número entero de minutos mayor a uno.")]
        public int Capacity { get; set; }

        public bool CanEdit { get; set; }

        public static ListRoomViewModel FromRoom(Room room, bool userCanEdit)
        {

            var vm = new ListRoomViewModel
            {
                Id = room.Id,
                Name = room.Name,
                ComplexName = room.Complex.Name,
                ComplexId = room.ComplexId,
                Capacity = room.Capacity,
                CanEdit = userCanEdit
            };

            return vm;
        }

        public static Room FromRoomViewModel(ListRoomViewModel roomVM)
        {
            var r = new Room
            {
                Id = roomVM.Id,
                Name = roomVM.Name,
                ComplexId = Convert.ToInt32(roomVM.ComplexId),
                Capacity = roomVM.Capacity,
            };

            return r;
        }
    }
}