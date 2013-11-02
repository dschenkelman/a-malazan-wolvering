namespace PhoneTicket.Web.Services
{
    using System.Threading.Tasks;

    using PhoneTicket.Web.Models;

    public interface IReportService
    {
        Task<ReportShowSeats> GetReportSeatsAsync(int showId);
    }
}