namespace PhoneTicket.Web.Services
{
    using System;
    using System.Threading.Tasks;

    using PhoneTicket.Web.Data;
    using PhoneTicket.Web.Models;

    public class TemporaryUserService : ITemporaryUserService, IDisposable
    {
        private IPhoneTicketRepositories repositories;

        public TemporaryUserService(IPhoneTicketRepositories repositories)
        {
            this.repositories = repositories;
        }

        public async Task<Guid> CreateUserAsync(User user)
        {
            this.repositories.Users.Insert(user);

            await this.repositories.Users.SaveAsync();

            var secret = Guid.NewGuid();

            this.repositories.TemporaryUsers.Insert(
                new TemporaryUser { Id = user.Id, Secret = secret, RegistrationDate = DateTime.Now });

            await this.repositories.TemporaryUsers.SaveAsync();

            return secret;
        }

        public async Task<bool> IsSecretValid(int userId, Guid secret)
        {
            var user = await this.repositories.TemporaryUsers.GetByKeyValuesAsync(userId);

            return user != null && user.Secret == secret;
        }

        public async Task ConfirmUser(int userId)
        {
            await this.repositories.TemporaryUsers.DeleteAsync(userId);
            await this.repositories.TemporaryUsers.SaveAsync();
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
    }
}