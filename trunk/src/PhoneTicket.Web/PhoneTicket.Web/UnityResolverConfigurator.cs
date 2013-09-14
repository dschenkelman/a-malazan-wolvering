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
            
            container.RegisterType<PhoneTicket.Web.Controllers.Api.UsersController>();
            container.RegisterType<PhoneTicket.Web.Controllers.UsersController>();
            container.RegisterType<PhoneTicketContext>();

            var resolver = new UnityDependencyResolver(container);

            return resolver;
        }
    }
}