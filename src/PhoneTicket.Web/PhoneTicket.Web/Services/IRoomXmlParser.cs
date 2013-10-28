namespace PhoneTicket.Web.Services
{
    using System.Collections.Generic;

    public interface IRoomXmlParser
    {
        IEnumerable<RoomXmlError> Validate(string xml);
    }
}