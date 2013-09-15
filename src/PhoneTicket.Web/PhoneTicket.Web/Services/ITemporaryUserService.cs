namespace PhoneTicket.Web.Services
{
    using System;
    using System.Threading.Tasks;

    using PhoneTicket.Web.Models;

    public interface ITemporaryUserService
    {
        Task<Guid> CreateUser(User user);

        Task<bool> IsSecretValid(int userId, Guid secret);

        Task ConfirmUser(int userId);
    }
}