namespace PhoneTicket.Web.Services
{
    using System;
    using System.Linq;
    using System.Collections.Generic;
    using System.Data.Entity;
    using System.Linq.Expressions;
    using System.Threading.Tasks;

    using PhoneTicket.Web.Data;
    using PhoneTicket.Web.Helpers;
    using PhoneTicket.Web.Models;

    public class DiscountService : IDiscountService
    {
        private readonly IPhoneTicketRepositories repositories;

        public DiscountService(IPhoneTicketRepositories repositories)
        {
            this.repositories = repositories;
        }

        public async Task<IEnumerable<Discount>> GetActiveAsync()
        {
            var currentDate = DateTimeHelpers.DateTimeInArgentina;

            return await this.repositories.Discounts.Filter(d => d.StartDate <= currentDate && currentDate <= d.EndDate).ToListAsync();
        }

        public Task<IEnumerable<Discount>> GetActiveAndFutureAsync()
        {
            return this.repositories.Discounts.AllAsync();
        }

        public async Task<IEnumerable<Discount>> GetActiveAndFutureAsync(Expression<Func<Discount, bool>> filter)
        {
            return await this.repositories.Discounts.Filter(filter).ToListAsync();
        }

        public async Task DeleteAsync(int discountId)
        {
            await this.repositories.Discounts.DeleteAsync(discountId);
            await this.repositories.Discounts.SaveAsync();
        }

        public Task CreateAsync(Discount discount)
        {
            this.repositories.Discounts.Insert(discount);
            return this.repositories.Discounts.SaveAsync();
        }

        public Task<Discount> GetByIdAsync(int discountId)
        {
            return this.repositories.Discounts.GetByKeyValuesAsync(discountId);
        }

        public async Task<IEnumerable<Discount>> GetByIdsAsync(params int[] discountIds)
        {
            return await this.repositories.Discounts.Filter(d => discountIds.Contains(d.Id)).ToListAsync();
        }

        public Task UpdateAsync(Discount discount)
        {
            return this.repositories.Discounts.SaveAsync();
        }
    }
}