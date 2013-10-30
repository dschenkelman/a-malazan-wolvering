namespace PhoneTicket.Web.Tests.Controllers.Api
{
    
    using Microsoft.VisualStudio.TestTools.UnitTesting;
    using Moq;
    using PhoneTicket.Web.Controllers.Api;
    using PhoneTicket.Web.Services;
    using PhoneTicket.Web.Models;
    
    using System;
    using System.Threading.Tasks;
    using System.Collections.Generic;
    using System.Linq;

    [TestClass]
    public class CreditCardsControllerTests
    {
        private MockRepository repository;

        private Mock<ICreditCardsService> creditCardsService;

        [TestInitialize]
        public void Initialize()
        {
            this.repository = new MockRepository(MockBehavior.Default);
            this.creditCardsService = this.repository.Create<ICreditCardsService>();
        }

        [TestMethod]
        public async Task ShouldRetrieveCreditCardCompaniesInformationWhenGetCreditCardCompaniesIsCalled()
        {
            const int CreditCardId1 = 1;
            const int CreditCardId2 = 2;
            const string CreditCardName1 = "CC1";
            const string CreditCardName2 = "CC2";

            var creditCardCompany1 = new CreditCardCompany { Id = CreditCardId1, Name = CreditCardName1 };
            var creditCardCompany2 = new CreditCardCompany { Id = CreditCardId2, Name = CreditCardName2 };

            var companies = new List<CreditCardCompany> { creditCardCompany1, creditCardCompany2};

            this.creditCardsService.Setup(ccs => ccs.GetAllAsync()).Returns(Task.FromResult((IEnumerable<CreditCardCompany>)companies)).Verifiable();

            var controller = this.CreateController();

            var response = await controller.GetCreditCardCompanies();

            Assert.AreEqual(2, response.Count());
            Assert.AreEqual(CreditCardId1, response.ElementAt(0).Id);
            Assert.AreEqual(CreditCardId2, response.ElementAt(1).Id);
            Assert.AreEqual(CreditCardName1, response.ElementAt(0).Name);
            Assert.AreEqual(CreditCardName2, response.ElementAt(1).Name);

            this.creditCardsService.Verify(ccs => ccs.GetAllAsync(), Times.Once());
        }

        public CreditCardsController CreateController()
        {
            return new CreditCardsController(this.creditCardsService.Object);
        }
    }
}
 /*
return await this.creditCardsService.GetAllAsync();
  */