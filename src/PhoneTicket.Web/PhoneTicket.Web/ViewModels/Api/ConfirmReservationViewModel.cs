namespace PhoneTicket.Web.ViewModels.Api
{
    using System;

    using Newtonsoft.Json;

    public class ConfirmReservationViewModel
    {
        public string CreditCardNumber { get; set; }

        public string CreditCardSecurityCode { get; set; }

        public string CreditCardExpiration { private get; set; }

        [JsonIgnore]
        public DateTime CreditCardExpirationDate
        {
            get
            {
                return DateTime.Parse(this.CreditCardExpiration);
            }
        }

        public int CreditCardCompanyId { get; set; }
    }
}