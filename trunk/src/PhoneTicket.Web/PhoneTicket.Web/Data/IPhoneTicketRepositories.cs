namespace PhoneTicket.Web.Data
{
    using PhoneTicket.Web.Models;

    public interface IPhoneTicketRepositories
    {
        IRepository<User> Users { get; }

        IRepository<Genre> Genres { get; }

        IRepository<Movie> Movies { get; }

        IRepository<Rating> Ratings { get; }

        IRepository<TemporaryUser> TemporaryUser { get; }
    }
}
