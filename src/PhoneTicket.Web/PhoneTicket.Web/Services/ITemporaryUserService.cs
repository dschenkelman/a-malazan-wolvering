namespace PhoneTicket.Web.Services
{
    using System;
    using System.Threading.Tasks;

    using PhoneTicket.Web.ViewModels;

    public interface ITemporaryUserService
    {
        Task<Guid> CreateUser(NewUserViewModel user);

        Task<bool> IsSecretValid(int userId, Guid secret);

        Task ConfirmUser(int userId);
    }
}