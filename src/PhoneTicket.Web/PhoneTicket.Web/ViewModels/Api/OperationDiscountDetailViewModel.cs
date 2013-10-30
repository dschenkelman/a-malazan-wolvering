namespace PhoneTicket.Web.ViewModels.Api
{
    using PhoneTicket.Web.Models;
    using System;
    using System.Collections.Generic;
    using System.Linq;
    using System.Web;

    public class OperationDiscountDetailViewModel
    {

        public int DiscountId { get; set; }

        public string Description { get; set; }

        public DiscountType Type { get; set; }

        public double? Value { get; set; }

        public int Count { get; set; }

        public static OperationDiscountDetailViewModel FromOperationDiscount(OperationDiscount discountOperation)
        {
            return new OperationDiscountDetailViewModel
            {
                DiscountId = discountOperation.Discount.Id,
                Description = discountOperation.Discount.Description,
                Type = discountOperation.Discount.Type,
                Value = discountOperation.Discount.Value,
                Count = discountOperation.Count
            };
        }
    }
}