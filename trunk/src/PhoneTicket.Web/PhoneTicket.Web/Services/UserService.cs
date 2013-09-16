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
        private PhoneTicketContext db;

        public UserService(PhoneTicketContext db)
        {
            this.db = db;
        }

        public Task<int> GetIdAsync(string email)
        {
            return this.db.Users.Where(u => u.EmailAddress == email).Select(u => u.Id).FirstAsync();
        }

        public Task<IEnumerable<User>> GetUsersAsync()
        {
            return this.GetUsersAsync(null);
        }

        public async Task<IEnumerable<User>> GetUsersAsync(Expression<Func<User, bool>> filter)
        {
            var confirmedUsers = this.db.Users.Where(u => !this.db.TemporaryUser.Any(tu => tu.Id == u.Id));

            var finalUsers = confirmedUsers;

            if (filter != null)
            {
                finalUsers = confirmedUsers.Where(filter);
            }

            return await finalUsers.ToListAsync();
        }

        public Task<User> GetUserAsync(int id)
        {
            return this.db.Users.FirstOrDefaultAsync(u => u.Id == id);
        }

        public Task UpdateAsync(User user)
        {
            return this.db.SaveChangesAsync();
        }

        public Task<bool> HasConflict(User user)
        {
            return this.db.Users.AnyAsync(u => u.Id == user.Id || u.EmailAddress == user.EmailAddress);
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
                if (this.db != null)
                {
                    this.db.Dispose();
                    this.db = null;
                }
            }
        }
    }
}