namespace PhoneTicket.Web.Tests.Services
{
    using System;
    using System.Linq;

    using Microsoft.VisualStudio.TestTools.UnitTesting;

    using PhoneTicket.Web.Models;
    using PhoneTicket.Web.Properties;
    using PhoneTicket.Web.Services;

    [TestClass]
    public class RoomXmlParserTests
    {
        [TestMethod]
        public void ShouldReturnErrorIfThereIsNoRootElement()
        {
            const string Xml = "";

            var parser = this.CreateParser();

            var errors = parser.Validate(Xml);

            Assert.AreEqual(1, errors.Count());

            var error = errors.ElementAt(0);

            Assert.AreEqual(0, error.Line);
            Assert.AreEqual(Resources.ErrorParsingContent, error.Message);
        }

        [TestMethod]
        public void ShouldReturnErrorIfRootElementIsNotSala()
        {
            const string Xml = "<Fila></Fila>";

            var parser = this.CreateParser();

            var errors = parser.Validate(Xml);

            Assert.AreEqual(1, errors.Count());

            var error = errors.ElementAt(0);

            Assert.AreEqual(1, error.Line);
            Assert.AreEqual(Resources.InvalidRootElement, error.Message);
        }

        [TestMethod]
        public void ShouldReturnErrorIfElementOtherThanFilaIsChildOfSala()
        {
           string xml = 
               "<Sala>" + Environment.NewLine +
               "<Fila></Fila>" + Environment.NewLine +
               "<Columna></Columna>" + Environment.NewLine +
               "<Columna></Columna>" + Environment.NewLine +
               "<Fila></Fila>" + Environment.NewLine +
                "</Sala>";

            var parser = this.CreateParser();

            var errors = parser.Validate(xml).Where(e => e.Message == Resources.InvalidRoomChildElement);

            Assert.AreEqual(2, errors.Count());

            var error1 = errors.ElementAt(0);

            Assert.AreEqual(3, error1.Line);

            var error2 = errors.ElementAt(1);

            Assert.AreEqual(4, error2.Line);
        }

        [TestMethod]
        public void ShouldReturnErrorIfNumeroAttributeIsNotPresentInFila()
        {
            string xml =
                "<Sala>" + Environment.NewLine +
                "<Fila></Fila>" + Environment.NewLine +
                "<Fila></Fila>" + Environment.NewLine +
                 "</Sala>";

            var parser = this.CreateParser();

            var errors = parser.Validate(xml).Where(e => e.Message == Resources.RowNumberIsRequired);

            Assert.AreEqual(2, errors.Count());

            var error1 = errors.ElementAt(0);

            Assert.AreEqual(2, error1.Line);

            var error2 = errors.ElementAt(1);

            Assert.AreEqual(3, error2.Line);
        }

        [TestMethod]
        public void ShouldReturnErrorIfDesdeAttributeIsNotPresentInFila()
        {
            string xml =
                "<Sala>" + Environment.NewLine +
                "<Fila></Fila>" + Environment.NewLine +
                "<Fila></Fila>" + Environment.NewLine +
                 "</Sala>";

            var parser = this.CreateParser();

            var errors = parser.Validate(xml).Where(e => e.Message == Resources.FromAttributeIsRequired);

            Assert.AreEqual(2, errors.Count());

            var error1 = errors.ElementAt(0);

            Assert.AreEqual(2, error1.Line);

            var error2 = errors.ElementAt(1);

            Assert.AreEqual(3, error2.Line);
        }


        [TestMethod]
        public void ShouldReturnErrorIfHastaAttributeIsNotPresentInFila()
        {
            string xml =
                "<Sala>" + Environment.NewLine +
                "<Fila></Fila>" + Environment.NewLine +
                "<Fila></Fila>" + Environment.NewLine +
                 "</Sala>";

            var parser = this.CreateParser();

            var errors = parser.Validate(xml).Where(e => e.Message == Resources.ToAttributeIsRequired);

            Assert.AreEqual(2, errors.Count());

            var error1 = errors.ElementAt(0);

            Assert.AreEqual(2, error1.Line);

            var error2 = errors.ElementAt(1);

            Assert.AreEqual(3, error2.Line);
        }

