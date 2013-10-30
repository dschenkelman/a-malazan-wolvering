namespace PhoneTicket.Web.Models
{
    using System;
    using System.ComponentModel.DataAnnotations;
    using System.ComponentModel.DataAnnotations.Schema;

    public class OccupiedSeat
    {
        [Key, Column(Order = 0), ForeignKey("Operation")]
        public Guid OperationId { get; set; }

        public virtual Operation Operation { get; set; }

        [Key, Column(Order = 1)]
        public int Row { get; set; }

        [Key, Column(Order = 2)]
        public int Column { get; set; }
    }
}