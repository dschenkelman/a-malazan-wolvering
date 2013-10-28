namespace PhoneTicket.Web.Helpers
{
    using System;
    using System.Xml;
    using System.Xml.Linq;

    public static class XElementExtensions
    {
        public static int GetLineNumber(this IXmlLineInfo element)
        {
            return element.LineNumber;
        }

        public static bool IsNumberInRange(this XAttribute attribute, int minValue, int maxValue)
        {
            var value = attribute.Value;

            int num;

            return int.TryParse(value, out num) && num >= minValue && num <= maxValue;
        }

        public static int ParseInt32(this XAttribute attribute)
        {
            return Convert.ToInt32(attribute.Value);
        }
    }
}