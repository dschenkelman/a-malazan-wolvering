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
        Task<IEnumerable<Operation>> GetAsync(Expression<Func<Operation, bool>> filter);
    }
}
