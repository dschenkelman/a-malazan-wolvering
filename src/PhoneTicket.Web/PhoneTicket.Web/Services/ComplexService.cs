namespace PhoneTicket.Web.Services
{
    using System.Collections.Generic;
    using System.Threading.Tasks;

    using PhoneTicket.Web.Data;
    using PhoneTicket.Web.Models;

    public class ComplexService : IComplexService 
    {
        private readonly IPhoneTicketRepositories repositories;

        public ComplexService(IPhoneTicketRepositories repositories)
        {
            this.repositories = repositories;
        }

        public Task<IEnumerable<Complex>> GetAsync()
        {
            return this.repositories.Complexes.AllAsync();
        }
    }
}