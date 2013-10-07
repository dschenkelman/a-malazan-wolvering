namespace PhoneTicket.Web.Tests.Controllers.Api
{
    using System.Collections.Generic;
    using System.Threading.Tasks;

    using Microsoft.VisualStudio.TestTools.UnitTesting;

    using Moq;

    using PhoneTicket.Web.Controllers.Api;
    using PhoneTicket.Web.Models;
    using PhoneTicket.Web.Services;

    [TestClass]
    public class ComplexesControllerTests
    {
        private MockRepository repository;

        private Mock<IComplexService> complexService;

        [TestInitialize]
        public void Initialize()
        {
            this.repository = new MockRepository(MockBehavior.Default);
            this.complexService = this.repository.Create<IComplexService>();
        }

        [TestMethod]
        public async Task ShouldReturnComplexesFromComplexServiceWhenGetIsCalled()
        {
            var complexes = new List<Complex>();

            this.complexService.Setup(cs => cs.GetAsync()).Returns(Task.FromResult((IEnumerable<Complex>)complexes)).Verifiable();

            var controller = this.CreateController();

            var returnedComplexes = await controller.Get();

            Assert.AreSame(complexes, returnedComplexes);

            this.repository.Verify();
        }

        public ComplexesController CreateController()
        {
            return new ComplexesController(this.complexService.Object);
        }
    }
}
