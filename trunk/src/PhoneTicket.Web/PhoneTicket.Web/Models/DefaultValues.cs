﻿namespace PhoneTicket.Web.Models
{
    using System.ComponentModel.DataAnnotations;

    public class Setting
    {
        [Key]
        public string Key { get; set; }

        public string Value { get; set; }
    }
}