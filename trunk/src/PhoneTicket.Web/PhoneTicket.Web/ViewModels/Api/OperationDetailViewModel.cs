namespace PhoneTicket.Web.ViewModels.Api
{
    using System;
    using System.Collections.Generic;
    using System.Linq;
    using System.Web;

    public class OperationDetailViewModel
    {
        public string MovieTitle { get; set; } 

        public string ShowDateAndTime { get; set; }

        public string ComplexAddress { get; set; }

        public double ShowPrice { get; set; }

        public List<ArmChairViewModel> Seats { get; set; }

        public List<OperationDiscountDetailViewModel> Discounts { get; set; }
    }
}