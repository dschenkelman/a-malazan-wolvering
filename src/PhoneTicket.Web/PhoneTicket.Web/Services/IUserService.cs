namespace PhoneTicket.Web.Services
{
    using System.Threading.Tasks;

    public interface IUserService
    {
        Task<int> GetId(string email);
    }
}