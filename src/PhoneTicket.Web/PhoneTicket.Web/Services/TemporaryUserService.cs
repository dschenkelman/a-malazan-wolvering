namespace PhoneTicket.Web.Services
{
    using System;
    using System.Threading.Tasks;

    using PhoneTicket.Web.Data;
    using PhoneTicket.Web.Models;
    using PhoneTicket.Web.ViewModels;

    public class TemporaryUserService : ITemporaryUserService, IDisposable
    {
        private PhoneTicketContext db;

        public TemporaryUserService(PhoneTicketContext db)
        {
            this.db = db;
        }

        public async Task<Guid> CreateUser(NewUserViewModel user)
        {
            this.db.Users.Add(user.ToUser());

            var secret = Guid.NewGuid();

            this.db.TemporaryUser.Add(
                new TemporaryUser { Id = user.Dni, Secret = secret, RegistrationDate = DateTime.Now });

            await this.db.SaveChangesAsync();

            return secret;
        }

        public async Task<bool> IsSecretValid(int userId, Guid secret)
        {
            var user = await this.db.TemporaryUser.FindAsync(userId);

            return user != null && user.Secret == secret;
        }

        public async Task ConfirmUser(int userId)
        {
            var user = await this.db.TemporaryUser.FindAsync(userId);
            this.db.TemporaryUser.Remove(user);
            await this.db.SaveChangesAsync();
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