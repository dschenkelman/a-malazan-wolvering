namespace PhoneTicket.Web.Controllers
{
    using System;
    using System.Net;
    using System.Net.Http;
    using System.Text;
    using System.Threading;
    using System.Threading.Tasks;
    using System.Web.Http;

    using PhoneTicket.Web.Services;
    using PhoneTicket.Web.ViewModels;

    [RoutePrefix("api/users")]
    public class UsersController : ApiController
    {
        private readonly IUserService userService;

        private readonly ITemporaryUserService temporaryUserService;

        public UsersController(IUserService userService, ITemporaryUserService temporaryUserService)
        {
            this.userService = userService;
            this.temporaryUserService = temporaryUserService;
        }

        [HttpPost("")]
        public async Task<HttpResponseMessage> Create(NewUserViewModel user)
        {
            await this.temporaryUserService.CreateUser(user);

            return new HttpResponseMessage(HttpStatusCode.Created);
        }

        [HttpGet("{id}/confirm")]
        public async Task<HttpResponseMessage> Confirm(int id, Guid secret)
        {
            if (await this.temporaryUserService.IsSecretValid(id, secret))
            {
                await this.temporaryUserService.ConfirmUser(id);

                var response = new HttpResponseMessage
                {
                    Content =
                        new StringContent("<head><title>Confirmacion de cuenta</title></head><body><p>Gracias por su confirmacion!</p></body>", Encoding.UTF8, "text/html"),
                };

                return response;
            }

            throw new HttpResponseException(HttpStatusCode.NotFound);
        }

        [Authorize]
        [HttpPost("auth")]
        public async Task<int> Auth()
        {
            return await this.userService.GetId(Thread.CurrentPrincipal.Identity.Name);
        }
    }
}