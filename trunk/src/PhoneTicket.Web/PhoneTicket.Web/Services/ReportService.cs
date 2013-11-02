namespace PhoneTicket.Web.Services
{
    using System.Data.Entity;
    using System.Linq;
    using System.Threading.Tasks;

    using PhoneTicket.Web.Data;
    using PhoneTicket.Web.Models;

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
    }
}