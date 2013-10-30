namespace PhoneTicket.Web.Migrations
{
    using System.Data.Entity.Migrations;
    
    public partial class Initial : DbMigration
    {
        public override void Up()
        {
            CreateTable(
                "dbo.Complexes",
                c => new
                    {
                        Id = c.Int(nullable: false, identity: true),
                        Location = c.Geography(),
                        Name = c.String(),
                        Address = c.String(),
                    })
                .PrimaryKey(t => t.Id);
            
            CreateTable(
                "dbo.Rooms",
                c => new
                    {
                        Id = c.Int(nullable: false, identity: true),
                        ComplexId = c.Int(nullable: false),
                        Name = c.String(),
                        Capacity = c.Int(nullable: false),
                        File = c.String(),
                    })
                .PrimaryKey(t => t.Id)
                .ForeignKey("dbo.Complexes", t => t.ComplexId, cascadeDelete: true)
                .Index(t => t.ComplexId);
            
            CreateTable(
                "dbo.Discounts",
                c => new
                    {
                        Id = c.Int(nullable: false, identity: true),
                        Description = c.String(),
                        Type = c.Int(nullable: false),
                        Value = c.Double(),
                        StartDate = c.DateTime(nullable: false),
                        EndDate = c.DateTime(nullable: false),
                    })
                .PrimaryKey(t => t.Id);
            
            CreateTable(
                "dbo.OperationDiscounts",
                c => new
                    {
                        OperationId = c.Guid(nullable: false),
                        DiscountId = c.Int(nullable: false),
                        Count = c.Int(nullable: false),
                    })
                .PrimaryKey(t => new { t.OperationId, t.DiscountId })
                .ForeignKey("dbo.Discounts", t => t.DiscountId, cascadeDelete: true)
                .ForeignKey("dbo.Operations", t => t.OperationId, cascadeDelete: true)
                .Index(t => t.DiscountId)
                .Index(t => t.OperationId);
            
            CreateTable(
                "dbo.Operations",
                c => new
                    {
                        Number = c.Guid(nullable: false, identity: true),
                        UserId = c.Int(nullable: false),
                        Date = c.DateTime(nullable: false),
                        ShowId = c.Int(nullable: false),
                        Type = c.Int(nullable: false),
                        CreditCardNumber = c.String(),
                        CreditCardSecurityCode = c.String(),
                        CreditCardExpirationDate = c.DateTime(),
                        CreditCardCompanyId = c.Int(),
                    })
                .PrimaryKey(t => t.Number)
                .ForeignKey("dbo.CreditCardCompanies", t => t.CreditCardCompanyId)
                .ForeignKey("dbo.Shows", t => t.ShowId, cascadeDelete: true)
                .ForeignKey("dbo.Users", t => t.UserId, cascadeDelete: true)
                .Index(t => t.CreditCardCompanyId)
                .Index(t => t.ShowId)
                .Index(t => t.UserId);
            
            CreateTable(
                "dbo.CreditCardCompanies",
                c => new
                    {
                        Id = c.Int(nullable: false, identity: true),
                        Name = c.String(),
                    })
                .PrimaryKey(t => t.Id);
            
            CreateTable(
                "dbo.OccupiedSeats",
                c => new
                    {
                        OperationId = c.Guid(nullable: false),
                        Row = c.Int(nullable: false),
                        Column = c.Int(nullable: false),
                    })
                .PrimaryKey(t => new { t.OperationId, t.Row, t.Column })
                .ForeignKey("dbo.Operations", t => t.OperationId, cascadeDelete: true)
                .Index(t => t.OperationId);
            
            CreateTable(
                "dbo.Shows",
                c => new
                    {
                        Id = c.Int(nullable: false, identity: true),
                        Date = c.DateTime(nullable: false),
                        RoomId = c.Int(nullable: false),
                        IsAvailable = c.Boolean(nullable: false),
                        MovieId = c.Int(nullable: false),
                        Price = c.Double(nullable: false),
                    })
                .PrimaryKey(t => t.Id)
                .ForeignKey("dbo.Movies", t => t.MovieId, cascadeDelete: true)
                .ForeignKey("dbo.Rooms", t => t.RoomId, cascadeDelete: true)
                .Index(t => t.MovieId)
                .Index(t => t.RoomId);
            
            CreateTable(
                "dbo.Movies",
                c => new
                    {
                        Id = c.Int(nullable: false, identity: true),
                        Title = c.String(nullable: false),
                        Synopsis = c.String(nullable: false),
                        ImageUrl = c.String(nullable: false),
                        TrailerUrl = c.String(nullable: false),
                        DurationInMinutes = c.Int(nullable: false),
                        GenreId = c.Int(nullable: false),
                        RatingId = c.Int(nullable: false),
                    })
                .PrimaryKey(t => t.Id)
                .ForeignKey("dbo.Genres", t => t.GenreId, cascadeDelete: true)
                .ForeignKey("dbo.Ratings", t => t.RatingId, cascadeDelete: true)
                .Index(t => t.GenreId)
                .Index(t => t.RatingId);
            
            CreateTable(
                "dbo.Genres",
                c => new
                    {
                        Id = c.Int(nullable: false, identity: true),
                        Name = c.String(),
                    })
                .PrimaryKey(t => t.Id);
            
            CreateTable(
                "dbo.Ratings",
                c => new
                    {
                        Id = c.Int(nullable: false, identity: true),
                        Description = c.String(),
                    })
                .PrimaryKey(t => t.Id);
            
            CreateTable(
                "dbo.Users",
                c => new
                    {
                        Id = c.Int(nullable: false),
                        EmailAddress = c.String(),
                        PasswordHash = c.Binary(),
                        FirstName = c.String(),
                        LastName = c.String(),
                        CellPhoneNumber = c.String(),
                        BirthDate = c.DateTime(),
                        IsValid = c.Boolean(nullable: false),
                    })
                .PrimaryKey(t => t.Id);
            
            CreateTable(
                "dbo.TemporaryUsers",
                c => new
                    {
                        Id = c.Int(nullable: false),
                        Secret = c.Guid(nullable: false),
                        RegistrationDate = c.DateTime(nullable: false),
                    })
                .PrimaryKey(t => t.Id)
                .ForeignKey("dbo.Users", t => t.Id)
                .Index(t => t.Id);
        }
        
        public override void Down()
        {
            DropForeignKey("dbo.TemporaryUsers", "Id", "dbo.Users");
            DropForeignKey("dbo.OperationDiscounts", "OperationId", "dbo.Operations");
            DropForeignKey("dbo.Operations", "UserId", "dbo.Users");
            DropForeignKey("dbo.Operations", "ShowId", "dbo.Shows");
            DropForeignKey("dbo.Shows", "RoomId", "dbo.Rooms");
            DropForeignKey("dbo.Shows", "MovieId", "dbo.Movies");
            DropForeignKey("dbo.Movies", "RatingId", "dbo.Ratings");
            DropForeignKey("dbo.Movies", "GenreId", "dbo.Genres");
            DropForeignKey("dbo.OccupiedSeats", "OperationId", "dbo.Operations");
            DropForeignKey("dbo.Operations", "CreditCardCompanyId", "dbo.CreditCardCompanies");
            DropForeignKey("dbo.OperationDiscounts", "DiscountId", "dbo.Discounts");
            DropForeignKey("dbo.Rooms", "ComplexId", "dbo.Complexes");
            DropIndex("dbo.TemporaryUsers", new[] { "Id" });
            DropIndex("dbo.OperationDiscounts", new[] { "OperationId" });
            DropIndex("dbo.Operations", new[] { "UserId" });
            DropIndex("dbo.Operations", new[] { "ShowId" });
            DropIndex("dbo.Shows", new[] { "RoomId" });
            DropIndex("dbo.Shows", new[] { "MovieId" });
            DropIndex("dbo.Movies", new[] { "RatingId" });
            DropIndex("dbo.Movies", new[] { "GenreId" });
            DropIndex("dbo.OccupiedSeats", new[] { "OperationId" });
            DropIndex("dbo.Operations", new[] { "CreditCardCompanyId" });
            DropIndex("dbo.OperationDiscounts", new[] { "DiscountId" });
            DropIndex("dbo.Rooms", new[] { "ComplexId" });
            DropTable("dbo.UserProfile");
            DropTable("dbo.TemporaryUsers");
            DropTable("dbo.Users");
            DropTable("dbo.Ratings");
            DropTable("dbo.Genres");
            DropTable("dbo.Movies");
            DropTable("dbo.Shows");
            DropTable("dbo.OccupiedSeats");
            DropTable("dbo.CreditCardCompanies");
            DropTable("dbo.Operations");
            DropTable("dbo.OperationDiscounts");
            DropTable("dbo.Discounts");
            DropTable("dbo.Rooms");
            DropTable("dbo.Complexes");
        }
    }
}
