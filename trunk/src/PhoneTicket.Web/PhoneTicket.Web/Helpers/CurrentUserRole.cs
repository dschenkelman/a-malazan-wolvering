namespace PhoneTicket.Web.Helpers
{
    using System;
    using System.Collections.Generic;
    using System.Linq;
    using System.Web;

    public class CurrentUserRole
    {
        private static CurrentUserRole instance = null;

        public bool userIsAdmin { get; set; }

        private CurrentUserRole(){}

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