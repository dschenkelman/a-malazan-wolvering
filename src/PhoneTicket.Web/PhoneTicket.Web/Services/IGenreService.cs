namespace PhoneTicket.Web.Services
{
    using System;
    using System.Collections.Generic;
    using System.Threading.Tasks;
    using System.Web.Mvc;


    public interface IGenreService
    {
        Task<IEnumerable<SelectListItem>> GetGenreListAsync(int? ID);
    }
}
