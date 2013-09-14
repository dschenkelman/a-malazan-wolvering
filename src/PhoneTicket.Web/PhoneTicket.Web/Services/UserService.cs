namespace PhoneTicket.Web.Services
{
    using System;
    using System.Data.Entity;
    using System.Linq;
    using System.Threading.Tasks;

    using PhoneTicket.Web.Data;

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