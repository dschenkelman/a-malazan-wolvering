namespace PhoneTicket.Web.Services
{
    using System.Collections.Generic;

    using PhoneTicket.Web.Models;

    public interface IRoomXmlParser
    {
        IEnumerable<RoomXmlError> Validate(string xml);

        ShowSeats Parse(string xml);
    }
}