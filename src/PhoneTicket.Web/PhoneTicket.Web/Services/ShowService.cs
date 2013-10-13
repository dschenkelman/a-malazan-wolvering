namespace PhoneTicket.Web.Services
{
    using System;

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

        public async Task<Show> GetAsync(int id)
        {
            //TODO REMOVE MOCK
            //return await this.repositories.Shows.GetByKeyValuesAsync(id);
            return new Show { Id = 1, MovieId = 1, RoomId = 1, Date = new DateTime(2013, 10, 10), Price = 10.0d, IsAvailable = true };
            
        }

        public async Task UpdateAsync(Show show)
        {
            //await this.repositories.Shows.SaveAsync();
        }

        public Task Add(params Show[] shows)
        {
            foreach (var show in shows)
            {
                this.repositories.Shows.Insert(show);    
            }

            return this.repositories.Shows.SaveAsync();
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