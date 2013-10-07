namespace PhoneTicket.Web.Models
{
    using System.Collections.Generic;
    using System.ComponentModel.DataAnnotations.Schema;
    using System.Data.Entity.Spatial;

    using Newtonsoft.Json;

    public class Complex
    {
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public int Id { get; set; }

        public DbGeography Location { get; set; }

        public string Name { get; set; }

        public string Address { get; set; }
        
        [JsonIgnore]
        public virtual ICollection<Room> Rooms { get; set; }
    }
}