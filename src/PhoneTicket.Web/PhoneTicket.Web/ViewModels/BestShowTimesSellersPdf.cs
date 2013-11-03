namespace PhoneTicket.Web.ViewModels
{
    using System;
    using System.Collections.Generic;
    using System.Linq;
    using System.Web;

    public class BestShowTimesSellersPdf
    {
        
        public string ChartRelativePath { get; set; }

        public string ComplexName { get; set; }

        public string FromDate { get; set; }

        public string ToDate { get; set; }

        public IEnumerable<ShowTimeTicketCountViewModel> ShowTimesInfo { get; set; }
    }
}