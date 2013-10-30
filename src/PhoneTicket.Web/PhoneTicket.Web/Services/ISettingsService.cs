namespace PhoneTicket.Web.Services
{
    using System.Threading.Tasks;

    public interface ISettingsService
    {
        Task<string> GetAsync(string key);
    }
}