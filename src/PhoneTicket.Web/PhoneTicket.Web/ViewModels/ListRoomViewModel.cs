namespace PhoneTicket.Web.ViewModels
{
    using System;
    using System.Collections.Generic;
    using System.ComponentModel.DataAnnotations;
    using System.Web;
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
        
        public bool CanEdit { get; set; }

        public int Capacity { get; set; }

        [Required]
        public HttpPostedFileBase RoomFile { get; set; }

        public string RoomFileParsed { get; set; }

        public static ListRoomViewModel FromRoom(Room room, bool userCanEdit)
        {
            var vm = new ListRoomViewModel
            {
                Id = room.Id,
                Name = room.Name,
                ComplexName = room.Complex.Name,
                ComplexId = room.ComplexId,
                CanEdit = userCanEdit,
                Capacity = room.Capacity,
            };

            return vm;
        }

        public static Room FromRoomViewModel(ListRoomViewModel roomViewModel)
        {
            var r = new Room
            {
                Id = roomViewModel.Id,
                Name = roomViewModel.Name,
                ComplexId = Convert.ToInt32(roomViewModel.ComplexId),
            };

            return r;
        }
    }
}