namespace PhoneTicket.Web.ViewModels
{
    using System;
    using System.Collections.Generic;
    using System.Linq;
    using System.Web;

    using PhoneTicket.Web.Models;
    using System.Threading.Tasks;
    using PhoneTicket.Web.Services;
    using System.Web.Mvc;

    public class ListRoomViewModel
    {
        public int Id { get; set; }

        public string Name { get; set; }

        public string ComplexName { get; set; }

        public IEnumerable<SelectListItem> AvailableComplexes { get; set; }
        public IEnumerable<SelectListItem> AvailableRoomTypes { get; set; }

        public int Capacity { get; set; }

        public string Type { get; set; }

        public static ListRoomViewModel FromRoom(Room room)
        {

            var vm = new ListRoomViewModel
            {
                Id = room.Id,
                Name = room.Name,
                ComplexName = room.Complex.Name,
                Capacity = room.Capacity,
                Type = room.Type.Description
            };

            return vm;
        }

        public static Room FromRoomViewModel(ListRoomViewModel roomVM)
        {
            var r = new Room
            {
                Name = roomVM.Name,
                ComplexId = Convert.ToInt32(roomVM.ComplexName),
                Capacity = roomVM.Capacity,
                TypeId = Convert.ToInt32(roomVM.Type)
            };

            return r;
        }
    }
}