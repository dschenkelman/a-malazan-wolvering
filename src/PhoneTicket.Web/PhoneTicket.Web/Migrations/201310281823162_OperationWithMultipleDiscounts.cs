namespace PhoneTicket.Web.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class OperationWithMultipleDiscounts : DbMigration
    {
        public override void Up()
        {
            DropForeignKey("dbo.Operations", "DiscountId", "dbo.Discounts");
            DropIndex("dbo.Operations", new[] { "DiscountId" });
            AddColumn("dbo.Discounts", "Operation_Number", c => c.Int());
            CreateIndex("dbo.Discounts", "Operation_Number");
            AddForeignKey("dbo.Discounts", "Operation_Number", "dbo.Operations", "Number");
            DropColumn("dbo.Operations", "DiscountId");
        }
        
        public override void Down()
        {
            AddColumn("dbo.Operations", "DiscountId", c => c.Int());
            DropForeignKey("dbo.Discounts", "Operation_Number", "dbo.Operations");
            DropIndex("dbo.Discounts", new[] { "Operation_Number" });
            DropColumn("dbo.Discounts", "Operation_Number");
            CreateIndex("dbo.Operations", "DiscountId");
            AddForeignKey("dbo.Operations", "DiscountId", "dbo.Discounts", "Id");
        }
    }
}
