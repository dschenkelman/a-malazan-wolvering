namespace PhoneTicket.Web.ViewModels.Api
{
    using System.Collections.Generic;

    using Newtonsoft.Json;

    public class ShowsPerComplexViewModel
    {
        public ShowsPerComplexViewModel()
        {
            this.Functions = new List<ShowInfoViewModel>();
        }

        public int CinemaId
        {
            get { return this.ComplexId; }
        }

        public string CinemaName
        {
            get { return this.ComplexName; }
        }

        public List<ShowInfoViewModel> Functions { get; set; }
        
        [JsonIgnore]
        public string ComplexName { get; set; }

        [JsonIgnore]
        public int ComplexId { get; set; }
    }
}