namespace PhoneTicket.Web.Services
{
    using System;
    using System.Collections.Generic;
    using System.Data.Entity;
    using System.Threading.Tasks;

    using PhoneTicket.Web.Data;
    using PhoneTicket.Web.Helpers;
    using PhoneTicket.Web.Models;

    public class DiscountService : IDiscountService
    {
        private readonly IPhoneTicketRepositories repositories;

        public DiscountService(IPhoneTicketRepositories repositories)
        {
            this.repositories = repositories;
        }

        public async Task<IEnumerable<Discount>> GetActive()
        {
            var currentDate = DateTimeHelpers.DateTimeInArgentina;

            return await this.repositories.Discounts.Filter(d => d.StartDate <= currentDate && currentDate <= d.EndDate).ToListAsync();
        }
    }
}