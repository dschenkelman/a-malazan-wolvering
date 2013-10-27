namespace PhoneTicket.Web.Controllers.Api
{
    using System.Collections.Generic;
    using System.Linq;
    using System.Web.Http;
    using System.Threading.Tasks;

    using PhoneTicket.Web.Services;
    using PhoneTicket.Web.ViewModels.Api;
    using PhoneTicket.Web.Models;
    using PhoneTicket.Web.Helpers;

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
        public async Task<IEnumerable<ComplexViewModel>> Get()
        {
            var complexes = await this.complexService.GetAsync();

            return complexes.Select(c => ComplexViewModel.FromComplex(c));
        }

        [HttpGet("{id}")]
        public async Task<ComplexViewModel> Get(int id)
        {
            var complex = await this.complexService.GetAsync(id);

            return ComplexViewModel.FromComplex(complex);
        }

        [HttpGet("{id}/rooms")]
        public async Task<IEnumerable<Room>> GetRooms(int id)
        {
            return await this.roomService.GetAsync(r => r.ComplexId == id);
        }
    }
}