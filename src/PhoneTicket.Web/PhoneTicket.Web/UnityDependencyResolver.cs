namespace PhoneTicket.Web
{
    using System;
    using System.Collections.Generic;
    using System.Linq;
    using System.Web.Http.Dependencies;

    using Microsoft.Practices.ServiceLocation;
    using Microsoft.Practices.Unity;

    public class UnityDependencyResolver : IDependencyResolver, IServiceLocator
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

        public object GetInstance(Type serviceType)
        {
            return this.GetService(serviceType);
        }

        public object GetInstance(Type serviceType, string key)
        {
            return this.container.Resolve(serviceType, key);
        }

        public IEnumerable<object> GetAllInstances(Type serviceType)
        {
            return this.GetServices(serviceType);
        }

        public TService GetInstance<TService>()
        {
            return (TService)this.GetInstance(typeof(TService));
        }

        public TService GetInstance<TService>(string key)
        {
            return (TService)this.GetInstance(typeof(TService), key);
        }

        public IEnumerable<TService> GetAllInstances<TService>()
        {
            return this.GetAllInstances(typeof(TService)).Cast<TService>();
        }
    }
}