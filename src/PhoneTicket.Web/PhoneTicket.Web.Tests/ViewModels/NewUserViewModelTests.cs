namespace PhoneTicket.Web.Tests.ViewModels
{
    using System;
    using System.Security.Cryptography;
    using System.Text;

    using Microsoft.VisualStudio.TestTools.UnitTesting;

    using PhoneTicket.Web.ViewModels;

    [TestClass]
    public class NewUserViewModelTests
    {
        [TestMethod]
        public void ShouldSetAllPropertiesToUserWhenToUserIsCalledUsingSHAForPasswordHash()
        {
            var birthDateString = "1990/09/09";
            var birthDate = new DateTime(1990, 9, 9);
            var emailAddress = "email@email.com";
            var cellPhone = "1153224411";
            var firstName = "John";
            var lastName = "Doe";
            var dni = 35111889;
            var password = "password";

            var viewModel = new NewUserViewModel
                                {
                                    BirthDate = birthDateString,
                                    CellPhoneNumber = cellPhone,
                                    EmailAddress = emailAddress,
                                    FirstName = firstName,
                                    LastName = lastName,
                                    Id = dni,
                                    Password = password
                                };

            var user = viewModel.ToUser();

            Assert.AreEqual(birthDate, user.BirthDate);
            Assert.AreEqual(emailAddress, user.EmailAddress);
            Assert.AreEqual(cellPhone, user.CellPhoneNumber);
            Assert.AreEqual(firstName, user.FirstName);
            Assert.AreEqual(lastName, user.LastName);
            Assert.AreEqual(dni, user.Id);

            var hash = new SHA256CryptoServiceProvider().ComputeHash(Encoding.UTF8.GetBytes(password));

            Assert.AreEqual(Encoding.UTF8.GetString(hash), Encoding.UTF8.GetString(user.PasswordHash));
        }

        [TestMethod]
        public void ShouldCreateNewUserWithIsValidTrue()
        {
            var birthDateString = "1990/09/09";
            var birthDate = new DateTime(1990, 9, 9);
            var emailAddress = "email@email.com";
            var cellPhone = "1153224411";
            var firstName = "John";
            var lastName = "Doe";
            var dni = 35111889;
            var password = "password";

            var viewModel = new NewUserViewModel
            {
                BirthDate = birthDateString,
                CellPhoneNumber = cellPhone,
                EmailAddress = emailAddress,
                FirstName = firstName,
                LastName = lastName,
                Id = dni,
                Password = password
            };

            Assert.IsTrue(viewModel.ToUser().IsValid);
        }

        [TestMethod]
        public void ShouldSetDateToNullIfBirthDateIsNotValid()
        {
            var birthDateString = "invalid/09/09";
            var emailAddress = "email@email.com";
            var cellPhone = "1153224411";
            var firstName = "John";
            var lastName = "Doe";
            var dni = 35111889;
            var password = "password";

            var viewModel = new NewUserViewModel
            {
                BirthDate = birthDateString,
                CellPhoneNumber = cellPhone,
                EmailAddress = emailAddress,
                FirstName = firstName,
                LastName = lastName,
                Id = dni,
                Password = password
            };

            var user = viewModel.ToUser();

            Assert.AreEqual(null, user.BirthDate);
        }

        [TestMethod]
        public void ShouldSetDateToNullIfBirthDateIsEmpty()
        {
            var birthDateString = string.Empty;
            var emailAddress = "email@email.com";
            var cellPhone = "1153224411";
            var firstName = "John";
            var lastName = "Doe";
            var dni = 35111889;
            var password = "password";

            var viewModel = new NewUserViewModel
            {
                BirthDate = birthDateString,
                CellPhoneNumber = cellPhone,
                EmailAddress = emailAddress,
                FirstName = firstName,
                LastName = lastName,
                Id = dni,
                Password = password
            };

            var user = viewModel.ToUser();

            Assert.AreEqual(null, user.BirthDate);
        }

        [TestMethod]
        public void ShouldSetDateToNullIfBirthDateIsNull()
        {
            string birthDateString = null;
            var emailAddress = "email@email.com";
            var cellPhone = "1153224411";
            var firstName = "John";
            var lastName = "Doe";
            var dni = 35111889;
            var password = "password";

            var viewModel = new NewUserViewModel
            {
                BirthDate = birthDateString,
                CellPhoneNumber = cellPhone,
                EmailAddress = emailAddress,
                FirstName = firstName,
                LastName = lastName,
                Id = dni,
                Password = password
            };

            var user = viewModel.ToUser();

            Assert.AreEqual(null, user.BirthDate);
        }
    }
}
