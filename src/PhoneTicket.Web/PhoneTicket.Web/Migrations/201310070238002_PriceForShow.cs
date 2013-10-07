namespace PhoneTicket.Web.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class PriceForShow : DbMigration
    {
        public override void Up()
        {
            AddColumn("dbo.Shows", "Price", c => c.Double(nullable: false));
        }
        
        public override void Down()
        {
            DropColumn("dbo.Shows", "Price");
        }
    }
}
