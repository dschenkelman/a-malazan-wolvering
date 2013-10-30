namespace PhoneTicket.Web.Services
{
    using System;
    using System.Collections.Generic;
    using System.Data.Entity;
    using System.Threading.Tasks;

    using PhoneTicket.Web.Data;
    using PhoneTicket.Web.Models;
    using System.Linq.Expressions;

    using PhoneTicket.Web.ViewModels.Api;

    public class OperationService : IOperationService
    {
        private readonly IPhoneTicketRepositories repositories;

        public OperationService(IPhoneTicketRepositories repositories)
        {
            this.repositories = repositories;
        }

        public async Task<Operation> GetAsync(Guid operationId)
        {
            return await this.repositories.Operations.GetByKeyValuesAsync(operationId);
        }

        public async Task<IEnumerable<Operation>> GetAsync(Expression<Func<Operation, bool>> filter)
        {
            return await this.repositories.Operations.Filter(filter).ToListAsync();
        }

        public async Task<Guid> CreateAsync(Operation operation)
        {
            this.repositories.Operations.Insert(operation);

            await this.repositories.Operations.SaveAsync();

            return operation.Number;
        }

        public async Task DeleteAsync(Guid id)
        {
            await this.repositories.Operations.DeleteAsync(id);

            await this.repositories.Operations.SaveAsync();
        }

        public async Task AddDiscountsAsync(Operation operation, IEnumerable<DiscountForOperationViewModel> discounts)
        {
            foreach (var discount in discounts)
            {
                operation.OperationDiscounts.Add(new OperationDiscount { DiscountId = discount.DiscountId, Count = discount.Count });
            }

            await this.repositories.Operations.SaveAsync();
        }
    }
}