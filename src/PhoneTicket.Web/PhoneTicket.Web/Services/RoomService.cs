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
    using System.Web.Mvc;

    public class RoomService : IRoomService, IDisposable
    {
        private IPhoneTicketRepositories repositories;

        public RoomService(IPhoneTicketRepositories repositories)
        {
            this.repositories = repositories;
        }

        public async Task<IEnumerable<Room>> GetAsync()
        {
            return await this.repositories.Rooms.AllAsync();
        }

        public async Task<IEnumerable<Room>> GetAsync(Expression<Func<Room, bool>> filter)
        {
            return await this.repositories.Rooms.Filter(filter).ToListAsync();
        }

        public async Task<Room> GetAsync(int id)
        {
            return await this.repositories.Rooms.GetByKeyValuesAsync(id);
        }

        public async Task CreateAsync(Room room)
        {
            this.repositories.Rooms.Insert(room);

            await this.repositories.Rooms.SaveAsync();
        }

        public async Task UpdateAsync(Room room)
        {
            await this.repositories.Rooms.SaveAsync();
        }

        public async Task DeleteAsync(int roomId)
        {
            await this.repositories.Rooms.DeleteAsync(roomId);

            await this.repositories.Rooms.SaveAsync();
        }

        public async Task<IEnumerable<SelectListItem>> SameComplexRoomsListAsync(int roomId)
        {
            //Filters rooms that belong to the same complex as roomId
            var room = await this.GetAsync(roomId);
            var rooms = await this.GetAsync(r => r.ComplexId == room.ComplexId);

            return from r in rooms
                   orderby r.Name
                   select new SelectListItem
                   {
                       Text = r.Name,
                       Value = r.Id.ToString(),
                       Selected = r.Id == roomId
                   };
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
                if (this.repositories != null)
                {
                    this.repositories.Dispose();
                    this.repositories = null;
                }
            }
        }
    }
}