namespace PhoneTicket.Web.Services
{
    using System.Collections.Generic;
    using System.Threading.Tasks;

    using PhoneTicket.Web.Models;

    public interface IShowService
    {
        Task<Show> GetAsync(int id);
        Task UpdateAsync(Show show);
        Task Add(params Show[] shows);
        Task<IEnumerable<Show>> GetForMovieAsync(int movieId);
        Task DeleteAsync(int showId);
        Task ChangeAvailability(int showId);
        Task<IEnumerable<OccupiedSeat>> GetOccupiedSeats(int showId);
        Task ManageAvailability(int showId);
    }
}
