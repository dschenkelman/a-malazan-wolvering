namespace PhoneTicket.Web.HttpModules
{
    using System.Linq;
    using System.Security.Cryptography;
    using System.Text;

    using PhoneTicket.Web.Data;

    public class PhoneTicketBasicAuthHttpModule : BasicAuthHttpModule
    {
        private readonly PhoneTicketContext db;

        public PhoneTicketBasicAuthHttpModule()
        {
            this.db = new PhoneTicketContext();
        }

        protected override bool CheckPassword(string emailAddress, string password)
        {
            var user = this.db.Users.FirstOrDefault(u => u.EmailAddress == emailAddress);

            if (user == null)
            {
                return false;
            }

            var hashString = Encoding.UTF8.GetString(new SHA256CryptoServiceProvider().ComputeHash(Encoding.UTF8.GetBytes(password)));

            return Encoding.UTF8.GetString(user.PasswordHash) == hashString;
        }

        public override void Dispose()
        {
            this.db.Dispose();
        }
    }
}