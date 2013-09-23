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

        public static MovieViewModel ToMovieViewModel(this Movie movie)
        {
            return new MovieViewModel()
            {

                Id = movie.Id,
                Title = movie.Title,
                Synopsis = movie.Synopsis,
                ImageUrl = movie.ImageUrl,
                Rating = movie.Rating.Description,
                DurationInMinutes = movie.DurationInMinutes,
                Genre = movie.Genre.Name,
                TrailerUrl = movie.TrailerUrl

            };
        }
    }
}
