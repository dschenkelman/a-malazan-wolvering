namespace PhoneTicket.Web.ViewModels
{
    using System;

    using PhoneTicket.Web.Helpers;
    using PhoneTicket.Web.Models;

    public class ListDiscountViewModel
    {
        public int Id { get; set; }

        public string Description { get; set; }

        public string Type { get; set; }

        public string Value { get; set; }

        public string StartDate { get; set; }

        public string EndDate { get; set; }

        public bool CanEdit { get; set; }

        public static ListDiscountViewModel FromDiscount(Discount discount, bool isAdmin)
        {
            var currentDate = DateTimeHelpers.DateTimeInArgentina;

            return new ListDiscountViewModel 
                        {
                           Id = discount.Id,
                           Description = discount.Description,
                           EndDate = discount.EndDate.ToString("yyyy-MM-dd"),
                           StartDate = discount.StartDate.ToString("yyyy-MM-dd"),
                           Type = discount.Type.InSpanish(),
                           Value = discount.Type == DiscountType.TwoForOne ? string.Empty : discount.Type == DiscountType.FixedPrice ? "$" + discount.Value :  discount.Value * 100 + "%",
                           CanEdit = isAdmin && currentDate < discount.StartDate
                        };
        }
    }
}