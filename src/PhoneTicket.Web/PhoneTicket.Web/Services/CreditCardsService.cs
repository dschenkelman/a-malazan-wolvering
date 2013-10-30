namespace PhoneTicket.Web.Services
{
    using PhoneTicket.Web.Data;
    using PhoneTicket.Web.Models;
    using System;
    using System.Collections.Generic;
    using System.Linq;
    using System.Threading.Tasks;
    using System.Web;

    public class CreditCardsService : ICreditCardsService
    {
        private IPhoneTicketRepositories repositories;

        public CreditCardsService(IPhoneTicketRepositories repositories)
        {
            this.repositories = repositories;
        }


        public async Task<IEnumerable<CreditCardCompany>> GetAllAsync()
        {
            return await this.repositories.CreditCardCompanies.AllAsync();
        }
    }
}