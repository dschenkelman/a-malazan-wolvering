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

        IRepository<Show> Shows { get; }

        IRepository<Operation> Operations { get; }

        IRepository<Discount> Discounts { get; }

        IRepository<OccupiedSeat> OccupiedSeats { get; }

        IRepository<OperationDiscount> OperationDiscounts { get; }

        IRepository<CreditCardCompany> CreditCardCompanies { get; }
    }
}

