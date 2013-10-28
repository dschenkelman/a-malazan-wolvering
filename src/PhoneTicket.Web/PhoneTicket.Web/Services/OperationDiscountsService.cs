namespace PhoneTicket.Web.Services
{
    using PhoneTicket.Web.Data;
    using System;
    using System.Collections.Generic;
    using System.Linq;
    using System.Threading.Tasks;
    using System.Web;

    public class OperationDiscountsService : IOperationDiscountsService
    {
        private IPhoneTicketRepositories repositories;

        public OperationDiscountsService(IPhoneTicketRepositories repositories)
        {
            this.repositories = repositories;
        }

        public async Task DeleteAsync(int id)
        {
            await this.repositories.OperationDiscounts.DeleteAsync(id);

            await this.repositories.OperationDiscounts.SaveAsync();
        }
    }
}