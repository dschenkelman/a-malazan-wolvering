namespace PhoneTicket.Web.ViewModels
{
    using System;
    using System.Collections.Generic;
    using System.ComponentModel.DataAnnotations;
    using System.Web.Mvc;

    using PhoneTicket.Web.Helpers;
    using PhoneTicket.Web.Models;
    using PhoneTicket.Web.Properties;

    public class CreateDiscountViewModel : IValidatableObject
    {
        public CreateDiscountViewModel()
        {
            this.DiscountTypes = new List<SelectListItem>();
        }

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

        public void PopulateDiscountTypes()
        {
            this.DiscountTypes.Add(
                new SelectListItem
                {
                    Selected = true,
                    Text = DiscountType.TwoForOne.InSpanish(),
                    Value = ((int)DiscountType.TwoForOne).ToString()
                });

            this.DiscountTypes.Add(
                new SelectListItem
                {
                    Selected = false,
                    Text = DiscountType.Percentage.InSpanish(),
                    Value = ((int)DiscountType.Percentage).ToString()
                });

            this.DiscountTypes.Add(
                new SelectListItem
                {
                    Selected = false,
                    Text = DiscountType.FixedPrice.InSpanish(),
                    Value = ((int)DiscountType.FixedPrice).ToString()
                });
        }

        public Discount ToDiscount()
        {
            var type = (DiscountType)this.Type;

            return new Discount
                    {
                        Description = this.Description,
                        EndDate = this.EndDate,
                        StartDate = this.StartDate,
                        Type = type,
                        Value =
                         this.Value.HasValue
                         ? (type == DiscountType.FixedPrice
                             ? this.Value.Value
                             : this.Value.Value / 100)
                         : (double?)null
                    };
        }
    }
}