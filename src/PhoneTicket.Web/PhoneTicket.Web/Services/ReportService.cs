namespace PhoneTicket.Web.Services
{
    using System;
    using System.Collections.Generic;
    using System.Data.Entity;
    using System.Linq;
    using System.Threading.Tasks;

    using PhoneTicket.Web.Data;
    using PhoneTicket.Web.Models;
    using PhoneTicket.Web.ViewModels;

    public class ReportService : IReportService
    {
        private readonly IRoomXmlParser roomXmlParser;

        private readonly IPhoneTicketRepositories repositories;

        public ReportService(IRoomXmlParser roomXmlParser, IPhoneTicketRepositories repositories)
        {
            this.roomXmlParser = roomXmlParser;
            this.repositories = repositories;
        }

        public async Task<ReportShowSeats> GetReportSeatsAsync(int showId)
        {
            var file = await this.repositories.Shows.Filter(s => s.Id == showId).Select(s => s.Room.File).FirstOrDefaultAsync();

            var reportShowSeats = this.roomXmlParser.Parse(file).ToReport();

            var seatsPerOperation =
                await
                this.repositories.Operations.Filter(o => o.ShowId == showId)
                    .Select(o => new { o.OccupiedSeats, o.Type })
                    .ToListAsync();

            foreach (var seatsInOperation in seatsPerOperation)
            {
                foreach (var seat in seatsInOperation.OccupiedSeats)
                {
                    if (seatsInOperation.Type == OperationType.Reservation)
                    {
                        reportShowSeats.MarkReserved(seat.Row, seat.Column);
                    }
                    else
                    {
                        reportShowSeats.MarkPurchased(seat.Row, seat.Column);
                    }
                }
            }

            return reportShowSeats;
        }

        public async Task<IEnumerable<MovieSalesViewModel>> GetSalesPerMovieReport(DateTime beginDate, DateTime endDate, int complexId)
        {
            var basicQuery = this.repositories.Operations.Filter(o => beginDate < o.Date && o.Date < endDate);

            string complexes;

            if (complexId != 0)
            {
                basicQuery = basicQuery.Where(o => o.Show.Room.ComplexId == complexId);

                complexes = (await this.repositories.Complexes.GetByKeyValuesAsync(complexId)).Name;
            }
            else
            {
                complexes = string.Join(", ", (await this.repositories.Complexes.AllAsync()).Select(c => c.Name));
            }

            var data = await basicQuery
                .Select(o => new { o.OccupiedSeats.Count, o.Show.Movie.Title })
                .GroupBy(o => o.Title)
                .Select(g => new { g.Key, Sum = g.Sum(a => a.Count) })
                .OrderByDescending(a => a.Sum)
                .ToListAsync();

            return data.Select(d => new MovieSalesViewModel { Movie = d.Key, Sales = d.Sum, Complexes = complexes });
        }
    }
}