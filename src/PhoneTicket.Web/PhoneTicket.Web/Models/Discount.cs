﻿namespace PhoneTicket.Web.Models
{
    using System;
    using System.Collections.ObjectModel;
    using System.ComponentModel.DataAnnotations.Schema;

    using PhoneTicket.Web.ViewModels;

    public class Discount
    {
        public int Id { get; set; }

        public string Description { get; set; }

        public DiscountType Type { get; set; }

        public double? Value { get; set; }

        public DateTime StartDate { get; set; }

        public DateTime EndDate { get; set; }

        public void UpdateFrom(DiscountViewModel viewModel)
        {
            var type = (DiscountType)viewModel.Type;

            this.Description = viewModel.Description;
            this.EndDate = viewModel.EndDate;
            this.StartDate = viewModel.StartDate;
            this.Type = type;
            this.Value = viewModel.Value.HasValue
                                ? (type == DiscountType.FixedPrice ? viewModel.Value.Value : viewModel.Value.Value / 100)
                                : (double?)null;
        }

        public virtual Collection<OperationDiscount> OperationDiscounts { get; set; }

        [NotMapped]
        public int RelatedTickets
        {
            get
            {
                return (this.Type == DiscountType.TwoForOne) ? 2 : 1;
            }
        }
    }
}