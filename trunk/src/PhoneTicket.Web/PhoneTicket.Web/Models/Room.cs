namespace PhoneTicket.Web.Models
{
    using System.ComponentModel.DataAnnotations;
    using System.ComponentModel.DataAnnotations.Schema;

    using Newtonsoft.Json;

    public class Room
    {
        [DatabaseGenerated(DatabaseGeneratedOption.Identity), Key]
        public int Id { get; set; }

        [ForeignKey("Complex")]
        public int ComplexId { get; set; }

        public string Name { get; set; }

        public int Capacity { get; set; }

        [JsonIgnore]
        public virtual Complex Complex { get; set; }

        [ForeignKey("Type")]
        public int TypeId { get; set; }

        [JsonIgnore]
        public virtual RoomType Type { get; set; }
    }
}