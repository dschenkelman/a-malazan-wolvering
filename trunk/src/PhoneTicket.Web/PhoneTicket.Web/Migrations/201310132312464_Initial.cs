namespace PhoneTicket.Web.Migrations
{
    using System;
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
                        TypeId = c.Int(nullable: false),
                    })
                .PrimaryKey(t => t.Id)
                .ForeignKey("dbo.Complexes", t => t.ComplexId, cascadeDelete: true)
                .ForeignKey("dbo.RoomTypes", t => t.TypeId, cascadeDelete: true)
                .Index(t => t.ComplexId)
                .Index(t => t.TypeId);
            
            CreateTable(
                "dbo.RoomTypes",
                c => new
                    {
                        Id = c.Int(nullable: false, identity: true),
                        Description = c.String(),
                    })
                .PrimaryKey(t => t.Id);
            
            CreateTable(
                "dbo.Genres",
                c => new
                    {
                        Id = c.Int(nullable: false, identity: true),
                        Name = c.String(),
                    })
                .PrimaryKey(t => t.Id);
            
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
                "dbo.Ratings",
                c => new
                    {
                        Id = c.Int(nullable: false, identity: true),
                        Description = c.String(),
                    })
                .PrimaryKey(t => t.Id);
            
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
        }
        
        public override void Down()
        {
            DropForeignKey("dbo.TemporaryUsers", "Id", "dbo.Users");
            DropForeignKey("dbo.Shows", "RoomId", "dbo.Rooms");
            DropForeignKey("dbo.Shows", "MovieId", "dbo.Movies");
            DropForeignKey("dbo.Movies", "RatingId", "dbo.Ratings");
            DropForeignKey("dbo.Movies", "GenreId", "dbo.Genres");
            DropForeignKey("dbo.Rooms", "TypeId", "dbo.RoomTypes");
            DropForeignKey("dbo.Rooms", "ComplexId", "dbo.Complexes");
            DropIndex("dbo.TemporaryUsers", new[] { "Id" });
            DropIndex("dbo.Shows", new[] { "RoomId" });
            DropIndex("dbo.Shows", new[] { "MovieId" });
            DropIndex("dbo.Movies", new[] { "RatingId" });
            DropIndex("dbo.Movies", new[] { "GenreId" });
            DropIndex("dbo.Rooms", new[] { "TypeId" });
            DropIndex("dbo.Rooms", new[] { "ComplexId" });
            DropTable("dbo.Users");
            DropTable("dbo.TemporaryUsers");
            DropTable("dbo.Shows");
            DropTable("dbo.Ratings");
            DropTable("dbo.Movies");
            DropTable("dbo.Genres");
            DropTable("dbo.RoomTypes");
            DropTable("dbo.Rooms");
            DropTable("dbo.Complexes");
        }
    }
}
