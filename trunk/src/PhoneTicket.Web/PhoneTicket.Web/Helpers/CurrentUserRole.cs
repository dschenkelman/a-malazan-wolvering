namespace PhoneTicket.Web.Helpers
{
    using System;
    using System.Collections.Generic;
    using System.Linq;
    using System.Web;
    using WebMatrix.WebData;

    public class CurrentUserRole
    {
        private static CurrentUserRole instance = null;

        public bool userIsAdmin { get; set; }

        private CurrentUserRole()
        {
            userIsAdmin = (WebSecurity.CurrentUserName.Equals("admin"));
        }

        public static CurrentUserRole getInstance()
        {
            if (instance == null)
            {
                instance = new CurrentUserRole();
            }

            return instance;
        }
    }
}