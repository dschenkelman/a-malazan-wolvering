namespace PhoneTicket.Web.Services
{
    using System.Threading.Tasks;

    using PhoneTicket.Web.Data;

    public class SettingsService : ISettingsService
    {
        private readonly IPhoneTicketRepositories repositories;

        public SettingsService(IPhoneTicketRepositories repositories)
        {
            this.repositories = repositories;
        }

        public async Task<string> GetAsync(string key)
        {
            var entry = await this.repositories.Settings.GetByKeyValuesAsync(key);

            return entry.Value;
        }
    }
}