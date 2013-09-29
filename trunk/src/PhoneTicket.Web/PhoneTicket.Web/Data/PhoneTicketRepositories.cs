namespace PhoneTicket.Web.Data
{
    using System;

    using PhoneTicket.Web.Models;

    public class PhoneTicketRepositories : IPhoneTicketRepositories
    {
        private IRepository<User> users;

        private IRepository<Genre> genres;

        private IRepository<Movie> movies;

        private IRepository<Rating> ratings;

        private IRepository<TemporaryUser> temporaryUsers;

        public PhoneTicketRepositories(
            IRepository<TemporaryUser> temporaryUsers,
            IRepository<Rating> ratings,
            IRepository<Movie> movies,
            IRepository<Genre> genres,
            IRepository<User> users)
        {
            this.temporaryUsers = temporaryUsers;
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

        public IRepository<TemporaryUser> TemporaryUsers
        {
            get
            {
                return this.temporaryUsers;
            }
        }

        public void Dispose()
        {
            this.Dispose(true);
            GC.SuppressFinalize(this);
        }

        protected virtual void Dispose(bool disposing)
        {
            if (!disposing)
            {
                return;
            }

            this.TemporaryUsers.Dispose();
            this.Genres.Dispose();
            this.Movies.Dispose();
            this.Ratings.Dispose();
            this.Users.Dispose();
        }
    }
}