﻿namespace PhoneTicket.Web.Controllers
{
    using System;
    using System.Collections.Generic;
    using System.Drawing;
    using System.IO;
    using System.Linq;
    using System.Threading.Tasks;
    using System.Web.Mvc;
    using System.Web.UI.DataVisualization.Charting;

    using PhoneTicket.Web.Handlers;
    using PhoneTicket.Web.Services;
    using PhoneTicket.Web.ViewModels;

    [Authorize]
    [RequireSsl]
    public class ReportsController : BaseReportsController
    {
        private const string TempImageDir = "~/Images/tmp";

        private readonly IReportService reportService;

        private readonly IShowService showService;

        private readonly IComplexService complexService;

        public ReportsController(IReportService reportService, IShowService showService, IComplexService complexService)
        {
            this.reportService = reportService;
            this.showService = showService;
            this.complexService = complexService;
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

        public async Task<ActionResult> SalesPerMovie()
        {
            var complexes = (await this.complexService.GetAsync())
                .Select(c => new SelectListItem { Selected = false, Text = c.Name, Value = c.Id.ToString() })
                .Concat(new[] { new SelectListItem { Selected = true, Text = "Todos", Value = "0" } });

            var viewModel = new SalesPerMovieViewModel { Complexes = complexes };

            return this.View(viewModel);
        }

        [HttpPost]
        public async Task<ActionResult> SalesPerMovie(SalesPerMovieViewModel searchParameters)
        {
            var complexes = new[] { new SelectListItem { Selected = searchParameters.ComplexId == 0, Text = "Todos", Value = "0" } }
                .Concat((await this.complexService.GetAsync())
                    .Select(c => new SelectListItem { Selected = searchParameters.ComplexId == c.Id, Text = c.Name, Value = c.Id.ToString() }));

            if (!ModelState.IsValid)
            {
                searchParameters.Complexes = complexes;
                return this.View(searchParameters);
            }

            var salesPerMovie = await this.reportService.GetSalesPerMovieReport(searchParameters.FromDate, searchParameters.ToDate, searchParameters.ComplexId);

            var relativePath = this.GenerateChartSalesPerMovie(salesPerMovie);

            var viewModel = new SalesPerMovieViewModel { Complexes = complexes, MovieSales = salesPerMovie, ChartPath = relativePath };

            return this.View(viewModel);
        }

        public async Task<ActionResult> SalesPerMoviePdf(SalesPerMovieViewModel searchParameters)
        {
            var complexes = new[] { new SelectListItem { Selected = searchParameters.ComplexId == 0, Text = "Todos", Value = "0" } }
                .Concat((await this.complexService.GetAsync())
                    .Select(c => new SelectListItem { Selected = searchParameters.ComplexId == c.Id, Text = c.Name, Value = c.Id.ToString() }));

            if (!ModelState.IsValid)
            {
                searchParameters.Complexes = complexes;
                return this.View("SalesPerMovie", searchParameters);
            }

            var salesPerMovie = await this.reportService.GetSalesPerMovieReport(searchParameters.FromDate, searchParameters.ToDate, searchParameters.ComplexId);

            var relativePath = this.GenerateChartSalesPerMovie(salesPerMovie);

            var absolutePath = this.HttpContext.Server.MapPath(relativePath);

            var result = this.ViewPdf(new SalesPerMovieViewModel { ChartPath = absolutePath, MovieSales = salesPerMovie });

            System.IO.File.Delete(absolutePath);

            return result;
        }

        private string GenerateChartSalesPerMovie(IEnumerable<MovieSalesViewModel> salesPerMovie)
        {
            var id = Guid.NewGuid();

            var tempDirPath = this.HttpContext.Server.MapPath(TempImageDir);

            if (!Directory.Exists(tempDirPath))
            {
                Directory.CreateDirectory(TempImageDir);
            }

            var chartRelativePath = string.Format("{0}/{1}.png", TempImageDir, id);
            var absolutePath = this.HttpContext.Server.MapPath(chartRelativePath);
            var chart = new Chart { BackColor = Color.Transparent };

            chart.ChartAreas.Add(new ChartArea() { BackColor = Color.Transparent });

            chart.Series.Add(new Series("Data"));
            chart.Series["Data"].ChartType = SeriesChartType.Pie;
            chart.Series["Data"]["PieLabelStyle"] = "Inside";
            chart.Series["Data"].Font = new System.Drawing.Font("Trebuchet MS", 16, System.Drawing.FontStyle.Regular);
            chart.Series["Data"]["PieLineColor"] = "Black";
            chart.Series["Data"].Points.DataBindXY(
                salesPerMovie.Select(spm => spm.Movie).ToArray(), salesPerMovie.Select(spm => spm.Sales).ToArray());

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

            return chartRelativePath;
        }

        public async Task<ActionResult> BestShowTimesSellers()
        {
            var complexes = (await this.complexService.GetAsync())
                .Select(c => new SelectListItem { Selected = false, Text = c.Name, Value = c.Id.ToString() });

            var viewModel = new BestShowTimesSellersViewModel { Complexes = complexes };

            return this.View(viewModel);
        }

        [HttpPost]
        public async Task<ActionResult> BestShowTimesSellers(BestShowTimesSellersViewModel searchParameters)
        {
            var complexes = (await this.complexService.GetAsync())
                .Select(c => new SelectListItem { Selected = false, Text = c.Name, Value = c.Id.ToString() });

            if (!ModelState.IsValid)
            {
                searchParameters.Complexes = complexes;
                return this.View(searchParameters);
            }

            var viewModel = await this.BestShowTimesSellersViewModel(searchParameters, complexes);

            return this.View(viewModel);
        }

        public async Task<ActionResult> BestShowTimesSellersPdf(BestShowTimesSellersViewModel searchParameters)
        {
            var complexes = (await this.complexService.GetAsync())
                .Select(c => new SelectListItem { Selected = false, Text = c.Name, Value = c.Id.ToString() });

            if (!ModelState.IsValid)
            {
                searchParameters.Complexes = complexes;
                return this.View("BestShowTimesSellers",searchParameters);
            }

            var viewModel = await this.BestShowTimesSellersViewModel(searchParameters, complexes);

            var absolutePath = this.HttpContext.Server.MapPath(viewModel.ChartPath);

            viewModel.ChartPath = absolutePath;

            var result = this.ViewPdf(viewModel);

            System.IO.File.Delete(absolutePath);

            return result;
        }

        private async Task<BestShowTimesSellersViewModel> BestShowTimesSellersViewModel(BestShowTimesSellersViewModel searchParameters, IEnumerable<SelectListItem> complexes)
        {
            var complex = await this.complexService.GetAsync(searchParameters.ComplexId);

            var shows = await this.showService.GetShowsBetweenDates(searchParameters.FromDate, searchParameters.ToDate);

            var groupedShows =
                shows.Where(s => s.Room.Complex.Id == searchParameters.ComplexId)
                     .GroupBy(s => s.Date.ToString("HH:mm"))
                     .OrderByDescending(gs => gs.Sum(s => s.Operations.Sum(o => o.OccupiedSeats.Count())));

            var relativePath = this.GenerateBestShowTimesSellersChart(groupedShows);

            var viewModel = new BestShowTimesSellersViewModel
            {
                ChartPath = relativePath,
                ShowTimesInfo = groupedShows.Select(gs => new ShowTimeTicketCountViewModel
                {
                    Time = gs.Key,
                    TicketCount = gs.Sum(s => s.Operations.Sum(o => o.OccupiedSeats.Count())),
                    MovieCount = gs.Select(s => s.Movie.Title).Distinct().Count()
                }
                                                   ),
                ComplexName = complex.Name,
                From = searchParameters.FromDate.ToString("yyyy-MM-dd"),
                To = searchParameters.ToDate.ToString("yyyy-MM-dd"),
                Complexes = complexes,
            };

            return viewModel;
        }

        private string GenerateBestShowTimesSellersChart(IOrderedEnumerable<IGrouping<string, Models.Show>> groupedShows)
        {
            var showTimes = groupedShows.Select(gs => gs.Key).Take(10).ToArray();
            var ticketsPerShowTimeTopTen =
                groupedShows.Select(gs => gs.Sum(s => s.Operations.Sum(o => o.OccupiedSeats.Count())))
                            .Take(10)
                            .ToArray();

            var id = Guid.NewGuid();

            var tempDirPath = this.HttpContext.Server.MapPath(TempImageDir);

            if (!Directory.Exists(tempDirPath))
            {
                Directory.CreateDirectory(TempImageDir);
            }

            var chartRelativePath = string.Format("{0}/{1}.png", TempImageDir, id);
            var absolutePath = this.HttpContext.Server.MapPath(chartRelativePath);
            var chart = new Chart { BackColor = Color.Transparent };

            chart.ChartAreas.Add(new ChartArea() { BackColor = Color.Transparent });

            chart.Series.Add(new Series("Data"));
            chart.Series["Data"].ChartType = SeriesChartType.Column;
            chart.Series["Data"].Font = new System.Drawing.Font("Trebuchet MS", 14, System.Drawing.FontStyle.Regular);
            chart.Series["Data"].Points.DataBindXY(showTimes, ticketsPerShowTimeTopTen);

            chart.Legends.Add("Legend");
            chart.Series["Data"].Label = "#VAL";
            chart.Series["Data"].LegendText = "#Tickets Vendidos";
            chart.Legends[0].Enabled = true;
            chart.Width = 800;
            chart.Height = 450;

            using (var stream = new FileStream(absolutePath, FileMode.Create))
            {
                chart.SaveImage(stream, ChartImageFormat.Png);
            }

            return chartRelativePath;
        }
    }
}