        [TestMethod]
        public void ShouldReturnErrorIfNumeroIsNotBetween1And17()
        {
            string xml =
                "<Sala>" + Environment.NewLine +
                "<Fila numero='0'></Fila>" + Environment.NewLine +
                "<Fila numero='18'></Fila>" + Environment.NewLine +
                "<Fila numero='a'></Fila>" + Environment.NewLine +
                "<Fila numero='6'></Fila>" + Environment.NewLine +
                "<Fila numero='17'></Fila>" + Environment.NewLine +
                "<Fila numero='1'></Fila>" + Environment.NewLine +
                 "</Sala>";

            var parser = this.CreateParser();

            var errors = parser.Validate(xml).Where(e => e.Message == Resources.RowNumberRangeError);

            Assert.AreEqual(3, errors.Count());

            var error1 = errors.ElementAt(0);

            Assert.AreEqual(2, error1.Line);

            var error2 = errors.ElementAt(1);

            Assert.AreEqual(3, error2.Line);

            var error3 = errors.ElementAt(2);

            Assert.AreEqual(4, error3.Line);
        }

        [TestMethod]
        public void ShouldReturnErrorIfDesdeIsNotBetween1And22()
        {
            string xml =
                "<Sala>" + Environment.NewLine +
                "<Fila desde='0'></Fila>" + Environment.NewLine +
                "<Fila desde='23'></Fila>" + Environment.NewLine +
                "<Fila desde='a'></Fila>" + Environment.NewLine +
                "<Fila desde='6'></Fila>" + Environment.NewLine +
                "<Fila desde='22'></Fila>" + Environment.NewLine +
                "<Fila desde='1'></Fila>" + Environment.NewLine +
                 "</Sala>";

            var parser = this.CreateParser();

            var errors = parser.Validate(xml).Where(e => e.Message == Resources.FromRangeError);

            Assert.AreEqual(3, errors.Count());

            var error1 = errors.ElementAt(0);

            Assert.AreEqual(2, error1.Line);

            var error2 = errors.ElementAt(1);

            Assert.AreEqual(3, error2.Line);

            var error3 = errors.ElementAt(2);

            Assert.AreEqual(4, error3.Line);
        }

        [TestMethod]
        public void ShouldReturnErrorIfHastaIsNotBetween1And22()
        {
            string xml =
                "<Sala>" + Environment.NewLine +
                "<Fila hasta='0'></Fila>" + Environment.NewLine +
                "<Fila hasta='23'></Fila>" + Environment.NewLine +
                "<Fila hasta='a'></Fila>" + Environment.NewLine +
                "<Fila hasta='6'></Fila>" + Environment.NewLine +
                "<Fila hasta='22'></Fila>" + Environment.NewLine +
                "<Fila hasta='1'></Fila>" + Environment.NewLine +
                 "</Sala>";

            var parser = this.CreateParser();

            var errors = parser.Validate(xml).Where(e => e.Message == Resources.ToRangeError);

            Assert.AreEqual(3, errors.Count());

            var error1 = errors.ElementAt(0);

            Assert.AreEqual(2, error1.Line);

            var error2 = errors.ElementAt(1);

            Assert.AreEqual(3, error2.Line);

            var error3 = errors.ElementAt(2);

            Assert.AreEqual(4, error3.Line);
        }

        [TestMethod]
        public void ShouldReturnErrorIfDesdeIsHighetThanHasta()
        {
            string xml =
                "<Sala>" + Environment.NewLine +
                "<Fila desde='10' hasta='9'></Fila>" + Environment.NewLine +
                 "</Sala>";

            var parser = this.CreateParser();

            var errors = parser.Validate(xml).Where(e => e.Message == Resources.FromHigherThanToError);

            Assert.AreEqual(1, errors.Count());

            var error1 = errors.ElementAt(0);

            Assert.AreEqual(2, error1.Line);
        }

        [TestMethod]
        public void ShouldReturnFreeSeatsForThoseSpecifiedInXml()
        {
            string xml =
                "<Sala>" + Environment.NewLine +
                "<Fila numero='1' desde='1' hasta='9'></Fila>" + Environment.NewLine +
                "<Fila numero='1' desde='8' hasta='11'></Fila>" + Environment.NewLine +
                "<Fila numero='2' desde='17' hasta='22'></Fila>" + Environment.NewLine +
                 "</Sala>";

            var parser = this.CreateParser();

            var seats = parser.Parse(xml);

            for (int i = 0; i <= 21; i++)
            {
                Assert.AreEqual(i <= 10 ? SeatState.Free : SeatState.NoSeat, seats.Seats[0][i]);
            }

            for (int i = 0; i <= 21; i++)
            {
                Assert.AreEqual(i >= 16 ? SeatState.Free : SeatState.NoSeat, seats.Seats[1][i]);
            }

            for (int i = 2; i <= 16; i++)
            {
                for (int j = 0; j <= 21; j++)
                {
                    Assert.AreEqual(SeatState.NoSeat, seats.Seats[i][j]);
                }
            }
        }

        private RoomXmlParser CreateParser()
        {
            return new RoomXmlParser();
        }
    }
}
