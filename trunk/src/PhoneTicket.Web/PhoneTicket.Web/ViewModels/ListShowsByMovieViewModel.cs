namespace PhoneTicket.Web.ViewModels
{
    using System;
    using System.Collections.Generic;
    using System.Linq;
    using System.Web.Mvc;

    public class ListShowsByMovieViewModel
    {
        public IEnumerable<IGrouping<DateTime, ListShowViewModel>> ShowsPerDay { get; set; }

        public int MovieId { get; set; }

        public IEnumerable<SelectListItem> Movies { get; set; }
    }
}