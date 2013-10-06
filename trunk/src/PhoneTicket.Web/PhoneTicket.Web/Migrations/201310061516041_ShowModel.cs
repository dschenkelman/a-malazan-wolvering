namespace PhoneTicket.Web.Migrations
{
    using System.Data.Entity.Migrations;
    
    public partial class ShowModel : DbMigration
    {
        public override void Up()
        {
            CreateTable(
                "dbo.Shows",
                c => new
                    {
                        Id = c.Int(nullable: false),
                        Date = c.DateTime(nullable: false),
                        RoomId = c.Int(nullable: false),
                        IsAvailable = c.Boolean(nullable: false),
                        MovieId = c.Int(nullable: false),
                    })
                .PrimaryKey(t => t.Id)
                .ForeignKey("dbo.Movies", t => t.MovieId, cascadeDelete: true)
                .ForeignKey("dbo.Rooms", t => t.RoomId, cascadeDelete: true)
                .Index(t => t.MovieId)
                .Index(t => t.RoomId);
            
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
                "dbo.RoomTypes",
                c => new
                    {
                        Id = c.Int(nullable: false, identity: true),
                        Description = c.String(),
                    })
                .PrimaryKey(t => t.Id);
            
        }
        
        public override void Down()
        {
            DropForeignKey("dbo.Shows", "RoomId", "dbo.Rooms");
            DropForeignKey("dbo.Rooms", "TypeId", "dbo.RoomTypes");
            DropForeignKey("dbo.Rooms", "ComplexId", "dbo.Complexes");
            DropForeignKey("dbo.Shows", "MovieId", "dbo.Movies");
            DropIndex("dbo.Shows", new[] { "RoomId" });
            DropIndex("dbo.Rooms", new[] { "TypeId" });
            DropIndex("dbo.Rooms", new[] { "ComplexId" });
            DropIndex("dbo.Shows", new[] { "MovieId" });
            DropTable("dbo.RoomTypes");
            DropTable("dbo.Complexes");
            DropTable("dbo.Rooms");
            DropTable("dbo.Shows");
        }
    }
}
