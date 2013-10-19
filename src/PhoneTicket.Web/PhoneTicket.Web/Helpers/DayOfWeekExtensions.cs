namespace PhoneTicket.Web.Helpers
{
    using System;

    public static class DayOfWeekExtensions
    {
        public static string InSpanish(this DayOfWeek dayOfWeek)
        {
            switch (dayOfWeek)
            {
                case DayOfWeek.Sunday:
                    return "Domingo";
                case DayOfWeek.Monday:
                    return "Lunes";
                case DayOfWeek.Tuesday:
                    return "Martes";
                case DayOfWeek.Wednesday:
                    return "Miercoles";
                case DayOfWeek.Thursday:
                    return "Jueves";
                case DayOfWeek.Friday:
                    return "Viernes";
                case DayOfWeek.Saturday:
                    return "Sabado";
                default:
                    throw new ArgumentOutOfRangeException("dayOfWeek");
            }
        }
    }
}