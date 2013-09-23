namespace PhoneTicket.Web.Migrations
{
    using System.Data.Entity.Migrations;
    using System.Linq;

    using PhoneTicket.Web.Data;
    using PhoneTicket.Web.Models;

    internal sealed class Configuration : DbMigrationsConfiguration<PhoneTicketContext>
    {
        public Configuration()
        {
            AutomaticMigrationsEnabled = true;
            AutomaticMigrationDataLossAllowed = true;
        }

        protected override void Seed(PhoneTicketContext context)
        {

            if (!context.Ratings.Any())
            {
                context.Ratings.Add(new Rating() { Description = "ATP" });
                context.Ratings.Add(new Rating() { Description = "SAM13" });
                context.Ratings.Add(new Rating() { Description = "SAM16" });
                context.Ratings.Add(new Rating() { Description = "SAM18" });    
            }

            if (!context.Genres.Any())
            {
                context.Genres.Add(new Genre { Name = "Comedia" });
                context.Genres.Add(new Genre { Name = "Acci�n" });
                context.Genres.Add(new Genre { Name = "Drama" });
                context.Genres.Add(new Genre { Name = "Terror" });
                context.Genres.Add(new Genre { Name = "Aventura" });
                context.Genres.Add(new Genre { Name = "Rom�ntica" });
                context.Genres.Add(new Genre { Name = "Western" });
                context.Genres.Add(new Genre { Name = "Suspenso" });
                context.Genres.Add(new Genre { Name = "Infantil" });
            }

            context.SaveChanges();

            if (!context.Movies.Any())
            {
                context.Movies.Add(
                    new Movie
                    {
                        Title = "El Conjuro",
                        Synopsis = "El conjuro se basa en los sucesos sobrenaturales que ocurrieron en la casa de Rhode Island de la familia Perron y que investigaron Ed y Lorraine Warren, expertos en actividades paranormales",
                        DurationInMinutes = 112,
                        GenreId = 4,
                        ImageUrl = "http://www.hoyts.com.ar/files/movies/picture/HO00001296.jpg",
                        TrailerUrl = "http://www.youtube.com/watch?v=OJgDCNyfWfQ",
                        RatingId = 3
                    });

                context.Movies.Add(
                    new Movie
                    {
                        Title = "Aviones",
                        Synopsis = "Aviones est� protagonizada por Dusty, un veloz avi�n fumigador de gran coraz�n que sue�a con competir como corredor de alto vuelo. Pero Dusty no fue precisamente construido para competir y, adem�s, le teme a las alturas. As� es como recurre al aviador naval Skipper, quien lo ayuda a calificar para enfrentarse a Ripslinger, el campe�n defensor del circuito de carreras. Dusty deber� demostrar todo su valor para llegar a alturas inimaginables, y le conceder� al mundo la inspiraci�n necesaria para volar",
                        DurationInMinutes = 96,
                        GenreId = 9,
                        ImageUrl = "http://www.hoyts.com.ar/files/movies/picture/HO00001342.jpg",
                        TrailerUrl = "http://www.youtube.com/watch?v=GpTivtieQq8",
                        RatingId = 1
                    });                
            }

            context.SaveChanges();
        }
    }
}