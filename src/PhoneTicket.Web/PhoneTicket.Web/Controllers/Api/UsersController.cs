namespace PhoneTicket.Web.Controllers.Api
{
    using System;
    using System.Net;
    using System.Net.Http;
    using System.Text;
    using System.Threading.Tasks;
    using System.Web.Http;

    using PhoneTicket.Web.Services;
    using PhoneTicket.Web.ViewModels;

    public class UsersController : ApiController
    {
        private readonly ITemporaryUserService temporaryUserService;

        public UsersController(ITemporaryUserService temporaryUserService)
        {
            this.temporaryUserService = temporaryUserService;
        }

        public async Task<HttpResponseMessage> Post(NewUserViewModel user)
        {
            await this.temporaryUserService.CreateUser(user);

            return new HttpResponseMessage(HttpStatusCode.Created);
        }

        [HttpGet]
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
    }
}