namespace PhoneTicket.Web.Templates
{
    using PhoneTicket.Web.Models;

    public partial class OperationEmailTemplate
    {
        private readonly string name;

        private readonly string operation;

        private readonly string date;

        private readonly string time;

        private readonly string movie;

        private readonly string complex;

        public Operation Operation { get; set; }

        public OperationEmailTemplate(User user, Operation operation, Show show)
        {
            this.name = string.Format("{0} {1}", user.FirstName, user.LastName);
            this.operation = operation.Type == OperationType.Reservation ? "reserva" : "compra";
            this.date = show.Date.ToString("yyyy-MM-dd");
            this.time = show.Date.ToString("hh:mm");
            this.movie = show.Movie.Title;
            this.complex = show.Room.Complex.Name;
        }
    }
}