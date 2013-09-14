namespace PhoneTicket.Web.ViewModels
{
    using System;

    using PhoneTicket.Web.Models;

    public class ListUserViewModel
    {
        private ListUserViewModel()
        {
        }

        public int Id { get; set; }

        public string EmailAddress { get; set; }

        public string FirstName { get; set; }

        public string LastName { get; set; }

        public string CellPhoneNumber { get; set; }

        public string BirthDate { get; set; }

        public static ListUserViewModel FromUser(User user)
        {
            var vm = new ListUserViewModel
                         {
                             Id = user.Id,
                             LastName = user.LastName,
                             FirstName = user.FirstName,
                             EmailAddress = user.EmailAddress,
                             CellPhoneNumber = user.CellPhoneNumber,
                             BirthDate = user.BirthDate.HasValue ? user.BirthDate.Value.ToString("yyyy-MM-dd") : string.Empty
                         };

            return vm;
        }
    }
}