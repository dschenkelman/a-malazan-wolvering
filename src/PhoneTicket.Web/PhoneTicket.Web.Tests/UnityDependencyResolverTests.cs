namespace PhoneTicket.Web.Tests
{
    using System;
    using System.Linq;
    using System.Reflection;

    using Microsoft.Practices.ObjectBuilder2;
    using Microsoft.Practices.Unity;
    using Microsoft.VisualStudio.TestTools.UnitTesting;

    using Moq;

    using PhoneTicket.Web;

    [TestClass]
    public class UnityDependencyResolverTests
    {
        private Mock<IUnityContainer> container;

        [TestInitialize]
        public void Initialize()
        {
            this.container = new Mock<IUnityContainer>();
        }

        [TestMethod]
        public void ShouldReturnNullIfTypeIsNotRegisteredWhenCallingGetService()
        {
            var resolver = this.CreateResolver();

            this.container.Setup(c => c.Registrations).Returns(Enumerable.Empty<ContainerRegistration>()).Verifiable();

            Assert.IsNull(resolver.GetService(typeof(string)));

            this.container.Verify(c => c.Registrations, Times.Once);
        }

        [TestMethod]
        public void ShouldReturnNullIfTypeIsNotRegisteredWhenCallingGetServices()
        {
            var resolver = this.CreateResolver();

            this.container.Setup(c => c.Registrations).Returns(Enumerable.Empty<ContainerRegistration>()).Verifiable();

            Assert.AreEqual(0, resolver.GetServices(typeof(string)).Count());

            this.container.Verify(c => c.Registrations, Times.Once);
        }

        [TestMethod]
        public void ShouldResolveValueFromContainerIfTypeIsRegisteredWhenGettingService()
        {
            var resolver = this.CreateResolver();
            
            this.container.Setup(c => c.Registrations).Returns(new[] { this.CreateRegistration(typeof(string)) }).Verifiable();
            this.container.Setup(c => c.Resolve(typeof(string), It.IsAny<string>())).Returns("ResolvedValue").Verifiable();

            Assert.AreEqual("ResolvedValue", resolver.GetService(typeof(string)));

            this.container.Verify(c => c.Registrations, Times.Once());
            this.container.Verify(c => c.Resolve(typeof(string), It.IsAny<string>()), Times.Once());
        }

        [TestMethod]
        public void ShouldResolveValuesFromContainerIfTypeIsRegisteredWhenGettingServices()
        {
            var resolver = this.CreateResolver();

            this.container.Setup(c => c.Registrations).Returns(new[] { this.CreateRegistration(typeof(string)) }).Verifiable();
            this.container.Setup(c => c.ResolveAll(typeof(string))).Returns(new[] { "ResolvedValue", "ResolvedValue2" }).Verifiable();

            var values = resolver.GetServices(typeof(string)).ToList();

            this.container.Verify(c => c.Registrations, Times.Once());
            this.container.Verify(c => c.ResolveAll(typeof(string)), Times.Once());

            Assert.AreEqual(2, values.Count);
            Assert.AreEqual("ResolvedValue", values[0]);
            Assert.AreEqual("ResolvedValue2", values[1]);
        }

        private ContainerRegistration CreateRegistration(Type registeredType)
        {
            var constructorInfo = typeof(ContainerRegistration).GetConstructors(BindingFlags.NonPublic | BindingFlags.Instance).First();

            return (ContainerRegistration)constructorInfo.Invoke(new object[] { registeredType, string.Empty, new Mock<IPolicyList>().Object });
        }

        private UnityDependencyResolver CreateResolver()
        {
            return new UnityDependencyResolver(this.container.Object);
        }
    }
}