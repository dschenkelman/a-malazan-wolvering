namespace PhoneTicket.Web.Controllers.Api
{
    using System;
    using System.Net;
    using System.Net.Http;
    using System.Text;
    using System.Threading;
    using System.Threading.Tasks;
    using System.Web.Http;

    using PhoneTicket.Web.Properties;
    using PhoneTicket.Web.Services;
    using PhoneTicket.Web.Templates;
    using PhoneTicket.Web.ViewModels;

    [RoutePrefix("api/users")]
    public class UsersController : ApiController
    {
        private readonly IUserService userService;

        private readonly ITemporaryUserService temporaryUserService;

        private IEmailService emailService;

        public UsersController(IUserService userService, ITemporaryUserService temporaryUserService, IEmailService emailService)
        {
            this.userService = userService;
            this.temporaryUserService = temporaryUserService;
            this.emailService = emailService;
        }

        [HttpPost("")]
        public async Task<HttpResponseMessage> Create(NewUserViewModel userViewModel)
        {
            var user = userViewModel.ToUser();

            if (await this.userService.HasConflict(user))
            {
                return new HttpResponseMessage(HttpStatusCode.Conflict);
            }

            var secret = await this.temporaryUserService.CreateUserAsync(user);
            
            var template = new EmailTemplate(user, secret);

            var confirmationMail = this.emailService.CreateMessage(Resources.ConfirmationEmailSubject, template.TransformText(), user.EmailAddress);

            await this.emailService.SendAsync(confirmationMail);

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
            return await this.userService.GetIdAsync(Thread.CurrentPrincipal.Identity.Name);
        }

        [Authorize(Users = "admin")]
        [HttpPut("{id}")]
        public async Task<HttpResponseMessage> Edit(int id, EditUserViewModel viewModel)
        {
            var user = await this.userService.GetUserAsync(id);
            user.IsValid = viewModel.IsValid;
            await this.userService.UpdateAsync(user);

            return new HttpResponseMessage(HttpStatusCode.NoContent);
        }
    }
}