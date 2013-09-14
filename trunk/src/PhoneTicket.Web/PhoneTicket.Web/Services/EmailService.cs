namespace PhoneTicket.Web.Services
{
    using System;
    using System.Net;
    using System.Net.Mail;
    using System.Threading.Tasks;

    public class EmailService : IEmailService
    {
        private readonly string fromAddress;
        private readonly string password;

        public EmailService(string host, int port, string fromAddress, string password, bool useDefaultCredentials, bool enableSsl)
        {
            this.fromAddress = fromAddress;
            this.password = password;

            this.Client = new SmtpClient(host, port);

            this.Client.UseDefaultCredentials = useDefaultCredentials;
            var credentials = new NetworkCredential(this.fromAddress, this.password, string.Empty);
            this.Client.Credentials = credentials;

            this.Client.EnableSsl = enableSsl;
        }

        public string FromAddress
        {
            get
            {
                return this.fromAddress;
            }
        }

        protected SmtpClient Client { get; private set; }

        public MailMessage CreateMessage(string subject, string body, params string[] toAddresses)
        {
            MailMessage message = null;

            try
            {
                message = new MailMessage { From = new MailAddress(this.fromAddress) };

                foreach (var address in toAddresses)
                {
                    message.To.Add(address);
                }

                message.Body = body;

                message.Subject = subject;

                message.IsBodyHtml = true;

                message.SubjectEncoding = System.Text.Encoding.UTF8;

                message.BodyEncoding = System.Text.Encoding.UTF8;

            }

            catch (Exception)
            {
                if (message != null)
                {
                    message.Dispose();
                }
            }

            return message;
        }

        public Task SendAsync(MailMessage message)
        {
            return this.Client.SendMailAsync(message);
        }
    }
}