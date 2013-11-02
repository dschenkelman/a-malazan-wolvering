namespace PhoneTicket.Web.Controllers
{
    using System.Threading.Tasks;
    using System.Web.Mvc;

    using PhoneTicket.Web.Handlers;
    using PhoneTicket.Web.Services;
    using PhoneTicket.Web.ViewModels;

    [Authorize]
    [RequireSsl]
    public class ReportsController : BaseReportsController
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
            var viewModel = await this.GetReportForShow(showId);

            return this.View(viewModel);
        }

        public async Task<ActionResult> ForShowPdf(int showId)
        {
            var viewModel = await this.GetReportForShow(showId);

            return this.ViewPdf(viewModel);
        }

        private async Task<ShowSeatMapViewModel> GetReportForShow(int showId)
        {
            var reportSeats = await this.reportService.GetReportSeatsAsync(showId);

            var show = await this.showService.GetAsync(showId);

            var viewModel = new ShowSeatMapViewModel
            {
                ShowId = showId,
                Complex = show.Room.Complex.Name,
                Movie = show.Movie.Title,
                Show = show.Date.ToString("yyyy-MM-dd HH:mm"),
                ReportShowSeats = reportSeats
            };

            return viewModel;
        }

    }
}