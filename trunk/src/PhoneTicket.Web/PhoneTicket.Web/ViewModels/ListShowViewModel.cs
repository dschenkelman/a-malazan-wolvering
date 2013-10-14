﻿namespace PhoneTicket.Web.ViewModels
{
    using System;

    using PhoneTicket.Web.Models;

    public class ListShowViewModel
    {
        public int Id { get; set; }
        
        public string Time { get; set; }

        public string Complex { get; set; }

        public string Room { get; set; }

        public bool CanEdit { get; set; }

        public DateTime Date { get; set; }

        public bool IsAvailable { get; set; }

        public static ListShowViewModel FromShow(Show show)
        {
            return new ListShowViewModel()
                       {
                           CanEdit = show.Date > DateTime.Now,
                           Complex = show.Room.Complex.Name,
                           Room = show.Room.Name,
                           Id = show.Id,
                           Date = show.Date,
                           IsAvailable = show.IsAvailable,
                           Time = show.Date.ToString("hh:mm"),
                       };
        }
    }
}