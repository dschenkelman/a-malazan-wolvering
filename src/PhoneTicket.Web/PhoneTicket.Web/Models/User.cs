namespace PhoneTicket.Web.Models
{
    using System;
    using System.ComponentModel.DataAnnotations.Schema;

    public class User
    {
        [DatabaseGenerated(DatabaseGeneratedOption.None)]
        public int Id { get; set; }
        
        public string EmailAddress { get; set; }

        public byte[] PasswordHash { get; set; }

        public string FirstName { get; set; }

        public string LastName { get; set; }

        public string CellPhoneNumber { get; set; }

        public DateTime? BirthDate { get; set; }

        public bool IsValid { get; set; }
    }
}