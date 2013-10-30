namespace PhoneTicket.Web.Controllers.Api
{
    using PhoneTicket.Web.Models;
    using PhoneTicket.Web.Services;
    using System;
    using System.Collections.Generic;
    using System.Linq;
    using System.Threading.Tasks;
    using System.Web;
    using System.Web.Http;

    [RoutePrefix("api/creditCards")]
    public class CreditCardsController : ApiController
    {

        private readonly ICreditCardsService creditCardsService;

        public CreditCardsController(ICreditCardsService creditCardsService)
        {
            this.creditCardsService = creditCardsService;
        }

        [Authorize]
        [HttpGet("")]
        public async Task<IEnumerable<CreditCardCompany>> GetCreditCardCompanies()
        {
            return await this.creditCardsService.GetAllAsync();
        }
    }
}