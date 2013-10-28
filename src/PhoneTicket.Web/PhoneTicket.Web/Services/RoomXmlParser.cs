namespace PhoneTicket.Web.Services
{
    using System.Collections.Generic;
    using System.Xml;
    using System.Xml.Linq;

    using PhoneTicket.Web.Helpers;
    using PhoneTicket.Web.Properties;

    public class RoomXmlParser : IRoomXmlParser
    {
        public IEnumerable<RoomXmlError> Validate(string xml)
        {
            XDocument document;

            try
            {
                document = XDocument.Parse(xml, LoadOptions.SetLineInfo);
            }
            catch (XmlException e)
            {
                return new[] { new RoomXmlError(e.LineNumber, Resources.ErrorParsingContent) };
            }

            if (document.Root == null || document.Root.Name != "Sala")
            {
                return new[] { new RoomXmlError(document.Root.GetLineNumber(), Resources.InvalidRootElement) };
            }

            var errors = new List<RoomXmlError>();

            foreach (var childElement in document.Root.Elements())
            {
                if (childElement.Name != "Fila")
                {
                    errors.Add(new RoomXmlError(childElement.GetLineNumber(), Resources.InvalidRoomChildElement));
                    continue;
                }

                var numberAttribute = childElement.Attribute("numero");

                if (numberAttribute == null)
                {
                    errors.Add(new RoomXmlError(childElement.GetLineNumber(), Resources.RowNumberIsRequired));
                }
                else
                {
                    if (!numberAttribute.IsNumberInRange(1, 17))
                    {
                        errors.Add(new RoomXmlError(childElement.GetLineNumber(), Resources.RowNumberRangeError));   
                    }
                }

                var fromAttribute = childElement.Attribute("desde");
                bool validFrom = false;

                if (fromAttribute == null)
                {
                    errors.Add(new RoomXmlError(childElement.GetLineNumber(), Resources.FromAttributeIsRequired));
                }
                else
                {
                    if (!fromAttribute.IsNumberInRange(1, 22))
                    {
                        errors.Add(new RoomXmlError(childElement.GetLineNumber(), Resources.FromRangeError));
                    }
                    else
                    {
                        validFrom = true;
                    }
                }

                var toAttribute = childElement.Attribute("hasta");
                bool validTo = false;

                if (toAttribute == null)
                {
                    errors.Add(new RoomXmlError(childElement.GetLineNumber(), Resources.ToAttributeIsRequired));
                }
                else
                {
                    if (!toAttribute.IsNumberInRange(1, 22))
                    {
                        errors.Add(new RoomXmlError(childElement.GetLineNumber(), Resources.ToRangeError));
                    }
                    else
                    {
                        validTo = true;
                    }
                }

                if (validFrom && validTo)
                {
                    var toValue = toAttribute.ParseInt32();
                    var fromValue = fromAttribute.ParseInt32();

                    if (fromValue > toValue)
                    {
                        errors.Add(new RoomXmlError(childElement.GetLineNumber(), Resources.FromHigherThanToError));
                    }
                }
            }

            return errors;
        }
    }

    public class RoomXmlError
    {
        public RoomXmlError(int line, string message)
        {
            this.Line = line;
            this.Message = message;
        }

        public int Line { get; private set; }

        public string Message { get; private set; }
    }
}