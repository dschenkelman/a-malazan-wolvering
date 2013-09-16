namespace PhoneTicket.Web.Data
{
    using System.Data.Entity;

    using PhoneTicket.Web.Migrations;
    using PhoneTicket.Web.Models;

    public class PhoneTicketContext : DbContext
    {
        public PhoneTicketContext() : base("DefaultConnection")
        {
        }

        public DbSet<User> Users { get; set; }

        public DbSet<TemporaryUser> TemporaryUser { get; set; }

        public DbSet<UserProfile> UserProfiles { get; set; }

        protected override void OnModelCreating(DbModelBuilder modelBuilder)
        {
            Database.SetInitializer(new MigrateDatabaseToLatestVersion<PhoneTicketContext, Configuration>());
        }
    }
}