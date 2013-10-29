namespace PhoneTicket.Web.ViewModels.Api
{
    using PhoneTicket.Web.Models;

    public class DiscountForOperationViewModel
    {
        public int DiscountId { get; set; }

        public int Count { get; set; }

        public static DiscountForOperationViewModel FromOperationDiscount(OperationDiscount discount)
        {
            return new DiscountForOperationViewModel { DiscountId = discount.DiscountId, Count = discount.Count };
        }
    }
}