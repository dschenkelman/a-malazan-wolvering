﻿namespace PhoneTicket.Web.Services
{
    using System.Collections.Generic;
    using System.Threading.Tasks;

    using PhoneTicket.Web.Models;

    public interface IComplexService
    {
        Task<IEnumerable<Complex>> GetAsync();
    }
}