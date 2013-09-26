namespace PhoneTicket.Web.Services
{
    using System;
    using System.Collections.Generic;
    using System.Linq;
    using System.Text;
    using System.Threading.Tasks;
    using System.Web.Mvc;

    public interface IRatingService
    {
        Task<IEnumerable<SelectListItem>> GetRatingListAsync(int? ID);
    }
}
