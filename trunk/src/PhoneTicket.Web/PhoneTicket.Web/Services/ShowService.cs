namespace PhoneTicket.Web.Services
{
    using System;
    using System.Collections.Generic;
    using System.Data.Entity;
    using System.Linq;

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

        public Task<Show> GetAsync(int id)
        {
            return this.repositories.Shows.GetByKeyValuesAsync(id);
        }

        public Task UpdateAsync(Show show)
        {
            return this.repositories.Shows.SaveAsync();
        }

        public Task Add(params Show[] shows)
        {
            foreach (var show in shows)
            {
                this.repositories.Shows.Insert(show);    
            }

            return shows.Length > 0 ? this.repositories.Shows.SaveAsync() : Task.FromResult<object>(null);
        }

        public async Task<IEnumerable<Show>> GetForMovieAsync(int movieId)
        {
            return await this.repositories.Shows.Filter(s => s.MovieId == movieId).ToListAsync();
        }

        public async Task DeleteAsync(int showId)
        {
            await this.repositories.Shows.DeleteAsync(showId);

            await this.repositories.Shows.SaveAsync();
        }

        public async Task ChangeAvailability(int showId)
        {
            var show = await this.repositories.Shows.GetByKeyValuesAsync(showId);

            show.IsAvailable = !show.IsAvailable;

            await this.repositories.Shows.SaveAsync();
        }

        public async Task<IEnumerable<OccupiedSeat>> GetOccupiedSeats(int showId)
        {
            var seats = await this.repositories.Operations.Filter(op => op.ShowId == showId).SelectMany(op => op.OccupiedSeats).ToListAsync();

            return seats;
        }

        public async Task ManageAvailabilityAsync(int showId)
        {
            var occupiedSeatsCount = (await this.GetOccupiedSeats(showId)).Count();

            var show = await this.GetAsync(showId);

            if (show.Room.Capacity == occupiedSeatsCount)
            {
                show.IsAvailable = false;
            }
            else if (show.Room.Capacity > occupiedSeatsCount)
            {
                show.IsAvailable = true;
            }

            await this.repositories.Shows.SaveAsync();
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