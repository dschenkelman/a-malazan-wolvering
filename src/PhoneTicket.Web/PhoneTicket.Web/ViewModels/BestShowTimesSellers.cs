namespace PhoneTicket.Web.ViewModels
{
    using System;
    using System.Collections.Generic;
    using System.Web.Mvc;

    using Newtonsoft.Json;

    using System.ComponentModel.DataAnnotations;

    using PhoneTicket.Web.Properties;

    public class BestShowTimesSellersViewModel : IValidatableObject
    {
        [Required(ErrorMessageResourceName = "RequiredField", ErrorMessageResourceType = typeof(Resources), ErrorMessage = null)]
        public string From { get; set; }

        [Required(ErrorMessageResourceName = "RequiredField", ErrorMessageResourceType = typeof(Resources), ErrorMessage = null)]
        public string To { get; set; }

        public int ComplexId { get; set; }

        public IEnumerable<SelectListItem> Complexes { get; set; }

        public string ComplexesNames { get; set; }

        [JsonIgnore]
        public IEnumerable<ShowTimeTicketCountViewModel> ShowTimesInfo { get; set; }

        [JsonIgnore]
        public string ChartPath { get; set; }

        [JsonIgnore]
        public DateTime FromDate
        {
            get
            {
                return DateTime.Parse(this.From);
            }
        }

        [JsonIgnore]
        public DateTime ToDate
        {
            get
            {
                return DateTime.Parse(this.To);
            }
        }

        public IEnumerable<ValidationResult> Validate(ValidationContext validationContext)
        {
            var results = new List<ValidationResult>();

            if (this.ToDate < this.FromDate)
            {
                results.Add(new ValidationResult("La fecha de fin del reporte no puede ser anterior a la de comienzo.", new[] { "To", "From" }));
            }

            return results;
        }
    }
}