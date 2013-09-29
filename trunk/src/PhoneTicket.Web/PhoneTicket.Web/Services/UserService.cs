namespace PhoneTicket.Web.Services
{
    using System;
    using System.Collections.Generic;
    using System.Data.Entity;
    using System.Linq;
    using System.Linq.Expressions;
    using System.Threading.Tasks;

    using PhoneTicket.Web.Data;
    using PhoneTicket.Web.Models;

    public class UserService : IUserService, IDisposable
    {
        private IPhoneTicketRepositories repositories;

        public UserService(IPhoneTicketRepositories repositories)
        {
            this.repositories = repositories;
        }

        public Task<int> GetIdAsync(string email)
        {
            return this.repositories.Users.Filter(u => u.EmailAddress == email).Select(u => u.Id).FirstAsync();
        }

        public Task<IEnumerable<User>> GetUsersAsync()
        {
            return this.GetUsersAsync(null);
        }

        public async Task<IEnumerable<User>> GetUsersAsync(Expression<Func<User, bool>> filter)
        {
            var tempUsers = await this.repositories.TemporaryUsers.AllAsync();
            
            var allUsers = await this.repositories.Users.AllAsync();

            var finalUsers = allUsers.Where(u => tempUsers.All(tu => tu.Id != u.Id));

            if (filter != null)
            {
                finalUsers = finalUsers.Where(filter.Compile());
            }

            return finalUsers;
        }

        public Task<User> GetUserAsync(int id)
        {
            return this.repositories.Users.GetByKeyValuesAsync(id);
        }

        public Task UpdateAsync(User user)
        {
            return this.repositories.Users.SaveAsync();
        }

        public async Task<bool> HasConflict(User user)
        {
            return await this.repositories.Users
                .Filter(u => u.Id == user.Id || u.EmailAddress == user.EmailAddress)
                .CountAsync() != 0;
        }

        public void Dispose()
        {
            this.Dispose(true);
            GC.SuppressFinalize(this);
        }

        protected virtual void Dispose(bool disposing)
        {
            if (disposing)
            {
                if (this.repositories != null)
                {
                    this.repositories.Dispose();
                    this.repositories = null;
                }
            }
        }

        private class UserComparer : IEqualityComparer<User>
        {
            public bool Equals(User x, User y)
            {
                return x.Id == y.Id;
            }

            public int GetHashCode(User obj)
            {
                return obj.GetHashCode();
            }
        }
    }
}