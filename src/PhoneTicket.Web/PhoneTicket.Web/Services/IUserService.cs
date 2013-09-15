namespace PhoneTicket.Web.Services
{
    using System.Collections.Generic;
    using System.Threading.Tasks;

    using PhoneTicket.Web.Models;

    public interface IUserService
    {
        Task<int> GetIdAsync(string email);

        Task<IEnumerable<User>> GetUsersAsync();

        Task<User> GetUserAsync(int id);

        Task UpdateAsync(User user);
    }
}