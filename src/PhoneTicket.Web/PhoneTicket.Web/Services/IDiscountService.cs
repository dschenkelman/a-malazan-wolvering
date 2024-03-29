﻿namespace PhoneTicket.Web.Services
{
    using System;
    using System.Collections.Generic;
    using System.Linq.Expressions;
    using System.Threading.Tasks;

    using PhoneTicket.Web.Models;

    public interface IDiscountService
    {
        Task<IEnumerable<Discount>> GetActiveAsync();

        Task<IEnumerable<Discount>> GetActiveAndFutureAsync();

        Task<IEnumerable<Discount>> GetActiveAndFutureAsync(Expression<Func<Discount, bool>> filter);

        Task DeleteAsync(int discountId);

        Task CreateAsync(Discount discount);

        Task<Discount> GetByIdAsync(int discountId);
        
        Task<IEnumerable<Discount>> GetByIdsAsync(params int[] discountIds);

        Task UpdateAsync(Discount discount);
    }
}