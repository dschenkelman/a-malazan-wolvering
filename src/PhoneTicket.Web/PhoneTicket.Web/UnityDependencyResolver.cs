namespace PhoneTicket.Web
{
    using System;
    using System.Collections.Generic;
    using System.Linq;
    using System.Web.Http.Dependencies;

    using Microsoft.Practices.Unity;

    public class UnityDependencyResolver : IDependencyResolver
    {
        private readonly IUnityContainer container;

        public UnityDependencyResolver(IUnityContainer container)
        {
            this.container = container;
        }

        public object GetService(Type serviceType)
        {
            if (this.container.IsRegistered(serviceType))
            {
                return this.container.Resolve(serviceType);
            }

            return null;
        }

        public IEnumerable<object> GetServices(Type serviceType)
        {
            if (this.container.IsRegistered(serviceType))
            {
                return this.container.ResolveAll(serviceType);
            }

            return Enumerable.Empty<object>();
        }

        public IDependencyScope BeginScope()
        {
            var child = this.container.CreateChildContainer();
            return new UnityDependencyResolver(child);
        }

        public void Dispose()
        {
            this.container.Dispose();
        }
    }
}