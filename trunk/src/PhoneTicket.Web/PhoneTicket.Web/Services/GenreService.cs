namespace PhoneTicket.Web.Services
{
    using System;
    using System.Collections.Generic;
    using System.Linq;
    using System.Threading.Tasks;
    using System.Web.Mvc;

    using PhoneTicket.Web.Data;
    using PhoneTicket.Web.Models;

    public class GenreService : IGenreService, IDisposable
    {
        private IPhoneTicketRepositories repositories;

        public GenreService(IPhoneTicketRepositories repositories)
        {
            this.repositories = repositories;
        }

        public async Task<IEnumerable<SelectListItem>> ListAsync(int? id)
        {
            var genres = await this.repositories.Genres.AllAsync();

            return from g in genres
                   orderby g.Name
                   select new SelectListItem
                   {
                       Text = g.Name,
                       Value = g.Id.ToString(),
                       Selected = g.Id == id
                   };
        }

        public Task<Genre> GetAsync(int id)
        {
            return this.repositories.Genres.GetByKeyValuesAsync(id);
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