namespace PhoneTicket.Web.Data
{
    using System;
    using System.Collections.Generic;
    using System.Data.Entity;
    using System.Linq;
    using System.Linq.Expressions;
    using System.Threading.Tasks;

    public class Repository<T> : IRepository<T> where T : class
    {
        private readonly IContext context;

        private readonly DbSet<T> dbSet;

        public Repository(IContext context)
        {
            this.context = context;
            this.dbSet = this.context.Set<T>();
        }

        public async Task<IEnumerable<T>> GetAsync(Expression<Func<T, bool>> predicate)
        {
            return await this.dbSet.Where(predicate).ToListAsync();
        }

        public Task<T> GetByKeyValuesAsync(params object[] keys)
        {
            return this.dbSet.FindAsync(keys);
        }

        public void Insert(T t)
        {
            this.dbSet.Add(t);
        }

        public async Task DeleteAsync(int id)
        {
            T t = await this.dbSet.FindAsync(id);
            this.dbSet.Remove(t);
        }

        public Task SaveAsync()
        {
            return this.context.SaveChangesAsync();
        }

        public void Dispose()
        {
            this.Dispose(true);
            GC.SuppressFinalize(this);
        }

        protected virtual void Dispose(bool disposing)
        {
            if (disposing)
            {
                this.context.Dispose();
            }
        }
    }
}