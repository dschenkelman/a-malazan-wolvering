namespace PhoneTicket.Web.ViewModels.Api
{
    using System;
    using System.Collections.Generic;
    using System.Linq;
    using System.Web;

    public class NewOperationViewModel
    {

        public int ShowId { get; set; }

        public List<ArmChairViewModel> ArmChairs { get; set; }

    }
}