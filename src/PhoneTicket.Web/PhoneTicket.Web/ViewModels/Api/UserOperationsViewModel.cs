namespace PhoneTicket.Web.ViewModels.Api
{
    using System;

    public class UserOperationsViewModel
    {
        public Guid Id { get; set; }
        
        public bool IsBought { get; set; }

        public string MovieTitle { get; set; }

        public string ShowDateAndTime { get; set; }

        public string ComplexAddress { get; set; }
    }
}