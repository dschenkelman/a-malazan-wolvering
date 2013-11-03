namespace PhoneTicket.Web.Services
{
    using System;
    using System.Collections.Generic;
    using System.Threading.Tasks;

    using PhoneTicket.Web.Models;
    using PhoneTicket.Web.ViewModels;

    public interface IReportService
    {
        Task<ReportShowSeats> GetReportSeatsAsync(int showId);

        Task<IEnumerable<MovieSalesViewModel>> GetSalesPerMovieReport(DateTime beginDate, DateTime endDate);
    }
}