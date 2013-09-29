namespace PhoneTicket.Web.Services
{
    using System;
    using System.Collections.Generic;
    using System.Data.Entity;
    using System.Linq;
    using System.Linq.Expressions;
    using System.Threading.Tasks;

    using PhoneTicket.Web.Data;
    using PhoneTicket.Web.Models;

    public class MovieService : IMovieService, IDisposable
    {
        private IPhoneTicketRepositories repositories;

        public MovieService(IPhoneTicketRepositories repositories)
        {
            this.repositories = repositories;
        }

        public Task<IEnumerable<Movie>> GetMoviesAsync()
        {
            return this.repositories.Movies.AllAsync();
        }

        public async Task<IEnumerable<Movie>> GetMoviesAsync(Expression<Func<Movie, bool>> filter)
        {
            if (filter != null)
            {
                return this.repositories.Movies.Filter(filter);
            }
            
            return await this.repositories.Movies.AllAsync();
        }

        public Task<Movie> GetAsync(int id)
        {
            return this.repositories.Movies.GetByKeyValuesAsync(id);
        }

        public async Task CreateAsync(Movie movie)
        {
            this.repositories.Movies.Insert(movie);

            await this.repositories.Movies.SaveAsync();
        }

        public Task UpdateAsync(Movie movie)
        {
            return this.repositories.Movies.SaveAsync();
        }

        public Task DeleteAsync(int movieId)
        {
            return this.repositories.Movies.DeleteAsync(movieId);
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