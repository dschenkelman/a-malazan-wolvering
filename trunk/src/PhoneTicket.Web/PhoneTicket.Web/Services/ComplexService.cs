namespace PhoneTicket.Web.Services
{
    using System.Linq;
    using System.Collections.Generic;
    using System.Threading.Tasks;
    using System.Web.Mvc;

    using PhoneTicket.Web.Data;
    using PhoneTicket.Web.Models;
    using System;

    

    public class ComplexService : IComplexService 
    {
        private IPhoneTicketRepositories repositories;

        public ComplexService(IPhoneTicketRepositories repositories)
        {
            this.repositories = repositories;
        }

        public Task<IEnumerable<Complex>> GetAsync()
        {
            return this.repositories.Complexes.AllAsync();
        }

        public Task<Complex> GetAsync(int id)
        {
            return this.repositories.Complexes.GetByKeyValuesAsync(id);
        }

        public async Task<IEnumerable<SelectListItem>> ListAsync(int? id)
        {
            var complexes = await this.repositories.Complexes.AllAsync();

            return from c in complexes
                   orderby c.Name
                   select new SelectListItem
                   {
                       Text = c.Name,
                       Value = c.Id.ToString(),
                       Selected = c.Id == id
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