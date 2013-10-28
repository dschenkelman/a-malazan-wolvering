namespace PhoneTicket.Web.Data
{
    using System.Data.Entity;

    using PhoneTicket.Web.Migrations;
    using PhoneTicket.Web.Models;

    public class PhoneTicketContext : DbContext, IContext
    {
        public PhoneTicketContext() : base("DefaultConnection")
        {
        }

        public DbSet<User> Users { get; set; }
        
        public DbSet<Genre> Genres { get; set; }
        
        public DbSet<Movie> Movies { get; set; }
        
        public DbSet<Rating> Ratings { get; set; }

        public DbSet<TemporaryUser> TemporaryUser { get; set; }

        public DbSet<UserProfile> UserProfiles { get; set; }

        public DbSet<Show> Shows { get; set; }

        public DbSet<Complex> Complexes { get; set; }

        public DbSet<Room> Rooms { get; set; }

        public DbSet<Discount> Discounts { get; set; }

        public DbSet<Operation> Operations { get; set; }

        public DbSet<OccupiedSeat> OccupiedSeats { get; set; }

        protected override void OnModelCreating(DbModelBuilder modelBuilder)
        {
           Database.SetInitializer(new MigrateDatabaseToLatestVersion<PhoneTicketContext, Configuration>());
        }

        DbSet<TEntity> IContext.Set<TEntity>()
        {
            return this.Set<TEntity>();
        }
    }
}