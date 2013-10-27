namespace PhoneTicket.Web.Tests.Controllers
{
    using System;
    using System.Collections.Generic;
    using System.Globalization;
    using System.Linq.Expressions;
    using System.Threading.Tasks;
    using System.Web.Mvc;

    using Microsoft.VisualStudio.TestTools.UnitTesting;

    using Moq;

    using PagedList;

    using PhoneTicket.Web.Controllers;
    using PhoneTicket.Web.Helpers;
    using PhoneTicket.Web.Models;
    using PhoneTicket.Web.Services;
    using PhoneTicket.Web.ViewModels;

    [TestClass]
    public class DiscountsControllerTests
    {
        private MockRepository mockRepository;

        private Mock<IDiscountService> discountService;

        private Mock<ICurrentUserRole> currentUserRole;

        [TestInitialize]
        public void Initialize()
        {
            this.mockRepository = new MockRepository(MockBehavior.Default);
            this.discountService = this.mockRepository.Create<IDiscountService>();
            this.currentUserRole = this.mockRepository.Create<ICurrentUserRole>();
        }

        [TestMethod]
        public async Task ShouldReturnItemsOnPageOneWhenIndexIsCalledIfNoPageIsProvided()
        {
            var discounts = new List<Discount>();
            const int Items = 20;

            for (int i = 0; i < Items; i++)
            {
                discounts.Add(
                    new Discount()
                    {
                        Id = i,
                        Description = i.ToString(CultureInfo.InvariantCulture),
                        StartDate = DateTimeHelpers.DateTimeInArgentina.AddDays(i),
                        EndDate = DateTimeHelpers.DateTimeInArgentina.AddDays(i + 10),
                        Value = i,
                        Type = DiscountType.FixedPrice
                    });
            }

            this.discountService.Setup(ds => ds.GetActiveAndFutureAsync()).Returns(Task.FromResult((IEnumerable<Discount>)discounts)).Verifiable();

            var controller = this.CreateController();

            var result = (ViewResult)await controller.Index(string.Empty, null);

            var pagedList = (IPagedList<ListDiscountViewModel>)result.ViewData.Model;

            var pages = Math.Ceiling((double)Items / pagedList.PageSize);

            Assert.AreEqual(Items, pagedList.TotalItemCount);
            Assert.IsTrue(pagedList.HasNextPage);
            Assert.IsFalse(pagedList.HasPreviousPage);
            Assert.IsTrue(pagedList.IsFirstPage);
            Assert.AreEqual(pages, pagedList.PageCount);
        }

        [TestMethod]
        public async Task ShouldReturnItemsOnPageNumberWhenIndexIsCalledIfWithPageNumberIsProvided()
        {
            var discounts = new List<Discount>();
            const int Items = 30;
            const int PageNumber = 3;

            for (int i = 0; i < Items; i++)
            {
                discounts.Add(
                    new Discount
                    {
                        Id = i,
                        Description = i.ToString(CultureInfo.InvariantCulture),
                        StartDate = DateTimeHelpers.DateTimeInArgentina.AddDays(i),
                        EndDate = DateTimeHelpers.DateTimeInArgentina.AddDays(i + 10),
                        Value = i,
                        Type = DiscountType.FixedPrice
                    });
            }

            this.discountService.Setup(ds => ds.GetActiveAndFutureAsync()).Returns(Task.FromResult((IEnumerable<Discount>)discounts)).Verifiable();

            var controller = this.CreateController();

            var result = (ViewResult)await controller.Index(string.Empty, PageNumber);

            var pagedList = (IPagedList<ListDiscountViewModel>)result.ViewData.Model;

            var pages = Math.Ceiling((double)Items / pagedList.PageSize);

            Assert.AreEqual(Items, pagedList.TotalItemCount);
            Assert.IsFalse(pagedList.HasNextPage);
            Assert.IsTrue(pagedList.HasPreviousPage);
            Assert.AreEqual(PageNumber, pagedList.PageNumber);
            Assert.AreEqual(pages, pagedList.PageCount);
        }

        [TestMethod]
        public async Task ShouldProvideDescriptionFilterToUserServiceIfEmailSearchIsProvided()
        {
            var discounts = new List<Discount>();
            const int PageNumber = 1;

            var discountMatches = new Discount() { Description = "50% OFF" };
            var discountDoesNotMatch = new Discount() { Description = "-$15" };

            this.discountService.Setup(
                ds =>
                ds.GetActiveAndFutureAsync(It.IsAny<Expression<Func<Discount, bool>>>()))
                .Callback<Expression<Func<Discount, bool>>>(e =>
                    {
                        var func = e.Compile();

                        Assert.IsTrue(func.Invoke(discountMatches));
                        Assert.IsFalse(func.Invoke(discountDoesNotMatch));
                    })
                .Returns(Task.FromResult((IEnumerable<Discount>)discounts)).Verifiable();

            var controller = this.CreateController();

            await controller.Index("OFF", PageNumber);

            this.discountService.VerifyAll();
        }

        [TestMethod]
        public async Task ShouldCorrectlySetViewModelPropertiesFromDiscountWhenIndexIsCalled()
        {
            var discount1 = new Discount()
            {
                Description = "D1",
                Id = 1,
                StartDate = DateTimeHelpers.DateTimeInArgentina.AddDays(1),
                EndDate = DateTimeHelpers.DateTimeInArgentina.AddDays(2),
                Type = DiscountType.TwoForOne
            };

            var discount2 = new Discount()
            {
                Description = "D2",
                Id = 2,
                StartDate = DateTimeHelpers.DateTimeInArgentina.AddDays(5),
                EndDate = DateTimeHelpers.DateTimeInArgentina.AddDays(6),
                Type = DiscountType.Percentage,
                Value = 0.1
            };

            var discount3 = new Discount()
                               {
                                   Description = "D3",
                                   Id = 3,
                                   StartDate = DateTimeHelpers.DateTimeInArgentina,
                                   EndDate = DateTimeHelpers.DateTimeInArgentina.AddDays(1),
                                   Type = DiscountType.FixedPrice,
                                   Value = 10
                               };

            var discounts = new List<Discount> { discount1, discount2, discount3 };
            const int PageNumber = 1;

            this.discountService.Setup(ds => ds.GetActiveAndFutureAsync()).Returns(Task.FromResult((IEnumerable<Discount>)discounts)).Verifiable();

            var controller = this.CreateController();

            var result = (ViewResult)await controller.Index(string.Empty, PageNumber);

            var pagedList = (IPagedList<ListDiscountViewModel>)result.ViewData.Model;

            var discount1ViewModel = pagedList[0];
            var discount2ViewModel = pagedList[1];
            var discount3ViewModel = pagedList[2];

            Assert.AreEqual(discount1ViewModel.Id, discount1.Id);
            Assert.AreEqual(discount1ViewModel.Description, discount1.Description);
            Assert.AreEqual(discount1ViewModel.StartDate, discount1.StartDate.ToString("yyyy-MM-dd"));
            Assert.AreEqual(discount1ViewModel.EndDate, discount1.EndDate.ToString("yyyy-MM-dd"));
            Assert.AreEqual(discount1ViewModel.Value, string.Empty);
            Assert.AreEqual(discount1ViewModel.Type, discount1.Type.InSpanish());

            Assert.AreEqual(discount2ViewModel.Id, discount2.Id);
            Assert.AreEqual(discount2ViewModel.Description, discount2.Description);
            Assert.AreEqual(discount2ViewModel.StartDate, discount2.StartDate.ToString("yyyy-MM-dd"));
            Assert.AreEqual(discount2ViewModel.EndDate, discount2.EndDate.ToString("yyyy-MM-dd"));
            Assert.AreEqual(discount2ViewModel.Value, (discount2.Value * 100) + "%");
            Assert.AreEqual(discount2ViewModel.Type, discount2.Type.InSpanish());

            Assert.AreEqual(discount3ViewModel.Id, discount3.Id);
            Assert.AreEqual(discount3ViewModel.Description, discount3.Description);
            Assert.AreEqual(discount3ViewModel.StartDate, discount3.StartDate.ToString("yyyy-MM-dd"));
            Assert.AreEqual(discount3ViewModel.EndDate, discount3.EndDate.ToString("yyyy-MM-dd"));
            Assert.AreEqual(discount3ViewModel.Value, "$" + discount3.Value);
            Assert.AreEqual(discount3ViewModel.Type, discount3.Type.InSpanish());
        }

        [TestMethod]
        public async Task ShouldNotAllowNonAdminToEditWhenCallingIndex()
        {
            var discount1 = new Discount()
            {
                Description = "D1",
                Id = 1,
                StartDate = DateTimeHelpers.DateTimeInArgentina.AddDays(1),
                EndDate = DateTimeHelpers.DateTimeInArgentina.AddDays(2),
                Type = DiscountType.TwoForOne
            };

            this.currentUserRole.Setup(cur => cur.IsAdmin()).Returns(false).Verifiable();

            var discounts = new List<Discount> { discount1 };
            const int PageNumber = 1;

            this.discountService.Setup(ds => ds.GetActiveAndFutureAsync()).Returns(Task.FromResult((IEnumerable<Discount>)discounts)).Verifiable();

            var controller = this.CreateController();

            var result = (ViewResult)await controller.Index(string.Empty, PageNumber);

            var pagedList = (IPagedList<ListDiscountViewModel>)result.ViewData.Model;

            var discount1ViewModel = pagedList[0];

            Assert.IsFalse(discount1ViewModel.CanEdit);

            this.mockRepository.VerifyAll();
        }

        [TestMethod]
        public async Task ShouldNotAllowEditionOfDiscountWhenCallingEditIfItIsNotInTheFuture()
        {
            var discount1 = new Discount()
            {
                Description = "D1",
                Id = 1,
                StartDate = DateTimeHelpers.DateTimeInArgentina.AddDays(-1),
                EndDate = DateTimeHelpers.DateTimeInArgentina.AddDays(2),
                Type = DiscountType.TwoForOne
            };

            this.currentUserRole.Setup(cur => cur.IsAdmin()).Returns(true).Verifiable();

            var discounts = new List<Discount> { discount1 };
            const int PageNumber = 1;

            this.discountService.Setup(ds => ds.GetActiveAndFutureAsync()).Returns(Task.FromResult((IEnumerable<Discount>)discounts)).Verifiable();

            var controller = this.CreateController();

            var result = (ViewResult)await controller.Index(string.Empty, PageNumber);

            var pagedList = (IPagedList<ListDiscountViewModel>)result.ViewData.Model;

            var discount1ViewModel = pagedList[0];

            Assert.IsFalse(discount1ViewModel.CanEdit);

            this.mockRepository.VerifyAll();
        }


        [TestMethod]
        public async Task ShouldAllowEditionOfDiscountWhenCallingEditIfItIsInTheFutureAndAdmin()
        {
            var discount1 = new Discount()
            {
                Description = "D1",
                Id = 1,
                StartDate = DateTimeHelpers.DateTimeInArgentina.AddDays(1),
                EndDate = DateTimeHelpers.DateTimeInArgentina.AddDays(2),
                Type = DiscountType.TwoForOne
            };

            this.currentUserRole.Setup(cur => cur.IsAdmin()).Returns(true).Verifiable();

            var discounts = new List<Discount> { discount1 };
            const int PageNumber = 1;

            this.discountService.Setup(ds => ds.GetActiveAndFutureAsync()).Returns(Task.FromResult((IEnumerable<Discount>)discounts)).Verifiable();

            var controller = this.CreateController();

            var result = (ViewResult)await controller.Index(string.Empty, PageNumber);

            var pagedList = (IPagedList<ListDiscountViewModel>)result.ViewData.Model;

            var discount1ViewModel = pagedList[0];

            Assert.IsTrue(discount1ViewModel.CanEdit);

            this.mockRepository.VerifyAll();
        }

        private DiscountsController CreateController()
        {
            return new DiscountsController(this.discountService.Object, this.currentUserRole.Object);
        }
    }
}
