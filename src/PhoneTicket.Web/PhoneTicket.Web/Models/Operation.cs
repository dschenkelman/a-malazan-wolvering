namespace PhoneTicket.Web.Models
{
    using System;
    using System.ComponentModel.DataAnnotations;
    using System.ComponentModel.DataAnnotations.Schema;

    public class Operation
    {
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        [Key]
        public int Number { get; set; }

        [ForeignKey("User")]
        public int UserId { get; set; }

        public virtual User User { get; set; }

        public DateTime Date { get; set; }

        [ForeignKey("Show")]
        public int ShowId { get; set; }

        public virtual Show Show { get; set; }

        public OperationType Type { get; set; }

        public string CreditCardNumber { get; set; }

        public string CreditCardSecurityCode { get; set; }

        public DateTime CreditCardExpirationDate { get; set; }

        [ForeignKey("CreditCardCompany")]
        public int CreditCardCompanyId { get; set; }

        public virtual CreditCardCompany CreditCardCompany { get; set; }

        [ForeignKey("Discount")]
        public int? DiscountId { get; set; }

        public virtual Discount Discount { get; set; }

        public int? DiscountCount { get; set; }
    }
}