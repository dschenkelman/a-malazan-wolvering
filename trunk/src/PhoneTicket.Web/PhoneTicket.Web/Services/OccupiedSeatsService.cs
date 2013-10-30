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

        private IOperationService operationService;

        private IShowService showService;

        public OccupiedSeatsService(IPhoneTicketRepositories repositories, IOperationService operationService, IShowService showService)
        {
            this.repositories = repositories;

            this.operationService = operationService;

            this.showService = showService;
        }

        public async Task CreateAsync(OccupiedSeat occupiedSeat)
        {
            this.repositories.OccupiedSeats.Insert(occupiedSeat);

            await this.repositories.OccupiedSeats.SaveAsync();

            await this.ManageShowAvailability(occupiedSeat.OperationId);
        }

        public async Task ManageShowAvailability(Guid OperationId)
        {
            var showId = (await this.operationService.GetAsync(OperationId)).ShowId;

            await this.showService.ManageAvailability(showId);
        }

    }
}