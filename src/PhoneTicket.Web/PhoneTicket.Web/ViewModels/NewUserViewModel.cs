namespace PhoneTicket.Web.ViewModels
{
    using System;
    using System.Security.Cryptography;
    using System.Text;

    using PhoneTicket.Web.Models;

    public class NewUserViewModel
    {
        public int Id { get; set; }

        public string EmailAddress { get; set; }

        public string Password { get; set; }

        public string FirstName { get; set; }

        public string LastName { get; set; }

        public string CellPhoneNumber { get; set; }

        public DateTime? BirthDate { get; set; }

        public User ToUser()
        {
            var passwordHash = new SHA256CryptoServiceProvider().ComputeHash(Encoding.UTF8.GetBytes(this.Password));

            return new User
                       {
                           BirthDate = this.BirthDate, 
                           CellPhoneNumber = this.CellPhoneNumber, 
                           Id = this.Id,
                           FirstName = this.FirstName,
                           LastName = this.LastName, 
                           EmailAddress = this.EmailAddress,
                           PasswordHash = passwordHash
                       };
        }
    }
}