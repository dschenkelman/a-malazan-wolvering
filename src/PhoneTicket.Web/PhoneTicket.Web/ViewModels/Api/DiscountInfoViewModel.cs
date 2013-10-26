namespace PhoneTicket.Web.ViewModels.Api
{
    using PhoneTicket.Web.Models;

    public class DiscountInfoViewModel
    {
        public int Id { get; set; }

        public string Description { get; set; }

        public DiscountType Type { get; set; }

        public double? Value { get; set; }

        public static DiscountInfoViewModel FromDiscount(Discount discount)
        {
            return new DiscountInfoViewModel 
                       {
                           Description = discount.Description,
                           Id = discount.Id,
                           Type = discount.Type,
                           Value = discount.Value
                       };
        }
    }
}