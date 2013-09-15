﻿namespace PhoneTicket.Web.Tests
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
            this.container = new Mock<IUnityContainer>(MockBehavior.Strict);
        }

        [TestMethod]
        public void ShouldRegisterTypeMappingsInContainerWhenConfiguring()
        {
            var configurator = this.CreateConfigurator();

            this.container.Setup(c => c.RegisterType(typeof(ITemporaryUserService), typeof(TemporaryUserService), null, It.IsAny<LifetimeManager>())).Returns(this.container.Object).Verifiable();
            this.container.Setup(c => c.RegisterType(typeof(IUserService), typeof(UserService), null, It.IsAny<LifetimeManager>())).Returns(this.container.Object).Verifiable();
            this.container.Setup(c => c.RegisterType(null, typeof(PhoneTicketContext), null, It.IsAny<LifetimeManager>())).Returns(this.container.Object).Verifiable();
            this.container.Setup(c => c.RegisterType(null, typeof(UsersController), null, It.IsAny<LifetimeManager>())).Returns(this.container.Object).Verifiable();
            this.container.Setup(c => c.RegisterType(null, typeof(PhoneTicket.Web.Controllers.UsersController), null, It.IsAny<LifetimeManager>())).Returns(this.container.Object).Verifiable();
            this.container.Setup(c => c.RegisterInstance(typeof(IEmailService), null, It.IsAny<EmailService>(), It.IsAny<ContainerControlledLifetimeManager>())).Returns(this.container.Object).Verifiable();

            configurator.Configure(this.container.Object);

            this.container.Verify(c => c.RegisterType(typeof(IUserService), typeof(UserService), null, It.IsAny<LifetimeManager>()));
            this.container.Verify(c => c.RegisterType(typeof(ITemporaryUserService), typeof(TemporaryUserService), null, It.IsAny<LifetimeManager>()), Times.Once());
            this.container.Verify(c => c.RegisterType(null, typeof(PhoneTicketContext), null, It.IsAny<LifetimeManager>()), Times.Once());
            this.container.Verify(c => c.RegisterType(null, typeof(UsersController), null, It.IsAny<LifetimeManager>()), Times.Once());
            this.container.Verify(c => c.RegisterType(null, typeof(PhoneTicket.Web.Controllers.UsersController), null, It.IsAny<LifetimeManager>()), Times.Once());
            this.container.Verify(c => c.RegisterInstance(typeof(IEmailService), null, It.IsAny<EmailService>(), It.IsAny<ContainerControlledLifetimeManager>()), Times.Once());
        }

        private UnityResolverConfigurator CreateConfigurator()
        {
            return new UnityResolverConfigurator();
        }
    }
}