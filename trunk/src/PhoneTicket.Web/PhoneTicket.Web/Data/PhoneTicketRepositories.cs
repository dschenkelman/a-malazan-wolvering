namespace PhoneTicket.Web.Data
{
    using System;

    using PhoneTicket.Web.Models;

    public class PhoneTicketRepositories : IPhoneTicketRepositories
    {
        private readonly IRepository<User> users;

        private readonly IRepository<Genre> genres;

        private readonly IRepository<Movie> movies;

        private readonly IRepository<Rating> ratings;

        private readonly IRepository<TemporaryUser> temporaryUsers;

        private readonly IRepository<Complex> complexes;

        private readonly IRepository<Room> rooms;

        public PhoneTicketRepositories(
            IRepository<TemporaryUser> temporaryUsers,
            IRepository<Rating> ratings,
            IRepository<Movie> movies,
            IRepository<Genre> genres,
            IRepository<User> users,
            IRepository<Complex> complexes, 
            IRepository<Room> rooms)
        {
            this.temporaryUsers = temporaryUsers;
            this.ratings = ratings;
            this.movies = movies;
            this.genres = genres;
            this.users = users;
            this.complexes = complexes;
            this.rooms = rooms;
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

        public IRepository<Complex> Complexes
        {
            get
            {
                return this.complexes;
            }
        }

        public IRepository<Room> Rooms
        {
            get
            {
                return this.rooms;
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