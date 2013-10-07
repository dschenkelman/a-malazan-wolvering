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
    using PhoneTicket.Web.Models;
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

            this.container.Setup(c => c.RegisterType(typeof(IUserService), typeof(UserService), null, It.IsAny<LifetimeManager>())).Returns(this.container.Object).Verifiable();
            this.container.Setup(c => c.RegisterType(typeof(ITemporaryUserService), typeof(TemporaryUserService), null, It.IsAny<LifetimeManager>())).Returns(this.container.Object).Verifiable();
            this.container.Setup(c => c.RegisterType(typeof(IMovieService), typeof(MovieService), null, It.IsAny<LifetimeManager>())).Returns(this.container.Object).Verifiable();
            this.container.Setup(c => c.RegisterType(typeof(IGenreService), typeof(GenreService), null, It.IsAny<LifetimeManager>())).Returns(this.container.Object).Verifiable();
            this.container.Setup(c => c.RegisterType(typeof(IRatingService), typeof(RatingService), null, It.IsAny<LifetimeManager>())).Returns(this.container.Object).Verifiable();
            this.container.Setup(c => c.RegisterInstance(typeof(IEmailService), null, It.IsAny<EmailService>(), It.IsAny<ContainerControlledLifetimeManager>())).Returns(this.container.Object).Verifiable();
            this.container.Setup(c => c.RegisterType(typeof(IGenreService), typeof(GenreService), null, It.IsAny<LifetimeManager>())).Returns(this.container.Object).Verifiable();
            this.container.Setup(c => c.RegisterType(typeof(IRatingService), typeof(RatingService), null, It.IsAny<LifetimeManager>())).Returns(this.container.Object).Verifiable();
            this.container.Setup(c => c.RegisterType(typeof(IComplexService), typeof(ComplexService), null, It.IsAny<LifetimeManager>())).Returns(this.container.Object).Verifiable();
            this.container.Setup(c => c.RegisterType(typeof(IRoomService), typeof(RoomService), null, It.IsAny<LifetimeManager>())).Returns(this.container.Object).Verifiable();

            this.container.Setup(c => c.RegisterType(null, typeof(PhoneTicketContext), null, It.IsAny<LifetimeManager>())).Returns(this.container.Object).Verifiable();
            this.container.Setup(c => c.RegisterType(typeof(IContext), typeof(PhoneTicketContext), null, It.IsAny<LifetimeManager>())).Returns(this.container.Object).Verifiable();
            this.container.Setup(c => c.RegisterType(null, typeof(UsersController), null, It.IsAny<LifetimeManager>())).Returns(this.container.Object).Verifiable();
            this.container.Setup(c => c.RegisterType(null, typeof(MoviesController), null, It.IsAny<LifetimeManager>())).Returns(this.container.Object).Verifiable();
            this.container.Setup(c => c.RegisterType(null, typeof(ComplexesController), null, It.IsAny<LifetimeManager>())).Returns(this.container.Object).Verifiable();
            this.container.Setup(c => c.RegisterType(null, typeof(PhoneTicket.Web.Controllers.UsersController), null, It.IsAny<LifetimeManager>())).Returns(this.container.Object).Verifiable();
            this.container.Setup(c => c.RegisterType(null, typeof(PhoneTicket.Web.Controllers.MoviesController), null, It.IsAny<LifetimeManager>())).Returns(this.container.Object).Verifiable();

            this.container.Setup(c => c.RegisterType(typeof(IRepository<User>), typeof(Repository<User>), null, It.IsAny<LifetimeManager>())).Returns(this.container.Object).Verifiable();
            this.container.Setup(c => c.RegisterType(typeof(IRepository<Genre>), typeof(Repository<Genre>), null, It.IsAny<LifetimeManager>())).Returns(this.container.Object).Verifiable();
            this.container.Setup(c => c.RegisterType(typeof(IRepository<Movie>), typeof(Repository<Movie>), null, It.IsAny<LifetimeManager>())).Returns(this.container.Object).Verifiable();
            this.container.Setup(c => c.RegisterType(typeof(IRepository<Rating>), typeof(Repository<Rating>), null, It.IsAny<LifetimeManager>())).Returns(this.container.Object).Verifiable();
            this.container.Setup(c => c.RegisterType(typeof(IRepository<TemporaryUser>), typeof(Repository<TemporaryUser>), null, It.IsAny<LifetimeManager>())).Returns(this.container.Object).Verifiable();
            this.container.Setup(c => c.RegisterType(typeof(IRepository<Complex>), typeof(Repository<Complex>), null, It.IsAny<LifetimeManager>())).Returns(this.container.Object).Verifiable();
            this.container.Setup(c => c.RegisterType(typeof(IRepository<Room>), typeof(Repository<Room>), null, It.IsAny<LifetimeManager>())).Returns(this.container.Object).Verifiable();
            this.container.Setup(c => c.RegisterType(typeof(IPhoneTicketRepositories), typeof(PhoneTicketRepositories), null, It.IsAny<LifetimeManager>())).Returns(this.container.Object).Verifiable();

            configurator.Configure(this.container.Object);

            this.container.Verify(c => c.RegisterType(typeof(IUserService), typeof(UserService), null, It.IsAny<LifetimeManager>()), Times.Once());
            this.container.Verify(c => c.RegisterType(typeof(ITemporaryUserService), typeof(TemporaryUserService), null, It.IsAny<LifetimeManager>()), Times.Once());
            this.container.Verify(c => c.RegisterType(typeof(IMovieService), typeof(MovieService), null, It.IsAny<LifetimeManager>()), Times.Once());
            this.container.Verify(c => c.RegisterType(typeof(IGenreService), typeof(GenreService), null, It.IsAny<LifetimeManager>()), Times.Once());
            this.container.Verify(c => c.RegisterType(typeof(IRatingService), typeof(RatingService), null, It.IsAny<LifetimeManager>()), Times.Once());
            this.container.Verify(c => c.RegisterInstance(typeof(IEmailService), null, It.IsAny<EmailService>(), It.IsAny<ContainerControlledLifetimeManager>()), Times.Once());
            this.container.Verify(c => c.RegisterType(null, typeof(PhoneTicketContext), null, It.IsAny<LifetimeManager>()), Times.Once());
            this.container.Verify(c => c.RegisterType(null, typeof(UsersController), null, It.IsAny<LifetimeManager>()), Times.Once());
            this.container.Verify(c => c.RegisterType(null, typeof(MoviesController), null, It.IsAny<LifetimeManager>()), Times.Once());
            this.container.Verify(c => c.RegisterType(null, typeof(PhoneTicket.Web.Controllers.UsersController), null, It.IsAny<LifetimeManager>()), Times.Once());
            this.container.Verify(c => c.RegisterType(null, typeof(PhoneTicket.Web.Controllers.MoviesController), null, It.IsAny<LifetimeManager>()), Times.Once());

            this.container.Verify(c => c.RegisterType(typeof(IRepository<User>), typeof(Repository<User>), null, It.IsAny<LifetimeManager>()), Times.Once());
            this.container.Verify(c => c.RegisterType(typeof(IRepository<Genre>), typeof(Repository<Genre>), null, It.IsAny<LifetimeManager>()), Times.Once());
            this.container.Verify(c => c.RegisterType(typeof(IRepository<Movie>), typeof(Repository<Movie>), null, It.IsAny<LifetimeManager>()), Times.Once());
            this.container.Verify(c => c.RegisterType(typeof(IRepository<Rating>), typeof(Repository<Rating>), null, It.IsAny<LifetimeManager>()), Times.Once());
            this.container.Verify(c => c.RegisterType(typeof(IRepository<TemporaryUser>), typeof(Repository<TemporaryUser>), null, It.IsAny<LifetimeManager>()), Times.Once());
            this.container.Verify(c => c.RegisterType(typeof(IPhoneTicketRepositories), typeof(PhoneTicketRepositories), null, It.IsAny<LifetimeManager>()), Times.Once());
        }

        private UnityResolverConfigurator CreateConfigurator()
        {
            return new UnityResolverConfigurator();
        }
    }
}