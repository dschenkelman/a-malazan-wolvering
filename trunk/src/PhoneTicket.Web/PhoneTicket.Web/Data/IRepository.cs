namespace PhoneTicket.Web.Data
{
    using System;
    using System.Collections.Generic;
    using System.Linq.Expressions;
    using System.Threading.Tasks;

    public interface IRepository<T> : IDisposable
    {
        Task<IEnumerable<T>> GetAsync(Expression<Func<T, bool>> predicate);

        Task<T> GetByKeyValuesAsync(params object[] keys);

        void Insert(T t);

        Task DeleteAsync(int id);

        Task SaveAsync();
    }
}
