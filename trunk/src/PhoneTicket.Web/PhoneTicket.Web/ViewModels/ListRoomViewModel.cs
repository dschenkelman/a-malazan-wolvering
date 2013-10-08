namespace PhoneTicket.Web.ViewModels
{
    using System;
    using System.Collections.Generic;
    using System.Linq;
    using System.Web;
    using System.Web.Mvc;
    using System.ComponentModel.DataAnnotations;
    using System.Threading.Tasks;
    using System.ComponentModel;

    using PhoneTicket.Web.Models;
    using PhoneTicket.Web.Services;
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

        public IEnumerable<SelectListItem> AvailableRoomTypes { get; set; }

        [Required(ErrorMessageResourceName = "RequiredField", ErrorMessageResourceType = typeof(Resources), ErrorMessage = null)]
        [Range(1, int.MaxValue, ErrorMessage = "Ingrese un número entero de minutos mayor a uno.")]
        public int Capacity { get; set; }

        public string TypeDescription { get; set; }

        [UIHint("DropDownList")]
        [Required(ErrorMessage = "Seleccione un Estilo de Sala")]
        public int? TypeId { get; set; }

        public static ListRoomViewModel FromRoom(Room room)
        {

            var vm = new ListRoomViewModel
            {
                Id = room.Id,
                Name = room.Name,
                ComplexName = room.Complex.Name,
                Capacity = room.Capacity,
                TypeDescription = room.Type.Description
            };

            return vm;
        }

        public static Room FromRoomViewModel(ListRoomViewModel roomVM)
        {
            var r = new Room
            {
                Name = roomVM.Name,
                ComplexId = Convert.ToInt32(roomVM.ComplexId),
                Capacity = roomVM.Capacity,
                TypeId = Convert.ToInt32(roomVM.TypeId)
            };

            return r;
        }
    }
}