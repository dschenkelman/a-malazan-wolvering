namespace PhoneTicket.Web.Models
{
    using System.Collections.Generic;
    using System.ComponentModel;
    using System.ComponentModel.DataAnnotations;
    using System.ComponentModel.DataAnnotations.Schema;

    using PhoneTicket.Web.Properties;

    public class Movie
    {
        public int Id { get; set; }

        [Required(ErrorMessageResourceName = "RequiredField", ErrorMessageResourceType = typeof(Resources), ErrorMessage = null)]
        [DisplayName("Título")]
        public string Title { get; set; }

        [Required(ErrorMessageResourceName = "RequiredField", ErrorMessageResourceType = typeof(Resources), ErrorMessage = null)]
        [DisplayName("Sinopsis")]
        public string Synopsis { get; set; }

        [Required(ErrorMessageResourceName = "RequiredField", ErrorMessageResourceType = typeof(Resources), ErrorMessage = null)]
        [Url(ErrorMessageResourceName = "UrlField", ErrorMessageResourceType = typeof(Resources), ErrorMessage = null)]
        [DisplayName("Url de imágen")]
        public string ImageUrl { get; set; }

        [Required(ErrorMessageResourceName = "RequiredField", ErrorMessageResourceType = typeof(Resources), ErrorMessage = null)]
        [Url(ErrorMessageResourceName = "UrlField", ErrorMessageResourceType = typeof(Resources), ErrorMessage = null)]
        [DisplayName("Url de trailer")]
        public string TrailerUrl { get; set; }

        [Required(ErrorMessageResourceName = "RequiredField", ErrorMessageResourceType = typeof(Resources), ErrorMessage = null)]
        [Range(1, int.MaxValue, ErrorMessage = "Ingrese un número entero de minutos mayor a uno.")]
        [DisplayName("Duración (en minutos)")]
        public int DurationInMinutes { get; set; }

        [ForeignKey("Genre")]
        public int GenreId { get; set; }

        [DisplayName("Género")]
        public virtual Genre Genre { get; set; }

        [ForeignKey("Rating")]
        public int RatingId { get; set; }

        [DisplayName("Clasificación")]
        public virtual Rating Rating { get; set; }

        public virtual ICollection<Show> Shows { get; set; }
    }
}