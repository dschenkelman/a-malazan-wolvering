namespace PhoneTicket.Web.Models
{
    using System;
    using System.ComponentModel.DataAnnotations.Schema;

    public class Show
    {
        [DatabaseGenerated(DatabaseGeneratedOption.Computed)]
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
    }
}