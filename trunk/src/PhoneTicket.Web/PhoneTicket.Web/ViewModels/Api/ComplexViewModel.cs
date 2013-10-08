namespace PhoneTicket.Web.ViewModels.Api
{
    using PhoneTicket.Web.Models;

    public class ComplexViewModel
    {
        public int Id { get; set; }

        public Location Location { get; set; }

        public string Address { get; set; }

        public string Name { get; set; }

        public static ComplexViewModel FromComplex(Complex complex)
        {
            return new ComplexViewModel()
                       {
                           Address = complex.Address,
                           Name = complex.Name,
                           Id = complex.Id
                       };
        }
    }
}