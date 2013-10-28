namespace PhoneTicket.Web.ViewModels.Api
{
    public class UserOperationsViewModel
    {
        public int Id { get; set; }
        
        public bool IsBought { get; set; }

        public string MovieTitle { get; set; }

        public string ShowDateAndTime { get; set; }

        public string ComplexAddress { get; set; }
    }
}