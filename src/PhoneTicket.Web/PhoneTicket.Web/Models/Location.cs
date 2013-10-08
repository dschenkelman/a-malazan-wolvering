namespace PhoneTicket.Web.Models
{
    using System.Globalization;

    public class Location
    {
        private const string GeoFormat = "POINT({0} {1})";

        public double Latitude { get; set; }

        public double Longitude { get; set; }

        public override string ToString()
        {
            return string.Format(
                GeoFormat,
                this.Longitude.ToString(CultureInfo.InvariantCulture),
                this.Latitude.ToString(CultureInfo.InvariantCulture));
        }
    }
}