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

    public class RatingService : IRatingService, IDisposable
    {
        private PhoneTicketContext db;

        public RatingService(PhoneTicketContext db)
        {
            this.db = db;
        }

        public async Task<IEnumerable<SelectListItem>> GetRatingListAsync(int? ID)
        {
            var ratings = await this.db.Ratings.ToListAsync();

            return new[] { new SelectListItem { Text = "Seleccionar uno" } }.Concat(
                   from r in ratings
                   orderby r.Description
                   select new SelectListItem
                   {
                       Text = r.Description,
                       Value = r.Id.ToString(),
                       Selected = (r.Id == ID)
                   });
        }

        public async Task<Rating> Get(int ID)
        {
            return (await this.db.Ratings.FindAsync(ID));
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