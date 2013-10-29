namespace PhoneTicket.Web.Tests.Controllers.Api
{
    using System;
    using System.Collections;
    using System.Collections.Generic;
    using System.Collections.ObjectModel;
    using System.Linq;
    using System.Linq.Expressions;
    using System.Threading.Tasks;
    using System.Web.Http.Results;

    using Microsoft.VisualStudio.TestTools.UnitTesting;

    using Moq;

    using PhoneTicket.Web.Controllers.Api;
    using PhoneTicket.Web.Models;
    using PhoneTicket.Web.Services;
    using PhoneTicket.Web.ViewModels.Api;

    [TestClass]
    public class DiscountsControllerTests
    {
        private MockRepository mockRepository;

        private Mock<IDiscountService> discountService;

        private Mock<IOperationService> operationService;

        [TestInitialize]
        public void TestInitialize()
        {
            this.mockRepository = new MockRepository(MockBehavior.Default);
            this.discountService = this.mockRepository.Create<IDiscountService>();
            this.operationService = this.mockRepository.Create<IOperationService>();
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
            this.discountService.Setup(ds => ds.GetActiveAsync()).Returns(Task.FromResult((IEnumerable<Discount>)discounts)).Verifiable();

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

        [TestMethod]
        public async Task ShouldReturnBadRequestWhenSetDiscountsForAnInexistentOperationIsCalled()
        {
            this.operationService
                .Setup(os => os.GetAsync(It.IsAny<Expression<Func<Operation, bool>>>()))
                .Returns(Task.FromResult(Enumerable.Empty<Operation>()))
                .Verifiable();

            var controller = this.CreateController();

            var result = (BadRequestErrorMessageResult)await controller.SetDiscounts(2, Enumerable.Empty<DiscountForOperationViewModel>());

            Assert.AreEqual("La operación no existe", result.Message);
        }

        [TestMethod]
        public async Task ShouldReturnBadRequestWhenDiscountTicketsAreGreaterThatOccupiedSeatsForOperation()
        {
            var operation = new Operation
                                {
                                    OccupiedSeats =
                                        new Collection<OccupiedSeat>
                                            {
                                                new OccupiedSeat(),
                                                new OccupiedSeat(),
                                                new OccupiedSeat(),
                                                new OccupiedSeat(),
                                                new OccupiedSeat()
                                            }
                                };

            this.operationService.Setup(os => os.GetAsync(It.IsAny<Expression<Func<Operation, bool>>>()))
                .Returns(Task.FromResult((IEnumerable<Operation>)new List<Operation> { operation }))
                .Verifiable();

            var discountsForOperation = new[]
                                            {
                                                new DiscountForOperationViewModel() { Count = 2, DiscountId = 1 },
                                                new DiscountForOperationViewModel() { Count = 1, DiscountId = 2 },
                                                new DiscountForOperationViewModel() { Count = 1, DiscountId = 3 }
                                            };

            var discounts = new[]
                                {
                                    new Discount { Type = DiscountType.TwoForOne, Id = 1 },
                                    new Discount { Type = DiscountType.Percentage, Id = 2 },
                                    new Discount { Type = DiscountType.FixedPrice, Id = 3 }
                                };

            this.discountService.Setup(ds => ds.GetByIdsAsync(It.IsAny<int[]>())).Callback<int[]>(a =>
            {
                Assert.AreEqual(3, a.Length);
                Assert.AreEqual(1, a[0]);
                Assert.AreEqual(2, a[1]);
                Assert.AreEqual(3, a[2]);
            }).Returns(Task.FromResult((IEnumerable<Discount>)discounts)).Verifiable();

            var controller = this.CreateController();

            var result = (BadRequestErrorMessageResult)await controller.SetDiscounts(2, discountsForOperation);

            Assert.AreEqual("La cantidad de asientos de la promociones es mayor a la cantidad de asientos reservados", result.Message);

            this.mockRepository.VerifyAll();
        }

        [TestMethod]
        public async Task ShouldAddDiscountsWhenSetDiscountsIsCalled()
        {
            var operation = new Operation
            {
                OccupiedSeats =
                    new Collection<OccupiedSeat>
                                            {
                                                new OccupiedSeat(),
                                                new OccupiedSeat(),
                                                new OccupiedSeat(),
                                                new OccupiedSeat(),
                                                new OccupiedSeat(),
                                                new OccupiedSeat()
                                            }
            };

            this.operationService.Setup(os => os.GetAsync(It.IsAny<Expression<Func<Operation, bool>>>()))
                .Returns(Task.FromResult((IEnumerable<Operation>)new List<Operation> { operation }))
                .Verifiable();

            var discountsForOperation = new[]
                                            {
                                                new DiscountForOperationViewModel() { Count = 2, DiscountId = 1 },
                                                new DiscountForOperationViewModel() { Count = 1, DiscountId = 2 },
                                                new DiscountForOperationViewModel() { Count = 1, DiscountId = 3 }
                                            };

            var discounts = new[]
                                {
                                    new Discount { Type = DiscountType.TwoForOne, Id = 1 },
                                    new Discount { Type = DiscountType.Percentage, Id = 2 },
                                    new Discount { Type = DiscountType.FixedPrice, Id = 3 }
                                };

            this.discountService.Setup(ds => ds.GetByIdsAsync(It.IsAny<int[]>())).Callback<int[]>(a =>
            {
                Assert.AreEqual(3, a.Length);
                Assert.AreEqual(1, a[0]);
                Assert.AreEqual(2, a[1]);
                Assert.AreEqual(3, a[2]);
            }).Returns(Task.FromResult((IEnumerable<Discount>)discounts)).Verifiable();

            this.operationService.Setup(os => os.AddDiscountsAsync(operation, discountsForOperation))
                .Returns(Task.FromResult<object>(null))
                .Verifiable();

            var controller = this.CreateController();

            var result = await controller.SetDiscounts(2, discountsForOperation);

            Assert.IsInstanceOfType(result, typeof(OkResult));

            this.mockRepository.VerifyAll();
        }

        private DiscountsController CreateController()
        {
            return new DiscountsController(this.discountService.Object, this.operationService.Object);
        }
    }
}
