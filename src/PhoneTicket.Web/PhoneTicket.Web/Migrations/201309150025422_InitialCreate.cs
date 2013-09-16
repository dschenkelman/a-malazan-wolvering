namespace PhoneTicket.Web.Migrations
{
    using System.Data.Entity.Migrations;
    
    public partial class InitialCreate : DbMigration
    {
        public override void Up()
        {
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

            //CreateTable(
            //    "dbo.UserProfile",
            //    c => new
            //        {
            //            UserId = c.Int(nullable: false, identity: true),
            //            UserName = c.String(),
            //        })
            //    .PrimaryKey(t => t.UserId);
        }
        
        public override void Down()
        {
            DropForeignKey("dbo.TemporaryUsers", "Id", "dbo.Users");
            DropIndex("dbo.TemporaryUsers", new[] { "Id" });
            DropTable("dbo.UserProfile");
            DropTable("dbo.Users");
            DropTable("dbo.TemporaryUsers");
        }
    }
}
