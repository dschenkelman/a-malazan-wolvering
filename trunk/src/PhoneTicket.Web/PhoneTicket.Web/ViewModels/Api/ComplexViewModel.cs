namespace PhoneTicket.Web.ViewModels.Api
{
    using PhoneTicket.Web.Models;
    using PhoneTicket.Web.Helpers;

    public class ComplexViewModel
    {
        public int Id { get; set; }

        public string Name { get; set; }

        public string Address { get; set; }

        public Location Location { get; set; }

        public static ComplexViewModel FromComplex(Complex complex)
        {
            return new ComplexViewModel()
                       {
                           Address = complex.Address,
                           Name = complex.Name,
                           Id = complex.Id,
                           Location = LocationExtension.FromDbGeographyToLocation(complex.Location)
                       };
        }
    }
}