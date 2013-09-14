using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Net.Mail;

namespace PhoneTicket.Web.Services
{
    public interface IEmailService
    {
        MailMessage CreateMessage(string subject, string body, params string[] toAddresses);
        
        Task SendAsync(MailMessage message);
    }
}
