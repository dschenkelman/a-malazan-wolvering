namespace PhoneTicket.Web.Services
{
    using System;
    using System.Collections.Generic;
    using System.Data.Entity;
    using System.Globalization;
    using System.Linq;
    using System.Threading.Tasks;
    using System.Web;
    using System.Web.Mvc;

    using PhoneTicket.Web.Data;
    using PhoneTicket.Web.Models;

    public class RatingService : IRatingService, IDisposable
    {
        private PhoneTicketContext db;

        public RatingService(PhoneTicketContext db)
        {
            this.db = db;
        }

        public async Task<IEnumerable<SelectListItem>> ListAsync(int? id)
        {
            var ratings = await this.db.Ratings.ToListAsync();

            return new[] { new SelectListItem { Text = "Seleccionar uno" } }.Concat(
                   from r in ratings
                   orderby r.Description
                   select new SelectListItem
                   {
                       Text = r.Description,
                       Value = r.Id.ToString(CultureInfo.InvariantCulture),
                       Selected = r.Id == id
                   });
        }

        public async Task<Rating> GetAsync(int id)
        {
            return (await this.db.Ratings.FindAsync(id));
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