namespace PhoneTicket.Web.Services
{
    using System;
    using System.Collections.Generic;
    using System.Data.Entity;
    using System.Linq;
    using System.Threading.Tasks;
    using System.Web;
    using System.Web.Mvc;
    

    using PhoneTicket.Web.Data;
    using PhoneTicket.Web.Models;

    public class GenreService : IGenreService, IDisposable
    {
        private PhoneTicketContext db;

        public GenreService(PhoneTicketContext db)
        {
            this.db = db;
        }

        public async Task<IEnumerable<SelectListItem>> GetGenreListAsync(int? ID)
        {
            var genres = await this.db.Genres.ToListAsync();

            return new[] { new SelectListItem { Text = "Seleccionar uno" } }.Concat(
                   from g in genres
                   orderby g.Name
                   select new SelectListItem
                   {
                       Text = g.Name,
                       Value = g.Id.ToString(),
                       Selected = (g.Id == ID)
                   });
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
                if (this.db != null)
                {
                    this.db.Dispose();
                    this.db = null;
                }
            }
        }
    }
}