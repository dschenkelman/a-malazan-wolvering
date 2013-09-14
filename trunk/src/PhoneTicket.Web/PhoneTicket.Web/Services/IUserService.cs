namespace PhoneTicket.Web.Services
{
    using System.Collections.Generic;
    using System.Threading.Tasks;

    using PhoneTicket.Web.Models;

    public interface IUserService
    {
        Task<int> GetId(string email);

        Task<IEnumerable<User>> GetUsers();
    }
}