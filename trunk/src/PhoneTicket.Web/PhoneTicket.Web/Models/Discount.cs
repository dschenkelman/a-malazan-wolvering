namespace PhoneTicket.Web.Models
{
    using System;

    public class Discount
    {
        public int Id { get; set; }

        public string Description { get; set; }

        public DiscountType Type { get; set; }

        public double? Value { get; set; }

        public DateTime StartDate { get; set; }

        public DateTime EndDate { get; set; }
    }
}