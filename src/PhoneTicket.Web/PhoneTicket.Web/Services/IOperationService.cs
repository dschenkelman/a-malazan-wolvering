namespace PhoneTicket.Web.Services
{
    using System;
    using System.Collections.Generic;
    using System.Linq.Expressions;
    using System.Threading.Tasks;

    using PhoneTicket.Web.Models;
    using PhoneTicket.Web.ViewModels.Api;

    public interface IOperationService
    {
        Task<Operation> GetAsync(Guid operationId);

        Task<IEnumerable<Operation>> GetAsync(Expression<Func<Operation, bool>> filter);

        Task<Guid> CreateAsync(Operation operation);

        Task DeleteAsync(Guid id);

        Task AddDiscountsAsync(Operation operation, IEnumerable<DiscountForOperationViewModel> discounts);

        Task SaveAsync(Operation operation);
    }
}
