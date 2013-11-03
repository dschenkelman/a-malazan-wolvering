namespace PhoneTicket.Web.Controllers
{
    using System;
    using System.Drawing;
    using System.IO;
    using System.Threading.Tasks;
    using System.Web.Mvc;
    using System.Web.UI.DataVisualization.Charting;

    using PhoneTicket.Web.Handlers;
    using PhoneTicket.Web.Services;
    using PhoneTicket.Web.ViewModels;

    using Chart = System.Web.Helpers.Chart;

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

        public Task<ActionResult> SalesPerMoviePdf()
        {
            var id = Guid.NewGuid();
            var chartRelativePath = string.Format("~/Images/{0}.png", id);
            var absolutePath = HttpContext.Server.MapPath(chartRelativePath);
            var chart = new System.Web.UI.DataVisualization.Charting.Chart();

            chart.ChartAreas.Add(new ChartArea());

            chart.Series.Add(new Series("Data"));
            chart.Series["Data"].ChartType = SeriesChartType.Pie;
            chart.Series["Data"]["PieLabelStyle"] = "Inside";
            chart.Series["Data"].Font = new System.Drawing.Font("Trebuchet MS", 16, System.Drawing.FontStyle.Regular);
            chart.Series["Data"]["PieLineColor"] = "Black";
            chart.Series["Data"].Points.DataBindXY(
                new [] { "Dragon Ball Z", "El Conjuro", "Corazon de Leon" },
                new [] { 200, 500, 300 });

            chart.Legends.Add("Legend");
            chart.Series["Data"].Label = "#VAL";
            chart.Series["Data"].LegendText = "#VALX";
            chart.Legends[0].Enabled = true;
            chart.Width = 800;
            chart.Height = 450;

            using (var stream = new FileStream(absolutePath, FileMode.Create))
            {
                chart.SaveImage(stream, ChartImageFormat.Png);
            }

            var result = this.ViewPdf(new SalesPerMoviePdfViewModel { ChartRelativePath = absolutePath});

            System.IO.File.Delete(absolutePath);

            return Task.FromResult(result);
        }
    }
}