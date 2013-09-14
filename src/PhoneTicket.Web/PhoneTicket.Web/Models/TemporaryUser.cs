namespace PhoneTicket.Web.Models
{
    using System;
    using System.ComponentModel.DataAnnotations;
    using System.ComponentModel.DataAnnotations.Schema;

    public class TemporaryUser
    {
        [ForeignKey("User"), Key, DatabaseGenerated(DatabaseGeneratedOption.None)]
        public int Id { get; set; }

        public virtual User User { get; set; }

        public Guid Secret { get; set; }

        public DateTime RegistrationDate { get; set; }
    }
}