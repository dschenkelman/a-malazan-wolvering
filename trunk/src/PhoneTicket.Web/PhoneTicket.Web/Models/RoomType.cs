namespace PhoneTicket.Web.Models
{
    using System.ComponentModel.DataAnnotations.Schema;

    public class RoomType
    {
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public int Id { get; set; }

        public string Description { get; set; }
    }
}