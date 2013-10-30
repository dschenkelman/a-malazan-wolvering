namespace PhoneTicket.Web.Services
{
    using PhoneTicket.Web.Models;

    using System;
    using System.Collections.Generic;
    using System.Linq;
    using System.Text;
    using System.Threading.Tasks;
    
    public interface ICreditCardsService
    {
        Task<IEnumerable<CreditCardCompany>> GetAllAsync();
    }
}
