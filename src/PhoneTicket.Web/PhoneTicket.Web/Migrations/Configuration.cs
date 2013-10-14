namespace PhoneTicket.Web.Migrations
{
    using System.Data.Entity.Migrations;
    using System.Data.Entity.Spatial;
    using System.Linq;

    using PhoneTicket.Web.Models;

    internal sealed class Configuration : DbMigrationsConfiguration<PhoneTicket.Web.Data.PhoneTicketContext>
    {
        public Configuration()
        {
            AutomaticMigrationsEnabled = false;
        }

        protected override void Seed(PhoneTicket.Web.Data.PhoneTicketContext context)
        {
            if (!context.Ratings.Any())
            {
                context.Ratings.Add(new Rating { Description = "ATP" });
                context.Ratings.Add(new Rating { Description = "SAM13" });
                context.Ratings.Add(new Rating { Description = "SAM16" });
                context.Ratings.Add(new Rating { Description = "SAM18" });
            }

            if (!context.Genres.Any())
            {
                context.Genres.Add(new Genre { Name = "Comedia" });
                context.Genres.Add(new Genre { Name = "Acción" });
                context.Genres.Add(new Genre { Name = "Drama" });
                context.Genres.Add(new Genre { Name = "Terror" });
                context.Genres.Add(new Genre { Name = "Aventura" });
                context.Genres.Add(new Genre { Name = "Romántica" });
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
                        Synopsis = "Aviones está protagonizada por Dusty, un veloz avión fumigador de gran corazón que sueña con competir como corredor de alto vuelo. Pero Dusty no fue precisamente construido para competir y, además, le teme a las alturas. Así es como recurre al aviador naval Skipper, quien lo ayuda a calificar para enfrentarse a Ripslinger, el campeón defensor del circuito de carreras. Dusty deberá demostrar todo su valor para llegar a alturas inimaginables, y le concederá al mundo la inspiración necesaria para volar",
                        DurationInMinutes = 96,
                        GenreId = 9,
                        ImageUrl = "http://www.hoyts.com.ar/files/movies/picture/HO00001342.jpg",
                        TrailerUrl = "http://www.youtube.com/watch?v=GpTivtieQq8",
                        RatingId = 1
                    });

                context.Movies.Add(
                    new Movie
                    {
                        Title = "Aprendices Fuera de Línea",
                        Synopsis = "Billy (Vince Vaughn) y Nick (Owen Wilson) son vendedores cuyas carreras han sido bombardeada por el mundo digital. Intentando probar que no son obsoletos, ellos desafían todos los obstáculos y aplican para una pasantía en Google, junto a un batallón de brillantes estudiantes. Sin embargo, entrar a la pasantía es tan sólo el comienzo. Ahora deben competir contra un grupo de los mejores estudiantes del país, genios de la tecnología y Billy (Vince Vaughn) y Nick (Owen Wilson) les probarán que la necesidad es la madre de toda reinvención.",
                        DurationInMinutes = 119,
                        GenreId = 1,
                        ImageUrl = "http://www.hoyts.com.ar/files/movies/picture/HO00001317.jpg",
                        TrailerUrl = "http://www.youtube.com/watch?v=30v_FQxGmaA",
                        RatingId = 2
                    });

                context.Movies.Add(
                    new Movie
                    {
                        Title = "El Ataque",
                        Synopsis = "Al policía del Capitolio John Cale le acaban de negar el trabajo de sus sueños: proteger al presidente James Sawyer. Para no defraudar a su pequeña hija con la noticia, la lleva a hacer un tour por la Casa Blanca cuando el edificio es sorprendido por un grupo paramilitar fuertemente armado. Ahora, mientras en gobierno de la nación está en medio del caos y el tiempo corre, dependerá de Cale salvar al presidente, a su hija y al país.",
                        DurationInMinutes = 131,
                        GenreId = 2,
                        ImageUrl = "http://www.hoyts.com.ar/files/movies/picture/HO00001348.jpg",
                        TrailerUrl = "http://www.youtube.com/watch?v=F2shqzlRdmk",
                        RatingId = 2
                    });

                context.Movies.Add(
                    new Movie
                    {
                        Title = "Séptimo",
                        Synopsis = "Como cada día, Marcelo recoge a sus hijos en el piso de su exmujer. Como cada día juegan a \"a ver quién llega antes\": ellos bajan por las escaleras, él en el ascensor, un divertimento que a su expareja no le gusta. Pero cuando un día Marcelo llega el primero al piso de abajo los niños no están. No están en ningún sitio. El miedo empieza a aflorar cuando una llamada telefónica les catapulta al horror: un secuestrador pone precio para la liberación de sus hijos. Marcelo tendrá que asumir la fragilidad de su mundo y decidir hasta dónde está dispuesto a llegar para recuperarlo.",
                        DurationInMinutes = 90,
                        GenreId = 8,
                        ImageUrl = "http://www.hoyts.com.ar/files/movies/picture/HO00001351.jpg",
                        TrailerUrl = "http://www.youtube.com/watch?v=BpWqVRbx3o8",
                        RatingId = 2
                    });

                context.Movies.Add(
                    new Movie
                    {
                        Title = "Dragon Ball Z: La Batalla de los Dioses",
                        Synopsis = "La historia tiene lugar algunos años después de la feroz batalla contra Majin Buu. Bils, el Dios de la Destrucción que mantiene el balance del universo, despierta tras un largo sueño. Escuchando rumores sobre un Saiyan que derrotó a Freezer, Bils sale en la búsqueda del guerrero que logró esta victoria: Goku. El Rey Kai advierte a Goku y le dice que evite esta confrontación. Sin embargo, la emoción de no pelear contra un nuevo oponente desde hace años hace que Goku decida pelear. Desafortunadamente, Goku no es reto para el poder extremo de Bils y es derrotado. Bils se va, preguntando si hay alguien en la Tierra que sea merecedor de ser destruido. ¿Lograrán Goku y los guerreros Z detener al Dios de la Destrucción?",
                        DurationInMinutes = 85,
                        GenreId = 9,
                        ImageUrl = "http://www.hoyts.com.ar/files/movies/picture/HO00001369.jpg",
                        TrailerUrl = "http://www.youtube.com/watch?v=Lt4zb36L7lM",
                        RatingId = 1
                    });

                context.Movies.Add(
                    new Movie
                    {
                        Title = "Corazón de León",
                        Synopsis = "Ivana Cornejo se una exitosa abogada dedicada a los litigios de familia. Desde hace 3 años está divorciada de Diego Bisoni, también abogado y socio del estudio Cornejo/Bisoni. Tras la pérdida de su celular, Ivana recibe la llamada de alguien que lo encontró, con intenciones de devolvérselo. Es León Godoy, un arquitecto de gran renombre con una personalidad arrolladora: simpático, galante, carismático...y también divorciado. En la charla telefónica que mantienen se establece una empatía inquietante y ambos sienten un inmediato interés. Durante esa misma charla coordinan para encontrarse al día siguiente en una confitería y allí concretar la devolución del celular. Ivana es la primera en llegar y León llega unos minutos más tarde. Cuando lo ve queda perpleja, León es todo lo que ella percibió, pero mide 1,35 m. Es el hombre perfecto, pero...demasiado bajo. A partir de ese encuentro, Ivana buscará superar esos 45 cm que le faltan al hombre de su vida. Así se enfrentará a las convicciones de una sociedad implacable y a sus propios prejuicios, que exigen a los hombres el éxito económico, profesional y esos ineludibles 180 centímetros de altura.",
                        DurationInMinutes = 109,
                        GenreId = 1,
                        ImageUrl = "http://www.hoyts.com.ar/files/movies/picture/HO00001307.jpg",
                        TrailerUrl = "http://www.youtube.com/watch?v=2qMRdY35NjE",
                        RatingId = 1
                    });
            }

            context.SaveChanges();

            if (!context.Complexes.Any())
            {
                context.Complexes.Add(
                    new Complex
                        {
                            Address = "Cabildo y Juramento",
                            Name = "Belgrano",
                            Location = DbGeography.FromText("POINT(-58.456633 -34.561893)")
                        });

                context.Complexes.Add(
                    new Complex
                    {
                        Address = "Florida y Lavalle",
                        Name = "Microcentro",
                        Location = DbGeography.FromText("POINT(-58.375416 -34.602164)")
                    });
            }

            if (!context.RoomTypes.Any())
            {
                context.RoomTypes.Add(new RoomType { Description = "Rectangular" });
            }

            context.SaveChanges();

            if (!context.Rooms.Any())
            {
                context.Rooms.Add(
                    new Room 
                    {
                        ComplexId = 1, // Belgrano
                        Name = "Sala 1",
                        TypeId = 1,
                        Capacity = 200
                    });

                context.Rooms.Add(
                    new Room
                    {
                        ComplexId = 1, // Belgrano
                        Name = "Sala 2",
                        TypeId = 1,
                        Capacity = 250
                    });

                context.Rooms.Add(
                    new Room
                    {
                        ComplexId = 2, // Microcentro
                        Name = "Sala 1",
                        TypeId = 1,
                        Capacity = 150
                    });

                context.Rooms.Add(
                    new Room
                    {
                        ComplexId = 2, // Microcentro
                        Name = "Sala 2",
                        TypeId = 1,
                        Capacity = 100
                    });
            }

            context.SaveChanges();
        }
    }
}
