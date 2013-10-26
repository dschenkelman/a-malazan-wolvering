namespace PhoneTicket.Web.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class PurchaseModel : DbMigration
    {
        public override void Up()
        {
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
                "dbo.Operations",
                c => new
                    {
                        Number = c.Int(nullable: false, identity: true),
                        UserId = c.Int(nullable: false),
                        Date = c.DateTime(nullable: false),
                        ShowId = c.Int(nullable: false),
                        Type = c.Int(nullable: false),
                        CreditCardNumber = c.String(),
                        CreditCardSecurityCode = c.String(),
                        CreditCardExpirationDate = c.DateTime(nullable: false),
                        CreditCardCompanyId = c.Int(nullable: false),
                        DiscountId = c.Int(),
                        DiscountCount = c.Int(),
                    })
                .PrimaryKey(t => t.Number)
                .ForeignKey("dbo.CreditCardCompanies", t => t.CreditCardCompanyId, cascadeDelete: true)
                .ForeignKey("dbo.Discounts", t => t.DiscountId)
                .ForeignKey("dbo.Shows", t => t.ShowId, cascadeDelete: true)
                .ForeignKey("dbo.Users", t => t.UserId, cascadeDelete: true)
                .Index(t => t.CreditCardCompanyId)
                .Index(t => t.DiscountId)
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
                        OperationId = c.Int(nullable: false),
                        Row = c.Int(nullable: false),
                        Column = c.Int(nullable: false),
                    })
                .PrimaryKey(t => new { t.OperationId, t.Row, t.Column })
                .ForeignKey("dbo.Operations", t => t.OperationId, cascadeDelete: true)
                .Index(t => t.OperationId);
            
        }
        
        public override void Down()
        {
            DropForeignKey("dbo.OccupiedSeats", "OperationId", "dbo.Operations");
            DropForeignKey("dbo.Operations", "UserId", "dbo.Users");
            DropForeignKey("dbo.Operations", "ShowId", "dbo.Shows");
            DropForeignKey("dbo.Operations", "DiscountId", "dbo.Discounts");
            DropForeignKey("dbo.Operations", "CreditCardCompanyId", "dbo.CreditCardCompanies");
            DropIndex("dbo.OccupiedSeats", new[] { "OperationId" });
            DropIndex("dbo.Operations", new[] { "UserId" });
            DropIndex("dbo.Operations", new[] { "ShowId" });
            DropIndex("dbo.Operations", new[] { "DiscountId" });
            DropIndex("dbo.Operations", new[] { "CreditCardCompanyId" });
            DropTable("dbo.OccupiedSeats");
            DropTable("dbo.CreditCardCompanies");
            DropTable("dbo.Operations");
            DropTable("dbo.Discounts");
        }
    }
}
