namespace PhoneTicket.Web.Controllers.Api
{
    using System.Collections.Generic;
    using System.Threading.Tasks;
    using System.Web.Http;

    using PhoneTicket.Web.Models;
    using PhoneTicket.Web.Services;

    public class ComplexesController : ApiController
    {
        private readonly IComplexService complexService;

        public ComplexesController(IComplexService complexService)
        {
            this.complexService = complexService;
        }

        public Task<IEnumerable<Complex>> Get()
        {
            return this.complexService.GetAsync();
        }
    }
}