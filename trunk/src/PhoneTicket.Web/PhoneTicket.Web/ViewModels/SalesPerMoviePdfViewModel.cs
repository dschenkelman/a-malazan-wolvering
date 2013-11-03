namespace PhoneTicket.Web.ViewModels
{
    using System.Collections.Generic;

    public class SalesPerMoviePdfViewModel
    {
        public string ChartRelativePath { get; set; }

        public IEnumerable<MovieSalesViewModel> MovieSales { get; set; }
    }
}