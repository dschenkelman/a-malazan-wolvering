namespace PhoneTicket.Web.Helpers
{
    using System;
    using System.Collections.Generic;
    using System.Linq;
    using System.Web;

    using PhoneTicket.Web.ViewModels.Api;
    using PhoneTicket.Web.Models;

    public static class ComplexExtensions
    {
        public static ComplexListItemViewModel ToListItemViewModel(this Complex complex)
        {
            return new ComplexListItemViewModel()
            {
                Id = complex.Id,
                Name = complex.Name,
                Address = complex.Address
            };
        }

    }
}