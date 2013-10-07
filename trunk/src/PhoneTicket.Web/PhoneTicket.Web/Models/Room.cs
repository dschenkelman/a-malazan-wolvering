﻿namespace PhoneTicket.Web.Models
{
    using System.ComponentModel.DataAnnotations;
    using System.ComponentModel.DataAnnotations.Schema;

    using Newtonsoft.Json;
    using System.ComponentModel;

    public class Room
    {
        [DatabaseGenerated(DatabaseGeneratedOption.Identity), Key]
        public int Id { get; set; }

        [ForeignKey("Complex")]
        public int ComplexId { get; set; }

        [DisplayName("Nombre")]
        public string Name { get; set; }

        [DisplayName("Capacidad")]
        public int Capacity { get; set; }

        [JsonIgnore]
        [DisplayName("Complejo")]
        public virtual Complex Complex { get; set; }

        [ForeignKey("Type")]
        public int TypeId { get; set; }

        [JsonIgnore]
        [DisplayName("Estilo de Sala")]
        public virtual RoomType Type { get; set; }
    }
}