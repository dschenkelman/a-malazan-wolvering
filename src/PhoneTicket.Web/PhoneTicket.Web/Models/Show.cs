namespace PhoneTicket.Web.Models
{
    using System;
    using System.Collections.ObjectModel;
    using System.ComponentModel.DataAnnotations;
    using System.ComponentModel.DataAnnotations.Schema;

    public class Show
    {
        [DatabaseGenerated(DatabaseGeneratedOption.Identity), Key]
        public int Id { get; set; }

        public DateTime Date { get; set; }

        [ForeignKey("Room")]
        public int RoomId { get; set; }

        public virtual Room Room { get; set; }

        public bool IsAvailable { get; set; }

        [ForeignKey("Movie")]
        public int MovieId { get; set; }
        
        public virtual Movie Movie { get; set; }

        public double Price { get; set; }

        public virtual Collection<Operation> Operations { get; set; }
    }
}