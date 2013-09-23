using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace PhoneTicket.Web.ViewModels.Api
{
    public class MovieViewModel
    {

        public int Id { get; set; }

        public string Title { get; set; }

        public string Synopsis { get; set; }

        public string ImageUrl { get; set; }

        public string Rating { get; set; }

        public int DurationInMinutes { get; set; }

        public string Genre { get; set; }

        public string TrailerUrl { get; set; }

    }
}