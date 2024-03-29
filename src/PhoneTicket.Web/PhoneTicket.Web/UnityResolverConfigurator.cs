﻿namespace PhoneTicket.Web
{
    using System.Web.Http.Dependencies;

    using Microsoft.Practices.Unity;

    using PhoneTicket.Web.Data;
    using PhoneTicket.Web.Models;
    using PhoneTicket.Web.Services;
    using PhoneTicket.Web.Helpers;

    public class UnityResolverConfigurator
    {
        public IDependencyResolver Configure(IUnityContainer container)
        {
            container.RegisterType<ITemporaryUserService, TemporaryUserService>();
            container.RegisterType<ISettingsService, SettingsService>();
            container.RegisterType<IUserService, UserService>();
            container.RegisterType<IMovieService, MovieService>();
            container.RegisterType<IGenreService, GenreService>();
            container.RegisterType<IRatingService, RatingService>();
            container.RegisterType<IComplexService, ComplexService>();
            container.RegisterType<IRoomService, RoomService>();
            container.RegisterType<IShowService, ShowService>();
            container.RegisterType<IDiscountService, DiscountService>();
            container.RegisterType<IOperationService, OperationService>();
            container.RegisterType<IOccupiedSeatsService, OccupiedSeatsService>();
            container.RegisterType<IOperationDiscountsService, OperationDiscountsService>();
            container.RegisterType<ICreditCardsService, CreditCardsService>();
            container.RegisterType<IRoomXmlParser, RoomXmlParser>();
            container.RegisterType<IReportService, ReportService>();

            container.RegisterType<IRepository<User>, Repository<User>>();
            container.RegisterType<IRepository<Genre>, Repository<Genre>>();
            container.RegisterType<IRepository<Movie>, Repository<Movie>>();
            container.RegisterType<IRepository<Rating>, Repository<Rating>>();
            container.RegisterType<IRepository<TemporaryUser>, Repository<TemporaryUser>>();
            container.RegisterType<IRepository<Complex>, Repository<Complex>>();
            container.RegisterType<IRepository<Room>, Repository<Room>>();
            container.RegisterType<IRepository<Show>, Repository<Show>>();
            container.RegisterType<IRepository<Operation>, Repository<Operation>>();
            container.RegisterType<IRepository<Discount>, Repository<Discount>>();
            container.RegisterType<IRepository<OccupiedSeat>, Repository<OccupiedSeat>>();
            container.RegisterType<IRepository<OperationDiscount>, Repository<OperationDiscount>>();
            container.RegisterType<IRepository<CreditCardCompany>, Repository<CreditCardCompany>>();
            container.RegisterType<IRepository<Setting>, Repository<Setting>>();

            container.RegisterType<IPhoneTicketRepositories, PhoneTicketRepositories>();

            container.RegisterType<PhoneTicket.Web.Controllers.Api.UsersController>();
            container.RegisterType<PhoneTicket.Web.Controllers.Api.MoviesController>();
            container.RegisterType<PhoneTicket.Web.Controllers.Api.ComplexesController>();
            container.RegisterType<PhoneTicket.Web.Controllers.Api.DiscountsController>();
            container.RegisterType<PhoneTicket.Web.Controllers.Api.CurrentUserController>();
            container.RegisterType<PhoneTicket.Web.Controllers.Api.ShowsController>();
            container.RegisterType<PhoneTicket.Web.Controllers.Api.ReservationsController>();
            container.RegisterType<PhoneTicket.Web.Controllers.Api.PurchasesController>();
            container.RegisterType<PhoneTicket.Web.Controllers.Api.CreditCardsController>();
            container.RegisterType<PhoneTicket.Web.Controllers.UsersController>();
            container.RegisterType<PhoneTicket.Web.Controllers.ReportsController>();
            container.RegisterType<PhoneTicket.Web.Controllers.MoviesController>();
            container.RegisterType<PhoneTicket.Web.Controllers.RoomsController>();
            container.RegisterType<PhoneTicket.Web.Controllers.ShowsController>();
            container.RegisterType<PhoneTicket.Web.Controllers.DiscountsController>();
            container.RegisterType<PhoneTicketContext>();
            container.RegisterType<IContext, PhoneTicketContext>();

            container.RegisterType<ICurrentUserRole, CurrentUserRole>();

            var emailService = new EmailService("smtp.gmail.com", 587, "wolveringticket@gmail.com", "wolvering", true, true);

            container.RegisterInstance<IEmailService>(emailService);

            var resolver = new UnityDependencyResolver(container);

            return resolver;
        }
    }
}
