namespace PhoneTicket.Web.Services
{
    using System.Collections.Generic;
    using System.Threading.Tasks;
    using System.Web.Mvc;

    using PhoneTicket.Web.Models;
    
    public interface IComplexService
    {
        Task<IEnumerable<Complex>> GetAsync();
        Task<Complex> GetAsync(int id);
        Task<IEnumerable<SelectListItem>> ListAsync(int? id);
    }
}