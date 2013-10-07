namespace PhoneTicket.Web.ViewModels
{
    using System;
    using System.Collections.Generic;
    using System.Linq;
    using System.Web;

    using PhoneTicket.Web.Models;

    public class ListRoomViewModel
    {
        public int Id { get; set; }

        public string Name { get; set; }

        public string ComplexName { get; set; }

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
    }
}