namespace PhoneTicket.Web.Services
{
    using System.Collections.Generic;
    using System.Threading.Tasks;

    using PhoneTicket.Web.Models;

    public interface IDiscountService
    {
        Task<IEnumerable<Discount>> GetActive();
    }
}