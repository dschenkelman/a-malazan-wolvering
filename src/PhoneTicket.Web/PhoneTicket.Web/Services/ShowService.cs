namespace PhoneTicket.Web.Services
{
    using System;
    using System.Collections.Generic;
    using System.Linq;
    using System.Web;

    using PhoneTicket.Web.Data;
    using PhoneTicket.Web.Models;
    using System.Threading.Tasks;

    public class ShowService: IShowService, IDisposable
    {
        private IPhoneTicketRepositories repositories;

        public ShowService(IPhoneTicketRepositories repositories)
        {
            this.repositories = repositories;
        }

        /*public async Task<Show> GetAsync(int id)
        {
            return await this.repositories.Shows.GetByKeyValuesAsync(id);
        }

        public async Task UpdateAsync(Show show)
        {
            await this.repositories.Shows.SaveAsync();
        }*/

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