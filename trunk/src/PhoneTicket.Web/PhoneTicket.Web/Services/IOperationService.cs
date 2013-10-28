namespace PhoneTicket.Web.Services
{
    using System;
    using System.Collections.Generic;
    using System.Linq;
    using System.Linq.Expressions;
    using System.Text;
    using System.Threading.Tasks;

    using PhoneTicket.Web.Models;

    public interface IOperationService
    {
        Task<Operation> GetAsync(int operationId);

        Task<IEnumerable<Operation>> GetAsync(Expression<Func<Operation, bool>> filter);

        Task<int> CreateAsync(Operation operation);

        Task DeleteAsync(int id);
    }
}
