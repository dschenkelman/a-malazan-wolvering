namespace PhoneTicket.Web.ViewModels
{
    using PhoneTicket.Web.Helpers;
    using PhoneTicket.Web.Models;

    public class ListMovieViewModel
    {
        public int Id { get; set; }

        public string Title { get; set; }

        public string Genre { get; set; }

        public int DurationInMinutes { get; set; }

        public string Rating { get; set; }

        public string ImageUrl { get; set; }

        public bool CanEdit { get; set; }

        public static ListMovieViewModel FromMovie(Movie movie)
        {
            var vm = new ListMovieViewModel
            {
                Id = movie.Id,
                DurationInMinutes = movie.DurationInMinutes,
                Genre = movie.Genre.Name,
                Rating = movie.Rating.Description,
                Title = movie.Title,
                ImageUrl = movie.ImageUrl,
                CanEdit = CurrentUserRole.getInstance().userIsAdmin
            };

            return vm;
        }
    }
}