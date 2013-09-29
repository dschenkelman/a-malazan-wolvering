namespace PhoneTicket.Web.Data
{
    using PhoneTicket.Web.Models;

    public class PhoneTicketRepositories : IPhoneTicketRepositories
    {
        private IRepository<User> users;

        private IRepository<Genre> genres;

        private IRepository<Movie> movies;

        private IRepository<Rating> ratings;

        private IRepository<TemporaryUser> temporaryUser;

        public PhoneTicketRepositories(
            IRepository<TemporaryUser> temporaryUser,
            IRepository<Rating> ratings,
            IRepository<Movie> movies,
            IRepository<Genre> genres,
            IRepository<User> users)
        {
            this.temporaryUser = temporaryUser;
            this.ratings = ratings;
            this.movies = movies;
            this.genres = genres;
            this.users = users;
        }

        public IRepository<User> Users
        {
            get
            {
                return this.users;
            }
        }

        public IRepository<Genre> Genres
        {
            get
            {
                return this.genres;
            }
        }

        public IRepository<Movie> Movies
        {
            get
            {
                return this.movies;
            }
        }

        public IRepository<Rating> Ratings
        {
            get
            {
                return this.ratings;
            }
        }

        public IRepository<TemporaryUser> TemporaryUser
        {
            get
            {
                return this.temporaryUser;
            }
        }
    }
}