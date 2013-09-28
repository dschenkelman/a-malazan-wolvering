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

        public async Task<IEnumerable<Movie>> GetMoviesAsync()
        {
            return await this.db.Movies.ToListAsync();
        }

        public async Task<Movie> GetMovie(int id)
        {
            return await this.db.Movies.FindAsync(id);
        }

        public async Task CreateAsync(Movie movie)
        {
            this.db.Movies.Add(movie);

            await this.db.SaveChangesAsync();
        }

        public async Task UpdateAsync(Movie movie)
        {
            this.db.Entry(movie).State = EntityState.Modified;

            await this.db.SaveChangesAsync();
        }

        public async Task DeleteAsync(Movie movie)
        {
            this.db.Entry(movie).State = EntityState.Deleted;

            await this.db.SaveChangesAsync();
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