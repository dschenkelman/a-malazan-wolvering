namespace PhoneTicket.Web.Controllers
{
    using System.Threading.Tasks;
    using System.Web.Mvc;

    using PhoneTicket.Web.Services;
    using PhoneTicket.Web.ViewModels;

    public class ReportsController : Controller
    {
        private readonly IReportService reportService;

        private readonly IShowService showService;

        public ReportsController(IReportService reportService, IShowService showService)
        {
            this.reportService = reportService;
            this.showService = showService;
        }

        public async Task<ActionResult> ForShow(int showId)
        {
            var reportSeats = await this.reportService.GetReportSeatsAsync(showId);

            var show = await showService.GetAsync(showId);

            return
                this.View(
                    new ShowSeatMapViewModel
                        {
                            Complex = show.Room.Complex.Name,
                            Movie = show.Movie.Title,
                            Show = show.Date.ToString("yyyy-MM-dd HH:mm"),
                            ReportShowSeats = reportSeats
                        });
        }
    }
}