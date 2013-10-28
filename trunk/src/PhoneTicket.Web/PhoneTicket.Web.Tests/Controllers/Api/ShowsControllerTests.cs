namespace PhoneTicket.Web.Tests.Controllers.Api
{
    using System.Collections;
    using System.Collections.Generic;
    using System.Threading.Tasks;

    using Microsoft.VisualStudio.TestTools.UnitTesting;

    using Moq;

    using PhoneTicket.Web.Controllers.Api;
    using PhoneTicket.Web.Models;
    using PhoneTicket.Web.Services;

    [TestClass]
    public class ShowsControllerTests
    {
        private MockRepository mockRepository;

        private Mock<IShowService> showService;

        private Mock<IRoomService> roomService;

        private Mock<IRoomXmlParser> roomXmlParser;

        [TestInitialize]
        public void Initialize()
        {
            this.mockRepository = new MockRepository(MockBehavior.Default);
            this.showService = this.mockRepository.Create<IShowService>();
            this.roomService = this.mockRepository.Create<IRoomService>();
            this.roomXmlParser = this.mockRepository.Create<IRoomXmlParser>();
        }

        [TestMethod]
        public async Task ShouldMarkOccupiedSeatsOnTopOfThoseDefinedByFile()
        {
            // arrange
            const int ShowId = 3;
            const int RoomId = 19;
            const string FileContent = "Content";

            var show = new Show { RoomId = RoomId };

            var room = new Room { Id = RoomId, File = FileContent };

            var showSeats = new ShowSeats();

            for (int i = 0; i < 17; i++)
            {
                for (int j = 0; j < 22; j++)
                {
                    if (j != 11 && j != 12)
                    {
                        showSeats.MarkFree(i + 1, j + 1);    
                    }
                }
            }

            var occupiedSeats = new List<OccupiedSeat>();

            for (int i = 0; i < 17; i++)
            {
                for (int j = 0; j < 22; j++)
                {
                    if (j != 11 && j != 12 && i % 2 == 0)
                    {
                        occupiedSeats.Add(new OccupiedSeat { Row = i + 1, Column = j + 1});
                    }
                }
            }

            this.showService.Setup(ss => ss.GetAsync(ShowId)).Returns(Task.FromResult(show)).Verifiable();

            this.roomService.Setup(rs => rs.GetAsync(RoomId)).Returns(Task.FromResult(room)).Verifiable();

            this.roomXmlParser.Setup(rxp => rxp.Parse(FileContent)).Returns(showSeats).Verifiable();

            this.showService.Setup(ss => ss.GetOccupiedSeats(ShowId))
                .Returns(Task.FromResult((IEnumerable<OccupiedSeat>)occupiedSeats));

            var controller = this.CreateController();

            // act
            var resultingSeats = await controller.Seats(ShowId);

            for (int i = 0; i < 17; i++)
            {
                for (int j = 0; j < 22; j++)
                {
                    if (j == 11 || j == 12)
                    {
                        Assert.AreEqual(SeatState.NoSeat, resultingSeats[i][j]);
                    }
                    else if (i % 2 == 0)
                    {
                        Assert.AreEqual(SeatState.Taken, resultingSeats[i][j]);
                    }
                    else
                    {
                        Assert.AreEqual(SeatState.Free, resultingSeats[i][j]);
                    }
                }
            }
        }

        public ShowsController CreateController()
        {
            return new ShowsController(this.showService.Object, this.roomService.Object, this.roomXmlParser.Object);
        }
    }
}
