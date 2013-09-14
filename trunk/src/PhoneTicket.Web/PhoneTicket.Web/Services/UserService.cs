namespace PhoneTicket.Web.Services
{
    using System;
    using System.Collections.Generic;
    using System.Data.Entity;
    using System.Linq;
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

        public Task<int> GetId(string email)
        {
            return this.db.Users.Where(u => u.EmailAddress == email).Select(u => u.Id).FirstAsync();
        }

        public async Task<IEnumerable<User>> GetUsers()
        {
            return await this.db.Users.Where(u => !this.db.TemporaryUser.Any(tu => tu.Id == u.Id)).ToListAsync();
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