namespace PhoneTicket.Web.ViewModels
{
    using System;
    using System.Collections.Generic;
    using System.ComponentModel.DataAnnotations;
    using System.Web.Mvc;

    using PhoneTicket.Web.Helpers;
    using PhoneTicket.Web.Models;
    using PhoneTicket.Web.Properties;

    public class DiscountViewModel : IValidatableObject
    {
        public DiscountViewModel()
        {
            this.DiscountTypes = new List<SelectListItem>();
        }

        public int Id { get; set; }

        [Required(ErrorMessageResourceName = "RequiredField", ErrorMessageResourceType = typeof(Resources), ErrorMessage = null)]
        public string Description { get; set; }

        public List<SelectListItem> DiscountTypes { get; set; }

        public int Type { get; set; }

        [Range(0, double.MaxValue, ErrorMessage = null, ErrorMessageResourceName = "GreaterThanZero", ErrorMessageResourceType = typeof(Resources))]
        public double? Value { get; set; }

        [Required(ErrorMessageResourceName = "RequiredField", ErrorMessageResourceType = typeof(Resources), ErrorMessage = null)]
        [DisplayFormat(ApplyFormatInEditMode = true, DataFormatString = "{0:yyyy-MM-dd}")]
        public DateTime StartDate { get; set; }

        [Required(ErrorMessageResourceName = "RequiredField", ErrorMessageResourceType = typeof(Resources), ErrorMessage = null)]
        [DisplayFormat(ApplyFormatInEditMode = true, DataFormatString = "{0:yyyy-MM-dd}")]
        public DateTime EndDate { get; set; }

        public IEnumerable<ValidationResult> Validate(ValidationContext validationContext)
        {
            var results = new List<ValidationResult>();

            var type = (DiscountType)this.Type;

            if (type == DiscountType.Percentage && this.Value > 100)
            {
                results.Add(new ValidationResult("El porcentaje de descuento debe ser menor o igual a 100.", new[] { "Value" }));
            }

            if (type != DiscountType.TwoForOne && (!this.Value.HasValue || this.Value.Value == 0))
            {
                results.Add(new ValidationResult("El valor debe ser mayor a 0", new[] { "Value" }));
            }

            if (this.EndDate < this.StartDate)
            {
                results.Add(new ValidationResult("La fecha de fin de la promoción no puede ser anterior a la de comienzo.", new[] { "StartDate", "EndDate" }));
            }

            return results;
        }

        public void PopulateDiscountTypes(DiscountType selected = DiscountType.TwoForOne)
        {
            var values = new[] { DiscountType.TwoForOne, DiscountType.Percentage, DiscountType.FixedPrice };

            foreach (var discountType in values)
            {
                this.DiscountTypes.Add(
                new SelectListItem
                {
                    Selected = selected == discountType,
                    Text = discountType.InSpanish(),
                    Value = ((int)discountType).ToString(),
                });
            }
        }

        public Discount ToDiscount()
        {
            var discount = new Discount();

            discount.UpdateFrom(this);

            return discount;
        }

        public static DiscountViewModel FromDiscount(Discount discount)
        {
            return new DiscountViewModel 
                        {
                           Description = discount.Description,
                           EndDate = discount.EndDate,
                           StartDate = discount.StartDate,
                           Id = discount.Id,
                           Type = (int)discount.Type,
                           Value = discount.Value.HasValue
                                 ? (discount.Type == DiscountType.FixedPrice
                                     ? discount.Value.Value
                                     : discount.Value.Value * 100)
                                 : (double?)null
                       };
        }
    }
}