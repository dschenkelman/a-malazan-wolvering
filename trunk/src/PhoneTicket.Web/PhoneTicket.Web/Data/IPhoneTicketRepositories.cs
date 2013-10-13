namespace PhoneTicket.Web.Data
{
    using System;

    using PhoneTicket.Web.Models;

    public interface IPhoneTicketRepositories : IDisposable
    {
        IRepository<User> Users { get; }

        IRepository<Genre> Genres { get; }

        IRepository<Movie> Movies { get; }

        IRepository<Rating> Ratings { get; }

        IRepository<TemporaryUser> TemporaryUsers { get; }

        IRepository<Complex> Complexes { get; }

        IRepository<Room> Rooms { get; }

        IRepository<RoomType> RoomTypes { get; }

        IRepository<Show> Shows { get; }
    }
}

