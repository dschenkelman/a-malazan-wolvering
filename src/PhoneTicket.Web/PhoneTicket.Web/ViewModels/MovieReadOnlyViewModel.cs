namespace PhoneTicket.Web.ViewModels
{
    using PhoneTicket.Web.Models;
    using System;
    using System.Collections.Generic;
    using System.ComponentModel;
    using System.Linq;
    using System.Web;

    public class MovieReadOnlyViewModel
    {
        [DisplayName("Título")]
        public string Title { get; set; }

        [DisplayName("Duración (en minutos)")]
        public int DurationInMinutes { get; set; }

        [DisplayName("Género")]
        public string Genre { get; set; }

        [DisplayName("Clasificación")]
        public string Rating { get; set; }

        [DisplayName("Sinopsis")]
        public string Synopsis { get; set; }

        [DisplayName("Url de trailer")]
        public string TrailerUrl { get; set; }

        [DisplayName("Url de imágen")]
        public string ImageUrl { get; set; }

        public static MovieReadOnlyViewModel FromMovie(Movie movie)
        {
            return new MovieReadOnlyViewModel
            {
                Title = movie.Title,
                DurationInMinutes = movie.DurationInMinutes,
                Genre = movie.Genre.Name,
                Rating = movie.Rating.Description,
                Synopsis = movie.Synopsis,
                TrailerUrl = movie.TrailerUrl,
                ImageUrl = movie.ImageUrl
            };

        }
    }
}