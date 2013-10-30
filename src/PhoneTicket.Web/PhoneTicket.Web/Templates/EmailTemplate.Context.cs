using System;

using PhoneTicket.Web.Models;

namespace PhoneTicket.Web.Templates
{
    public partial class EmailTemplate
    {
        private User User { get; set; }
        private Guid Secret { get; set; }

        public EmailTemplate(User user, Guid secret)
        {
            this.User = user;
            this.Secret = secret;
        }
    }
}