namespace PhoneTicket.Web.Services
{
    using System;
    using System.Collections.Generic;
    using System.Linq.Expressions;
    using System.Threading.Tasks;

    using PhoneTicket.Web.Models;

    public interface IRoomService
    {
        Task<IEnumerable<Room>> GetAsync();
        Task<Room> GetAsync(int id);
        Task<IEnumerable<Room>> GetAsync(Expression<Func<Room, bool>> filter);
        Task CreateAsync(Room room);
        Task UpdateAsync(Room room);
        Task DeleteAsync(int roomId);
    }
}