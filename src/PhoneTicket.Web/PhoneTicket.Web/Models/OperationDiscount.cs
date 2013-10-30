namespace PhoneTicket.Web.Models
{
    using System;
    using System.ComponentModel.DataAnnotations;
    using System.ComponentModel.DataAnnotations.Schema;

    public class OperationDiscount
    {
        public int Count { get; set; }

        [Key, ForeignKey("Operation"), Column(Order = 0)]
        public Guid OperationId { get; set; } 

        public Operation Operation { get; set; }

        [Key, ForeignKey("Discount"), Column(Order = 1)]
        public int DiscountId { get; set; }

        public Discount Discount { get; set; }
    }
}