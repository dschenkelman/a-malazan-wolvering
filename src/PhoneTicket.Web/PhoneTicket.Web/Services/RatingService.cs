namespace PhoneTicket.Web.Services
{
    using System;
    using System.Collections.Generic;
    using System.Globalization;
    using System.Linq;
    using System.Threading.Tasks;
    using System.Web.Mvc;

    using PhoneTicket.Web.Data;
    using PhoneTicket.Web.Models;

    public class RatingService : IRatingService, IDisposable
    {
        private IPhoneTicketRepositories repositories;

        public RatingService(IPhoneTicketRepositories repositories)
        {
            this.repositories = repositories;
        }

        public async Task<IEnumerable<SelectListItem>> ListAsync(int? id)
        {
            var ratings = await this.repositories.Ratings.AllAsync();

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

        public Task<Rating> GetAsync(int id)
        {
            return this.repositories.Ratings.GetByKeyValuesAsync(id);
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