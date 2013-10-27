namespace PhoneTicket.Web.Helpers
{
    using System;

    using PhoneTicket.Web.Models;

    public static class DiscountTypeExtensions
    {
        public static string InSpanish(this DiscountType discountType)
        {
            switch (discountType)
            {
                case DiscountType.TwoForOne:
                    return "2x1";
                case DiscountType.FixedPrice:
                    return "Precio Fijo";
                case DiscountType.Percentage:
                    return "Porcentaje";
                default:
                    throw new ArgumentOutOfRangeException("discountType");
            }
        }
    }
}