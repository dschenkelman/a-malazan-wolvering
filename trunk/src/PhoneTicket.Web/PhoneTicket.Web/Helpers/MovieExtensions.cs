namespace PhoneTicket.Web.Helpers
{
    
    using System;
    using System.Collections.Generic;
    using System.Linq;
    using System.Web;

    using PhoneTicket.Web.ViewModels.Api;
    using PhoneTicket.Web.Models;

    public static class MovieExtensions
    {
        public static MovieListItemViewModel ToListItemViewModel(this Movie movie)
        {
            return new MovieListItemViewModel()
            {

                Id = movie.Id,
                Title = movie.Title,
                ImageUrl = movie.ImageUrl

            };
        }
    }
}