namespace PhoneTicket.Web.Services
{
    using System;
    using System.Collections.Generic;
    using System.Linq;
    using System.Linq.Expressions;
    using System.Text;
    using System.Threading.Tasks;

    using PhoneTicket.Web.Models;

    public interface IOcuppiedSeatsService
    {
        Task<IEnumerable<OccupiedSeat>> GetAsync(Expression<Func<OccupiedSeat, bool>> filter);
    }
}
