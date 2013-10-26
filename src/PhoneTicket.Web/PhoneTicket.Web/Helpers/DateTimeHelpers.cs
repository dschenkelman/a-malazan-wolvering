namespace PhoneTicket.Web.Helpers
{
    using System;

    public class DateTimeHelpers
    {
        private static TimeZoneInfo argentinaTimeZone;

        static DateTimeHelpers()
        {
            argentinaTimeZone = TimeZoneInfo.FindSystemTimeZoneById("Argentina Standard Time");
        }

        public static DateTime DateTimeInArgentina 
        { 
            get
            {
                return DateTime.UtcNow.Add(argentinaTimeZone.BaseUtcOffset);
            } 
        }
    }
}