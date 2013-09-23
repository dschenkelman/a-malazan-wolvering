namespace PhoneTicket.Web.Services
{
    using System;
    using System.Collections.Generic;
    using System.Threading.Tasks;

    using PhoneTicket.Web.Models;

    public interface IMovieService
    {
        Task<IEnumerable<Movie>> GetMoviesAsync();
        Task<Movie> GetMovie(int id);
    }
}