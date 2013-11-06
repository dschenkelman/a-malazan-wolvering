namespace PhoneTicket.Web.ViewModels
{
    using System;
    using System.Collections.Generic;
    using System.Linq;
    using System.Web;

    public class ShowTimeTicketCountViewModel
    {
        public string Time { get; set; }

        public int TicketCount { get; set; }

        public int MovieCount { get; set; }

        public string Complexes { get; set; }
    }
}