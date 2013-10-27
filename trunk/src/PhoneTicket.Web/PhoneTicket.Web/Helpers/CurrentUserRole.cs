namespace PhoneTicket.Web.Helpers
{
    using WebMatrix.WebData;

    public class CurrentUserRole : ICurrentUserRole
    {
        public bool IsAdmin()
        {
            return WebSecurity.CurrentUserName.Equals("admin");
        }
    }
}