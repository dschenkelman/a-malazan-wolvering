namespace PhoneTicket.Web
{
    using System.Web.Http.Dependencies;

    using Microsoft.Practices.Unity;

    using PhoneTicket.Web.Data;
    using PhoneTicket.Web.Services;

    public class UnityResolverConfigurator
    {
        public IDependencyResolver Configure(IUnityContainer container)
        {
            container.RegisterType<ITemporaryUserService, TemporaryUserService>();
            container.RegisterType<IUserService, UserService>();
            container.RegisterType<IMovieService, MovieService>();
            
            container.RegisterType<PhoneTicket.Web.Controllers.Api.UsersController>();
            container.RegisterType<PhoneTicket.Web.Controllers.Api.MoviesController>();
            container.RegisterType<PhoneTicket.Web.Controllers.UsersController>();
            container.RegisterType<PhoneTicket.Web.Controllers.MoviesController>();
            container.RegisterType<PhoneTicketContext>();

            var emailService = new EmailService("smtp.gmail.com", 587, "wolveringticket@gmail.com", "wolvering", true, true);

            container.RegisterInstance<IEmailService>(emailService);

            var resolver = new UnityDependencyResolver(container);

            return resolver;
        }
    }
}