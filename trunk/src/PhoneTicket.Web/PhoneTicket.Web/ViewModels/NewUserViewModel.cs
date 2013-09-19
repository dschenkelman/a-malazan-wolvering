namespace PhoneTicket.Web.ViewModels
{
    using System;
    using System.Globalization;
    using System.Security.Cryptography;
    using System.Text;

    using PhoneTicket.Web.Models;

    public class NewUserViewModel
    {
        public int Dni { get; set; }

        public string EmailAddress { get; set; }

        public string Password { get; set; }

        public string FirstName { get; set; }

        public string LastName { get; set; }

        public string CellPhoneNumber { get; set; }

        public string BirthDate { get; set; }

        public User ToUser()
        {
            var passwordHash = new SHA256CryptoServiceProvider().ComputeHash(Encoding.UTF8.GetBytes(this.Password));
            DateTime parsed;
            bool result = DateTime.TryParseExact(this.BirthDate, "yyyy/MM/dd", null, DateTimeStyles.None, out parsed);

            return new User
                       {
                           BirthDate = result ? parsed : (DateTime?)null, 
                           CellPhoneNumber = this.CellPhoneNumber, 
                           Id = this.Dni,
                           FirstName = this.FirstName,
                           LastName = this.LastName, 
                           EmailAddress = this.EmailAddress,
                           PasswordHash = passwordHash,
                           IsValid = true,
                       };
        }
    }
}