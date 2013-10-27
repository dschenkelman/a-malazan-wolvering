namespace PhoneTicket.Web.Tests.Controllers.Api
{
    using System.Collections.Generic;
    using System.Linq;
    using System.Threading.Tasks;

    using Microsoft.VisualStudio.TestTools.UnitTesting;

    using Moq;

    using PhoneTicket.Web.Controllers.Api;
    using PhoneTicket.Web.Models;
    using PhoneTicket.Web.Services;

    [TestClass]
    public class DiscountsControllerTests
    {
        private MockRepository mockRepository;

        private Mock<IDiscountService> discountService;

        [TestInitialize]
        public void TestInitialize()
        {
            this.mockRepository = new MockRepository(MockBehavior.Default);
            this.discountService = this.mockRepository.Create<IDiscountService>();
        }

        [TestMethod]
        public async Task ShouldReturnListOfViewModelInstancesWithDiscountInformationWhenCallingGet()
        {
            var discounts = new List<Discount>
                                                  {
                                                      new Discount
                                                          {
                                                              Description = "D1",
                                                              Id = 1,
                                                              Type = DiscountType.FixedPrice,
                                                              Value = 30
                                                          },
                                                      new Discount
                                                          {
                                                              Description = "D2",
                                                              Id = 2,
                                                              Type = DiscountType.Percentage,
                                                              Value = 0.5
                                                          },
                                                      new Discount
                                                          {
                                                              Description = "D3",
                                                              Id = 3,
                                                              Type = DiscountType.TwoForOne
                                                          },
                                                  };
            this.discountService.Setup(ds => ds.GetActive()).Returns(Task.FromResult((IEnumerable<Discount>)discounts)).Verifiable();

            var controller = this.CreateController();

            var discountViewModels = await controller.Get();

            Assert.AreEqual(3, discountViewModels.Count());

            var viewModel1 = discountViewModels.ElementAt(0);

            Assert.AreEqual(discounts[0].Id, viewModel1.Id);
            Assert.AreEqual(discounts[0].Type, viewModel1.Type);
            Assert.AreEqual(discounts[0].Description, viewModel1.Description);
            Assert.AreEqual(discounts[0].Value, viewModel1.Value);

            var viewModel2 = discountViewModels.ElementAt(1);

            Assert.AreEqual(discounts[1].Id, viewModel2.Id);
            Assert.AreEqual(discounts[1].Type, viewModel2.Type);
            Assert.AreEqual(discounts[1].Description, viewModel2.Description);
            Assert.AreEqual(discounts[1].Value, viewModel2.Value);

            var viewModel3 = discountViewModels.ElementAt(2);

            Assert.AreEqual(discounts[2].Id, viewModel3.Id);
            Assert.AreEqual(discounts[2].Type, viewModel3.Type);
            Assert.AreEqual(discounts[2].Description, viewModel3.Description);
            Assert.AreEqual(discounts[2].Value, viewModel3.Value);

            this.mockRepository.VerifyAll();
        }

        private DiscountsController CreateController()
        {
            return new DiscountsController(this.discountService.Object);
        }
    }
}
