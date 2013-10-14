namespace PhoneTicket.Web.ViewModels
{
    using System;
    using System.Collections.Generic;
    using System.Linq;

    public class ListShowsByMovieViewModel
    {
        public IEnumerable<IGrouping<DateTime, ListShowViewModel>> ShowsPerDay { get; set; }

        public int MovieId { get; set; }
    }
}