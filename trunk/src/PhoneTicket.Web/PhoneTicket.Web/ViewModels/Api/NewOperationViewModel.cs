namespace PhoneTicket.Web.ViewModels.Api
{
    using System;
    using System.Collections.Generic;

    using PhoneTicket.Web.Helpers;
    using PhoneTicket.Web.Models;

    public class NewOperationViewModel
    {
        public int ShowId { get; set; }

        public List<ArmChairViewModel> ArmChairs { get; set; }

        public string CreditCardNumber { get; set; }

        public string CreditCardSecurityCode { get; set; }

        public DateTime? CreditCardExpirationDate { get; set; }

        public int? CreditCardCompanyId { get; set; }

        public Operation ToOperation(int userId, OperationType type)
        {
            var operation = new Operation
            {
                UserId = userId,
                Date = DateTimeHelpers.DateTimeInArgentina,
                ShowId = this.ShowId,
                Type = type,
            };

            if (type != OperationType.Reservation)
            {
                operation.CreditCardNumber = this.CreditCardNumber;
                operation.CreditCardSecurityCode = this.CreditCardSecurityCode;
                operation.CreditCardExpirationDate = this.CreditCardExpirationDate;
                operation.CreditCardCompanyId = this.CreditCardCompanyId;
            }

            return operation;
        }
    }
}