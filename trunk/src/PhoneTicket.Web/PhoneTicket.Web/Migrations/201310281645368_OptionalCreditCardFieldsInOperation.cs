namespace PhoneTicket.Web.Migrations
{
    using System.Data.Entity.Migrations;
    
    public partial class OptionalCreditCardFieldsInOperation : DbMigration
    {
        public override void Up()
        {
            DropForeignKey("dbo.Operations", "CreditCardCompanyId", "dbo.CreditCardCompanies");
            DropIndex("dbo.Operations", new[] { "CreditCardCompanyId" });
            AlterColumn("dbo.Operations", "CreditCardExpirationDate", c => c.DateTime());
            AlterColumn("dbo.Operations", "CreditCardCompanyId", c => c.Int());
            CreateIndex("dbo.Operations", "CreditCardCompanyId");
            AddForeignKey("dbo.Operations", "CreditCardCompanyId", "dbo.CreditCardCompanies", "Id");
        }
        
        public override void Down()
        {
            DropForeignKey("dbo.Operations", "CreditCardCompanyId", "dbo.CreditCardCompanies");
            DropIndex("dbo.Operations", new[] { "CreditCardCompanyId" });
            AlterColumn("dbo.Operations", "CreditCardCompanyId", c => c.Int(nullable: false));
            AlterColumn("dbo.Operations", "CreditCardExpirationDate", c => c.DateTime(nullable: false));
            CreateIndex("dbo.Operations", "CreditCardCompanyId");
            AddForeignKey("dbo.Operations", "CreditCardCompanyId", "dbo.CreditCardCompanies", "Id", cascadeDelete: true);
        }
    }
}
