namespace PhoneTicket.Web.Services
{
    using System;
    using System.Collections.Generic;
    using System.Linq;
    using System.Data.Entity;
    using System.Web;
    using System.Threading.Tasks;

    using PhoneTicket.Web.Data;
    using PhoneTicket.Web.Models;
    using System.Linq.Expressions;

    public class OperationService : IOperationService
    {
        private IPhoneTicketRepositories repositories;

        public OperationService(IPhoneTicketRepositories repositories)
        {
            this.repositories = repositories;
        }

        public async Task<IEnumerable<Operation>> GetAsync(Expression<Func<Operation, bool>> filter)
        {
            return await this.repositories.Operations.Filter(filter).ToListAsync();
        }

        public async Task<int> CreateAsync(Operation operation)
        {
            this.repositories.Operations.Insert(operation);

            await this.repositories.Operations.SaveAsync();

            return operation.Number;
        }
    }
}