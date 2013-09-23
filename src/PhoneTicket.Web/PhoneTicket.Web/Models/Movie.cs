namespace PhoneTicket.Web.Models
{
    using System.ComponentModel.DataAnnotations.Schema;

    public class Movie
    {
        public int Id { get; set; }

        public string Title { get; set; }
                
        public string Synopsis { get; set; }

        public string ImageUrl { get; set; }

        public string TrailerUrl { get; set; }

        public int DurationInMinutes { get; set; }

        [ForeignKey("Genre")]
        public int GenreId { get; set; }

        public virtual Genre Genre { get; set; }

        [ForeignKey("Rating")]
        public int RatingId { get; set; }

        public virtual Rating Rating { get; set; }
    }
}