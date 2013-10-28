namespace PhoneTicket.Web.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class AddRoomXmlFile : DbMigration
    {
        public override void Up()
        {
            AddColumn("dbo.Rooms", "File", c => c.String());
        }
        
        public override void Down()
        {
            DropColumn("dbo.Rooms", "File");
        }
    }
}
