namespace PhoneTicket.Web.Services
{
    using PhoneTicket.Web.Models;

    using System.Collections.Generic;
    using System.Threading.Tasks;
    using System.Web.Mvc;

    public interface IRatingService
    {
        Task<IEnumerable<SelectListItem>> ListAsync(int? id);

        Task<Rating> GetAsync(int id);
    }
}
