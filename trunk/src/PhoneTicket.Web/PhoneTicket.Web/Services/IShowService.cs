﻿namespace PhoneTicket.Web.Services
{
    using System;
    using System.Collections.Generic;
    using System.Linq;
    using System.Text;
    using System.Threading.Tasks;

    using PhoneTicket.Web.Models;

    public interface IShowService
    {
        Task<Show> GetAsync(int id);
        Task UpdateAsync(Show show);
    }
}
