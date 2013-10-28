namespace PhoneTicket.Web.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class DeleteRoomStyles : DbMigration
    {
        public override void Up()
        {
            DropForeignKey("dbo.Rooms", "TypeId", "dbo.RoomTypes");
            DropIndex("dbo.Rooms", new[] { "TypeId" });
            DropColumn("dbo.Rooms", "TypeId");
            DropTable("dbo.RoomTypes");
        }
        
        public override void Down()
        {
            CreateTable(
                "dbo.RoomTypes",
                c => new
                    {
                        Id = c.Int(nullable: false, identity: true),
                        Description = c.String(),
                    })
                .PrimaryKey(t => t.Id);
            
            AddColumn("dbo.Rooms", "TypeId", c => c.Int(nullable: false));
            CreateIndex("dbo.Rooms", "TypeId");
            AddForeignKey("dbo.Rooms", "TypeId", "dbo.RoomTypes", "Id", cascadeDelete: true);
        }
    }
}
