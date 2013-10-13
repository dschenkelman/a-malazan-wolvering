namespace PhoneTicket.Web.Helpers
{
    
    using System;
    using System.Data.Entity.Spatial;

    using PhoneTicket.Web.Models;

    public static class LocationExtension
    {
        public static Location ToLocation(this DbGeography geography)
        {
            return new Location 
            {
                Latitude = Convert.ToDouble(geography.Latitude),
                Longitude = Convert.ToDouble(geography.Longitude)
            };
        }
    }
}