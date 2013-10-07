namespace PhoneTicket.Web.Services
{
    using System;
    using System.Collections.Generic;
    using System.Linq;
    using System.Threading.Tasks;
    using System.Web;
    using System.Web.Mvc;

    using PhoneTicket.Web.Data;

    public class RoomTypeService : IRoomTypeService, IDisposable
    {
        private IPhoneTicketRepositories repositories;

        public RoomTypeService(IPhoneTicketRepositories repositories)
        {
            this.repositories = repositories;
        }

        public async Task<IEnumerable<SelectListItem>> ListAsync(int? id)
        {
            var roomTypes = await this.repositories.RoomTypes.AllAsync();

            return from r in roomTypes
                    orderby r.Description
                    select new SelectListItem
                    {
                        Text = r.Description,
                        Value = r.Id.ToString(),
                        Selected = r.Id == id
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