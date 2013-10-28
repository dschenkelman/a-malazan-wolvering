namespace PhoneTicket.Web.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class DiscountWithMultipleOperations : DbMigration
    {
        public override void Up()
        {
            DropForeignKey("dbo.Discounts", "Operation_Number", "dbo.Operations");
            DropIndex("dbo.Discounts", new[] { "Operation_Number" });
            CreateTable(
                "dbo.OperationDiscounts",
                c => new
                    {
                        Operation_Number = c.Int(nullable: false),
                        Discount_Id = c.Int(nullable: false),
                    })
                .PrimaryKey(t => new { t.Operation_Number, t.Discount_Id })
                .ForeignKey("dbo.Operations", t => t.Operation_Number, cascadeDelete: true)
                .ForeignKey("dbo.Discounts", t => t.Discount_Id, cascadeDelete: true)
                .Index(t => t.Operation_Number)
                .Index(t => t.Discount_Id);
            
            DropColumn("dbo.Discounts", "Operation_Number");
        }
        
        public override void Down()
        {
            AddColumn("dbo.Discounts", "Operation_Number", c => c.Int());
            DropForeignKey("dbo.OperationDiscounts", "Discount_Id", "dbo.Discounts");
            DropForeignKey("dbo.OperationDiscounts", "Operation_Number", "dbo.Operations");
            DropIndex("dbo.OperationDiscounts", new[] { "Discount_Id" });
            DropIndex("dbo.OperationDiscounts", new[] { "Operation_Number" });
            DropTable("dbo.OperationDiscounts");
            CreateIndex("dbo.Discounts", "Operation_Number");
            AddForeignKey("dbo.Discounts", "Operation_Number", "dbo.Operations", "Number");
        }
    }
}
