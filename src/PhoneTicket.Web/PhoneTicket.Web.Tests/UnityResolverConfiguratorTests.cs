﻿namespace PhoneTicket.Web.Tests
{
    using Microsoft.Practices.Unity;
    using Microsoft.VisualStudio.TestTools.UnitTesting;

    using Moq;

    using PhoneTicket.Web;
    using PhoneTicket.Web.Controllers.Api;
    using PhoneTicket.Web.Data;
    using PhoneTicket.Web.Models;
    using PhoneTicket.Web.Services;
    using PhoneTicket.Web.Helpers;

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

            this.container.Setup(c => c.RegisterType(typeof(IShowService), typeof(ShowService), null, It.IsAny<LifetimeManager>())).Returns(this.container.Object).Verifiable();
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
            this.container.Setup(c => c.RegisterType(typeof(IDiscountService), typeof(DiscountService), null, It.IsAny<LifetimeManager>())).Returns(this.container.Object).Verifiable();
            this.container.Setup(c => c.RegisterType(typeof(IOperationService), typeof(OperationService), null, It.IsAny<LifetimeManager>())).Returns(this.container.Object).Verifiable();
            this.container.Setup(c => c.RegisterType(typeof(IOccupiedSeatsService), typeof(OccupiedSeatsService), null, It.IsAny<LifetimeManager>())).Returns(this.container.Object).Verifiable();
            this.container.Setup(c => c.RegisterType(typeof(IOperationDiscountsService), typeof(OperationDiscountsService), null, It.IsAny<LifetimeManager>())).Returns(this.container.Object).Verifiable();
            this.container.Setup(c => c.RegisterType(typeof(ICreditCardsService), typeof(CreditCardsService), null, It.IsAny<LifetimeManager>())).Returns(this.container.Object).Verifiable();
            this.container.Setup(c => c.RegisterType(typeof(ISettingsService), typeof(SettingsService), null, It.IsAny<LifetimeManager>())).Returns(this.container.Object).Verifiable();
            this.container.Setup(c => c.RegisterType(typeof(IReportService), typeof(ReportService), null, It.IsAny<LifetimeManager>())).Returns(this.container.Object).Verifiable();

            this.container.Setup(c => c.RegisterType(null, typeof(PhoneTicketContext), null, It.IsAny<LifetimeManager>())).Returns(this.container.Object).Verifiable();
            this.container.Setup(c => c.RegisterType(typeof(IContext), typeof(PhoneTicketContext), null, It.IsAny<LifetimeManager>())).Returns(this.container.Object).Verifiable();
            this.container.Setup(c => c.RegisterType(null, typeof(UsersController), null, It.IsAny<LifetimeManager>())).Returns(this.container.Object).Verifiable();
            this.container.Setup(c => c.RegisterType(null, typeof(DiscountsController), null, It.IsAny<LifetimeManager>())).Returns(this.container.Object).Verifiable();
            this.container.Setup(c => c.RegisterType(null, typeof(MoviesController), null, It.IsAny<LifetimeManager>())).Returns(this.container.Object).Verifiable();
            this.container.Setup(c => c.RegisterType(null, typeof(ComplexesController), null, It.IsAny<LifetimeManager>())).Returns(this.container.Object).Verifiable();
            this.container.Setup(c => c.RegisterType(null, typeof(CurrentUserController), null, It.IsAny<LifetimeManager>())).Returns(this.container.Object).Verifiable();
            this.container.Setup(c => c.RegisterType(null, typeof(ShowsController), null, It.IsAny<LifetimeManager>())).Returns(this.container.Object).Verifiable();
            this.container.Setup(c => c.RegisterType(null, typeof(ReservationsController), null, It.IsAny<LifetimeManager>())).Returns(this.container.Object).Verifiable();
            this.container.Setup(c => c.RegisterType(null, typeof(PurchasesController), null, It.IsAny<LifetimeManager>())).Returns(this.container.Object).Verifiable();
            this.container.Setup(c => c.RegisterType(null, typeof(CreditCardsController), null, It.IsAny<LifetimeManager>())).Returns(this.container.Object).Verifiable();
            this.container.Setup(c => c.RegisterType(null, typeof(PhoneTicket.Web.Controllers.UsersController), null, It.IsAny<LifetimeManager>())).Returns(this.container.Object).Verifiable();
            this.container.Setup(c => c.RegisterType(null, typeof(PhoneTicket.Web.Controllers.MoviesController), null, It.IsAny<LifetimeManager>())).Returns(this.container.Object).Verifiable();
            this.container.Setup(c => c.RegisterType(null, typeof(PhoneTicket.Web.Controllers.RoomsController), null, It.IsAny<LifetimeManager>())).Returns(this.container.Object).Verifiable();
            this.container.Setup(c => c.RegisterType(null, typeof(PhoneTicket.Web.Controllers.ShowsController), null, It.IsAny<LifetimeManager>())).Returns(this.container.Object).Verifiable();
            this.container.Setup(c => c.RegisterType(null, typeof(PhoneTicket.Web.Controllers.DiscountsController), null, It.IsAny<LifetimeManager>())).Returns(this.container.Object).Verifiable();
            this.container.Setup(c => c.RegisterType(null, typeof(PhoneTicket.Web.Controllers.ReportsController), null, It.IsAny<LifetimeManager>())).Returns(this.container.Object).Verifiable();

            this.container.Setup(c => c.RegisterType(typeof(ICurrentUserRole), typeof(CurrentUserRole), null, It.IsAny<LifetimeManager>())).Returns(this.container.Object).Verifiable();
            this.container.Setup(c => c.RegisterType(typeof(IRoomXmlParser), typeof(RoomXmlParser), null, It.IsAny<LifetimeManager>())).Returns(this.container.Object).Verifiable();

            this.container.Setup(c => c.RegisterType(typeof(IRepository<User>), typeof(Repository<User>), null, It.IsAny<LifetimeManager>())).Returns(this.container.Object).Verifiable();
            this.container.Setup(c => c.RegisterType(typeof(IRepository<Genre>), typeof(Repository<Genre>), null, It.IsAny<LifetimeManager>())).Returns(this.container.Object).Verifiable();
            this.container.Setup(c => c.RegisterType(typeof(IRepository<Movie>), typeof(Repository<Movie>), null, It.IsAny<LifetimeManager>())).Returns(this.container.Object).Verifiable();
            this.container.Setup(c => c.RegisterType(typeof(IRepository<Rating>), typeof(Repository<Rating>), null, It.IsAny<LifetimeManager>())).Returns(this.container.Object).Verifiable();
            this.container.Setup(c => c.RegisterType(typeof(IRepository<TemporaryUser>), typeof(Repository<TemporaryUser>), null, It.IsAny<LifetimeManager>())).Returns(this.container.Object).Verifiable();
            this.container.Setup(c => c.RegisterType(typeof(IRepository<Complex>), typeof(Repository<Complex>), null, It.IsAny<LifetimeManager>())).Returns(this.container.Object).Verifiable();
            this.container.Setup(c => c.RegisterType(typeof(IRepository<Room>), typeof(Repository<Room>), null, It.IsAny<LifetimeManager>())).Returns(this.container.Object).Verifiable();
            this.container.Setup(c => c.RegisterType(typeof(IRepository<Show>), typeof(Repository<Show>), null, It.IsAny<LifetimeManager>())).Returns(this.container.Object).Verifiable();
            this.container.Setup(c => c.RegisterType(typeof(IRepository<Discount>), typeof(Repository<Discount>), null, It.IsAny<LifetimeManager>())).Returns(this.container.Object).Verifiable();
            this.container.Setup(c => c.RegisterType(typeof(IRepository<Operation>), typeof(Repository<Operation>), null, It.IsAny<LifetimeManager>())).Returns(this.container.Object).Verifiable();
            this.container.Setup(c => c.RegisterType(typeof(IRepository<OccupiedSeat>), typeof(Repository<OccupiedSeat>), null, It.IsAny<LifetimeManager>())).Returns(this.container.Object).Verifiable();
            this.container.Setup(c => c.RegisterType(typeof(IRepository<OperationDiscount>), typeof(Repository<OperationDiscount>), null, It.IsAny<LifetimeManager>())).Returns(this.container.Object).Verifiable();
            this.container.Setup(c => c.RegisterType(typeof(IRepository<CreditCardCompany>), typeof(Repository<CreditCardCompany>), null, It.IsAny<LifetimeManager>())).Returns(this.container.Object).Verifiable();
            this.container.Setup(c => c.RegisterType(typeof(IRepository<Setting>), typeof(Repository<Setting>), null, It.IsAny<LifetimeManager>())).Returns(this.container.Object).Verifiable();
            this.container.Setup(c => c.RegisterType(typeof(IPhoneTicketRepositories), typeof(PhoneTicketRepositories), null, It.IsAny<LifetimeManager>())).Returns(this.container.Object).Verifiable();


            configurator.Configure(this.container.Object);

            this.container.Verify(c => c.RegisterType(typeof(IRoomXmlParser), typeof(RoomXmlParser), null, It.IsAny<LifetimeManager>()), Times.Once());
            this.container.Verify(c => c.RegisterType(typeof(IShowService), typeof(ShowService), null, It.IsAny<LifetimeManager>()), Times.Once());
            this.container.Verify(c => c.RegisterType(typeof(IUserService), typeof(UserService), null, It.IsAny<LifetimeManager>()), Times.Once());
            this.container.Verify(c => c.RegisterType(typeof(ITemporaryUserService), typeof(TemporaryUserService), null, It.IsAny<LifetimeManager>()), Times.Once());
            this.container.Verify(c => c.RegisterType(typeof(IMovieService), typeof(MovieService), null, It.IsAny<LifetimeManager>()), Times.Once());
            this.container.Verify(c => c.RegisterType(typeof(IGenreService), typeof(GenreService), null, It.IsAny<LifetimeManager>()), Times.Once());
            this.container.Verify(c => c.RegisterType(typeof(IRatingService), typeof(RatingService), null, It.IsAny<LifetimeManager>()), Times.Once());
            this.container.Verify(c => c.RegisterType(typeof(IRoomService), typeof(RoomService), null, It.IsAny<LifetimeManager>()), Times.Once());
            this.container.Verify(c => c.RegisterType(typeof(ISettingsService), typeof(SettingsService), null, It.IsAny<LifetimeManager>()), Times.Once());
            this.container.Verify(c => c.RegisterType(typeof(IReportService), typeof(ReportService), null, It.IsAny<LifetimeManager>()), Times.Once());
            this.container.Verify(c => c.RegisterInstance(typeof(IEmailService), null, It.IsAny<EmailService>(), It.IsAny<ContainerControlledLifetimeManager>()), Times.Once());
            this.container.Verify(c => c.RegisterType(null, typeof(PhoneTicketContext), null, It.IsAny<LifetimeManager>()), Times.Once());
            this.container.Verify(c => c.RegisterType(null, typeof(UsersController), null, It.IsAny<LifetimeManager>()), Times.Once());
            this.container.Verify(c => c.RegisterType(null, typeof(DiscountsController), null, It.IsAny<LifetimeManager>()), Times.Once());
            this.container.Verify(c => c.RegisterType(null, typeof(MoviesController), null, It.IsAny<LifetimeManager>()), Times.Once());
            this.container.Verify(c => c.RegisterType(null, typeof(ComplexesController), null, It.IsAny<LifetimeManager>()), Times.Once());
            this.container.Verify(c => c.RegisterType(null, typeof(PurchasesController), null, It.IsAny<LifetimeManager>()), Times.Once());
            this.container.Verify(c => c.RegisterType(null, typeof(PhoneTicket.Web.Controllers.UsersController), null, It.IsAny<LifetimeManager>()), Times.Once());
            this.container.Verify(c => c.RegisterType(null, typeof(PhoneTicket.Web.Controllers.MoviesController), null, It.IsAny<LifetimeManager>()), Times.Once());
            this.container.Verify(c => c.RegisterType(null, typeof(PhoneTicket.Web.Controllers.RoomsController), null, It.IsAny<LifetimeManager>()), Times.Once());
            this.container.Verify(c => c.RegisterType(null, typeof(PhoneTicket.Web.Controllers.ShowsController), null, It.IsAny<LifetimeManager>()), Times.Once());
            this.container.Verify(c => c.RegisterType(null, typeof(PhoneTicket.Web.Controllers.DiscountsController), null, It.IsAny<LifetimeManager>()), Times.Once());
            this.container.Verify(c => c.RegisterType(null, typeof(PhoneTicket.Web.Controllers.ReportsController), null, It.IsAny<LifetimeManager>()), Times.Once());

            this.container.Verify(c => c.RegisterType(typeof(IRepository<User>), typeof(Repository<User>), null, It.IsAny<LifetimeManager>()), Times.Once());
            this.container.Verify(c => c.RegisterType(typeof(IRepository<Genre>), typeof(Repository<Genre>), null, It.IsAny<LifetimeManager>()), Times.Once());
            this.container.Verify(c => c.RegisterType(typeof(IRepository<Movie>), typeof(Repository<Movie>), null, It.IsAny<LifetimeManager>()), Times.Once());
            this.container.Verify(c => c.RegisterType(typeof(IRepository<Rating>), typeof(Repository<Rating>), null, It.IsAny<LifetimeManager>()), Times.Once());
            this.container.Verify(c => c.RegisterType(typeof(IRepository<TemporaryUser>), typeof(Repository<TemporaryUser>), null, It.IsAny<LifetimeManager>()), Times.Once());
            this.container.Verify(c => c.RegisterType(typeof(IRepository<Room>), typeof(Repository<Room>), null, It.IsAny<LifetimeManager>()), Times.Once());
            this.container.Verify(c => c.RegisterType(typeof(IRepository<Show>), typeof(Repository<Show>), null, It.IsAny<LifetimeManager>()), Times.Once());

            this.container.Verify(c => c.RegisterType(typeof(IRepository<Discount>), typeof(Repository<Discount>), null, It.IsAny<LifetimeManager>()), Times.Once());
            this.container.Verify(c => c.RegisterType(typeof(IRepository<Operation>), typeof(Repository<Operation>), null, It.IsAny<LifetimeManager>()), Times.Once());
            this.container.Verify(c => c.RegisterType(typeof(IRepository<OccupiedSeat>), typeof(Repository<OccupiedSeat>), null, It.IsAny<LifetimeManager>()), Times.Once());
            this.container.Verify(c => c.RegisterType(typeof(IRepository<Setting>), typeof(Repository<Setting>), null, It.IsAny<LifetimeManager>()), Times.Once());
            this.container.Verify(c => c.RegisterType(typeof(IPhoneTicketRepositories), typeof(PhoneTicketRepositories), null, It.IsAny<LifetimeManager>()), Times.Once());
        }

        private UnityResolverConfigurator CreateConfigurator()
        {
            return new UnityResolverConfigurator();
        }
    }
}