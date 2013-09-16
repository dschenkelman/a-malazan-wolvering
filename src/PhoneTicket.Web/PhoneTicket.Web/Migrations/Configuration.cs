namespace PhoneTicket.Web.Migrations
{
    using System.Data.Entity.Migrations;

    using PhoneTicket.Web.Data;

    internal sealed class Configuration : DbMigrationsConfiguration<PhoneTicketContext>
    {
        public Configuration()
        {
            AutomaticMigrationsEnabled = true;
            AutomaticMigrationDataLossAllowed = true;
        }

        protected override void Seed(PhoneTicketContext context)
        {
        }
    }
}
