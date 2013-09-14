namespace PhoneTicket.Web.Tests
{
    using System;
    using System.Collections.Generic;

    using Microsoft.Practices.Unity;
    using Microsoft.VisualStudio.TestTools.UnitTesting;

    using Moq;

    using PhoneTicket.Web;
    using PhoneTicket.Web.Controllers.Api;
    using PhoneTicket.Web.Data;
    using PhoneTicket.Web.Services;

    [TestClass]
    public class UnityResolverConfiguratorTests
    {
        private Mock<IUnityContainer> container;

        [TestInitialize]
        public void Initialize()
        {
            this.container = new Mock<IUnityContainer>();
        }

        [TestMethod]
        public void ShouldRegisterTypeMappingsInContainerWhenConfiguring()
        {
            var mappings = new Dictionary<Type, Type>();
            var configurator = this.CreateConfigurator();

            this.container.Setup(r => r.RegisterType(typeof(ITemporaryUserService), typeof(TemporaryUserService), null, It.IsAny<LifetimeManager>())).Verifiable();
            this.container.Setup(r => r.RegisterType(null, typeof(PhoneTicketContext), null, It.IsAny<LifetimeManager>())).Verifiable();
            this.container.Setup(r => r.RegisterType(null, typeof(UsersController), null, It.IsAny<LifetimeManager>())).Verifiable();

            configurator.Configure(this.container.Object);

            this.container.Verify(r => r.RegisterType(typeof(ITemporaryUserService), typeof(TemporaryUserService), null, It.IsAny<LifetimeManager>()), Times.Once());
            this.container.Verify(r => r.RegisterType(null, typeof(PhoneTicketContext), null, It.IsAny<LifetimeManager>()), Times.Once());
            this.container.Verify(r => r.RegisterType(null, typeof(UsersController), null, It.IsAny<LifetimeManager>()), Times.Once());
        }

        private UnityResolverConfigurator CreateConfigurator()
        {
            return new UnityResolverConfigurator();
        }
    }
}