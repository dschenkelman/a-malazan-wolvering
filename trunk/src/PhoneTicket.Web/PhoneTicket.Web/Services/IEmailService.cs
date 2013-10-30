namespace PhoneTicket.Web.Services
{
    using System.Net.Mail;
    using System.Threading.Tasks;

    public interface IEmailService
    {
        MailMessage CreateMessage(string subject, string body, params string[] toAddresses);
        
        Task SendAsync(MailMessage message);
    }
}
