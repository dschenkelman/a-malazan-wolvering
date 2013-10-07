namespace PhoneTicket.Web.Services
{
    using System;
    using System.Collections.Generic;
    using System.Data.Entity;
    using System.Linq;
    using System.Linq.Expressions;
    using System.Threading.Tasks;

    using PhoneTicket.Web.Data;
    using PhoneTicket.Web.Models;

    public class RoomService : IRoomService
    {
        private readonly IPhoneTicketRepositories repositories;

        public RoomService(IPhoneTicketRepositories repositories)
        {
            this.repositories = repositories;
        }

        public async Task<IEnumerable<Room>> GetAsync(Expression<Func<Room, bool>> filter)
        {
            return await this.repositories.Rooms.Filter(filter).ToListAsync();
        }
    }
}