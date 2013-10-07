namespace PhoneTicket.Web.Controllers.Api
{
    using System.Collections.Generic;
    using System.Threading.Tasks;
    using System.Web.Http;

    using PhoneTicket.Web.Models;
    using PhoneTicket.Web.Services;

    [RoutePrefix("api/complexes")]
    public class ComplexesController : ApiController
    {
        private readonly IComplexService complexService;

        private readonly IRoomService roomService;

        public ComplexesController(IComplexService complexService, IRoomService roomService)
        {
            this.complexService = complexService;
            this.roomService = roomService;
        }

        [HttpGet]
        public Task<IEnumerable<Complex>> Get()
        {
            return this.complexService.GetAsync();
        }

        [HttpGet("{id}/rooms")]
        public async Task<IEnumerable<Room>> GetRooms(int id)
        {
            return await this.roomService.GetAsync(r => r.ComplexId == id);
        }
    }
}