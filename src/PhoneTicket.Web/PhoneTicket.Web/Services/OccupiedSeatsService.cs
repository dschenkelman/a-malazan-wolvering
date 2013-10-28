namespace PhoneTicket.Web.Services
{
    using System;
    using System.Collections.Generic;
    using System.Linq;
    using System.Threading.Tasks;
    using System.Web;

    using PhoneTicket.Web.Models;
    using PhoneTicket.Web.Data;
    using System.Collections.ObjectModel;

    public class OccupiedSeatsService : IOccupiedSeatsService
    {
        private IPhoneTicketRepositories repositories;

        public OccupiedSeatsService(IPhoneTicketRepositories repositories)
        {
            this.repositories = repositories;

        }
        public async Task CreateAsync(OccupiedSeat occupiedSeat)
        {
            this.repositories.OccupiedSeats.Insert(occupiedSeat);

            await this.repositories.OccupiedSeats.SaveAsync();
        }

    }
}