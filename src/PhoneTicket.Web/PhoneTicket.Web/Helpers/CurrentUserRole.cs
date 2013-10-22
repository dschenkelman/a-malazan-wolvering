namespace PhoneTicket.Web.Helpers
{
    using System;
    using System.Collections.Generic;
    using System.Linq;
    using System.Web;
    using WebMatrix.WebData;

    public class CurrentUserRole : ICurrentUserRole
    {
        public bool UserIsAdmin()
        {
            return WebSecurity.CurrentUserName.Equals("admin");
        }
    }
}