namespace PhoneTicket.Web.Services
{
    using System;
    using System.Collections.Generic;
    using System.Data.Entity;
    using System.Threading.Tasks;

    using PhoneTicket.Web.Data;
    using PhoneTicket.Web.Models;

    public class MovieService : IMovieService, IDisposable
    {
        private PhoneTicketContext db;

        public MovieService(PhoneTicketContext db)
        {
            this.db = db;
        }

        public async Task<IEnumerable<Movie>> GetMovies()
        {
            return await this.db.Movies.ToListAsync();
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