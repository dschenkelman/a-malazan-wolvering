namespace PhoneTicket.Web.Services
{
    using System;
    using System.Collections.Generic;
    using System.Linq.Expressions;
    using System.Threading.Tasks;

    using PhoneTicket.Web.Models;

    public interface IUserService
    {
        Task<int> GetIdAsync(string email);

        Task<IEnumerable<User>> GetUsersAsync();

        Task<IEnumerable<User>> GetUsersAsync(Expression<Func<User, bool>> filter);

        Task<User> GetUserAsync(int id);

        Task<User> GetUserAsync(string email);

        Task UpdateAsync(User user);

        Task<bool> HasConflict(User user);
    }
}