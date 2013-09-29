namespace PhoneTicket.Web.Data
{
    using System;
    using System.Data.Entity;
    using System.Threading.Tasks;

    public interface IContext : IDisposable
    {
        Task<int> SaveChangesAsync();

        DbSet<TEntity> Set<TEntity>() where TEntity : class;
    }
}
