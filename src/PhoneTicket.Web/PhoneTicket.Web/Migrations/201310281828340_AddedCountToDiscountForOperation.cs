namespace PhoneTicket.Web.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class AddedCountToDiscountForOperation : DbMigration
    {
        public override void Up()
        {
            DropForeignKey("dbo.OperationDiscounts", "Operation_Number", "dbo.Operations");
            DropForeignKey("dbo.OperationDiscounts", "Discount_Id", "dbo.Discounts");
            DropIndex("dbo.OperationDiscounts", new[] { "Operation_Number" });
            DropIndex("dbo.OperationDiscounts", new[] { "Discount_Id" });
            AddColumn("dbo.OperationDiscounts", "OperationId", c => c.Int(nullable: false));
            AddColumn("dbo.OperationDiscounts", "DiscountId", c => c.Int(nullable: false));
            AddColumn("dbo.OperationDiscounts", "Count", c => c.Int(nullable: false));
            DropPrimaryKey("dbo.OperationDiscounts");
            AddPrimaryKey("dbo.OperationDiscounts", new[] { "OperationId", "DiscountId" });
            CreateIndex("dbo.OperationDiscounts", "DiscountId");
            CreateIndex("dbo.OperationDiscounts", "OperationId");
            AddForeignKey("dbo.OperationDiscounts", "DiscountId", "dbo.Discounts", "Id", cascadeDelete: true);
            AddForeignKey("dbo.OperationDiscounts", "OperationId", "dbo.Operations", "Number", cascadeDelete: true);
            DropColumn("dbo.Operations", "DiscountCount");
            DropColumn("dbo.OperationDiscounts", "Operation_Number");
            DropColumn("dbo.OperationDiscounts", "Discount_Id");
        }
        
        public override void Down()
        {
            AddColumn("dbo.OperationDiscounts", "Discount_Id", c => c.Int(nullable: false));
            AddColumn("dbo.OperationDiscounts", "Operation_Number", c => c.Int(nullable: false));
            AddColumn("dbo.Operations", "DiscountCount", c => c.Int());
            DropForeignKey("dbo.OperationDiscounts", "OperationId", "dbo.Operations");
            DropForeignKey("dbo.OperationDiscounts", "DiscountId", "dbo.Discounts");
            DropIndex("dbo.OperationDiscounts", new[] { "OperationId" });
            DropIndex("dbo.OperationDiscounts", new[] { "DiscountId" });
            DropPrimaryKey("dbo.OperationDiscounts");
            AddPrimaryKey("dbo.OperationDiscounts", new[] { "Operation_Number", "Discount_Id" });
            DropColumn("dbo.OperationDiscounts", "Count");
            DropColumn("dbo.OperationDiscounts", "DiscountId");
            DropColumn("dbo.OperationDiscounts", "OperationId");
            CreateIndex("dbo.OperationDiscounts", "Discount_Id");
            CreateIndex("dbo.OperationDiscounts", "Operation_Number");
            AddForeignKey("dbo.OperationDiscounts", "Discount_Id", "dbo.Discounts", "Id", cascadeDelete: true);
            AddForeignKey("dbo.OperationDiscounts", "Operation_Number", "dbo.Operations", "Number", cascadeDelete: true);
        }
    }
}
