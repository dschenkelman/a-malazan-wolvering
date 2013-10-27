namespace PhoneTicket.Web.ViewModels.Api
{
    using PhoneTicket.Web.Models;
    using System;
    using System.Collections.Generic;
    using System.Linq;
    using System.Web;

    public class UserInformationViewModel
    {
        public int Id { get; set; }

        public string FirstName { get; set; }

        public string LastName { get; set; }

        public string EmailAddress { get; set; }

        public string Bithday { get; set; }

        public string CellPhoneNumber { get; set; }

        public static UserInformationViewModel FromUser(User user)
        {
            var userVM = new UserInformationViewModel
            {
                Id = user.Id,
                FirstName = user.FirstName,
                LastName = user.LastName,
                EmailAddress = user.EmailAddress,
                Bithday = user.BirthDate.HasValue ? user.BirthDate.Value.ToString("dd/MM/yyyy") : string.Empty,
                CellPhoneNumber = user.CellPhoneNumber
            };

            return userVM;
        }
    }
}
