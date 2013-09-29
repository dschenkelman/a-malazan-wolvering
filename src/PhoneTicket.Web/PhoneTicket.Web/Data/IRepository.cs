namespace PhoneTicket.Web.Data
{
    using System;
    using System.Collections.Generic;
    using System.Linq;
    using System.Linq.Expressions;
    using System.Threading.Tasks;

    public interface IRepository<T> : IDisposable
    {
        IQueryable<T> Filter(Expression<Func<T, bool>> predicate);

        Task<T> GetByKeyValuesAsync(params object[] keys);

        Task<IEnumerable<T>> AllAsync();

        void Insert(T t);

        Task DeleteAsync(int id);

        Task SaveAsync();
    }
}
