package phoneticket.android.services.get.mock;

import android.os.AsyncTask;
import phoneticket.android.model.IMovie;
import phoneticket.android.model.Movie;
import phoneticket.android.services.get.IRetrieveMovieInfoService;
import phoneticket.android.services.get.IRetrieveMovieInfoServiceDelegate;

public class MockRetrieveMovieInfoService extends
		AsyncTask<String, String, String> implements IRetrieveMovieInfoService {

	private String title[] = { "El Conjuro", "Aprendices Fuera de Línea",
			"Aviones", "Cacería Macabra", "Cenicienta", "Corazón de León",
			"Dos Armas Letales", "Dragon Ball Z: La Batalla de los Dioses",
			"El Ataque", "Séptimo" };

	private String synopsis[] = {
			"El conjuro se basa en los sucesos sobrenaturales que ocurrieron en la casa de Rhode Island de la familia Perron y que investigaron Ed y Lorraine Warren, expertos en actividades paranormales",
			"Billy (Vince Vaughn) y Nick (Owen Wilson) son vendedores cuyas carreras han sido bombardeada por el mundo digital. Intentando probar que no son obsoletos, ellos desafían todos los obstáculos y aplican para una pasantía en Google, junto a un batallón de brillantes estudiantes. Sin embargo, entrar a la pasantía es tan sólo el comienzo. Ahora deben competir contra un grupo de los mejores estudiantes del país, genios de la tecnología y Billy (Vince Vaughn) y Nick (Owen Wilson) les probarán que la necesidad es la madre de toda reinvención.",
			"Aviones está protagonizada por Dusty, un veloz avión fumigador de gran corazón que sueña con competir como corredor de alto vuelo. Pero Dusty no fue precisamente construido para competir y, además, le teme a las alturas. Así es como recurre al aviador naval Skipper, quien lo ayuda a calificar para enfrentarse a Ripslinger, el campeón defensor del circuito de carreras. Dusty deberá demostrar todo su valor para llegar a alturas inimaginables, y le concederá al mundo la inspiración necesaria para volar",
			"En esta divertida película de terror, en una reunión familiar, todo sale mal. Atrincherados en una retirada casa de vacaciones, la familia Davison es atacada violentamente por una pandilla de asesinos enmascarados. Desafortunadamente para los delincuentes, han elegido a la familia equivocada, ya que una de las víctimas parece tener una habilidad mortal para contraatacar.",
			"CENICIENTA, esta nueva interpretación coreográfica fue estrenada por el prestigioso Ballet Nacional de Holanda en el Music Theatre de Amsterdam en diciembre de 2012, bajo la dirección del aclamado Christopher Wheeldon. Inspirado en la historia de la Cenicienta de los Hermanos Grimm, Wheeldon ha recreado una nueva interpretación coreográfica junto al ballet de Holanda, acompañado por la grandiosa partitura de Prokofiev, representado bajo una gran puesta en escena y el vestuario a cargo del diseñador británico, Julián Crouch; hacen que esta obra sea conmovedoramente impactante y encantadora. El excelente reparto incluye el maridaje perfecto que conforman Anna Tsygankova y Matthew Golding, acompañados por los grandes bailarines del ballet nacional de Holanda. Cenicienta Ballet, te hará disfrutar de manera diferente y original el clásico cuento de princesas que te acompañó durante varias generaciones",
			"Ivana Cornejo se una exitosa abogada dedicada a los litigios de familia. Desde hace 3 años está divorciada de Diego Bisoni, también abogado y socio del estudio Cornejo/Bisoni. Tras la pérdida de su celular, Ivana recibe la llamada de alguien que lo encontró, con intenciones de devolvérselo. Es León Godoy, un arquitecto de gran renombre con una personalidad arrolladora: simpático, galante, carismático...y también divorciado. En la charla telefónica que mantienen se establece una empatía inquietante y ambos sienten un inmediato interés. Durante esa misma charla coordinan para encontrarse al día siguiente en una confitería y allí concretar la devolución del celular. Ivana es la primera en llegar y León llega unos minutos más tarde. Cuando lo ve queda perpleja, León es todo lo que ella percibió, pero mide 1,35 m. Es el hombre perfecto, pero...demasiado bajo. A partir de ese encuentro, Ivana buscará superar esos 45 cm que le faltan al hombre de su vida. Así se enfrentará a las convicciones de una sociedad implacable y a sus propios prejuicios, que exigen a los hombres el éxito económico, profesional y esos ineludibles 180 centímetros de altura.",
			"Dos armas letales nos habla de dos ladrones que preparan un gran atraco a un banco que, según creen, pertenece a la mafia. Ambos son agentes encubiertos, uno de la DEA y otro de la Inteligencia Militar, pero ninguno conoce la verdadera identidad del otro. Además el banco que acechan esconde una gran cantidad de dinero que la CIA utiliza para sus Black-Ops, u operaciones sin el control del gobierno.",
			"La historia tiene lugar algunos años después de la feroz batalla contra Majin Buu. Bils, el Dios de la Destrucción que mantiene el balance del universo, despierta tras un largo sueño. Escuchando rumores sobre un Saiyan que derrotó a Freezer, Bils sale en la búsqueda del guerrero que logró esta victoria: Goku. El Rey Kai advierte a Goku y le dice que evite esta confrontación. Sin embargo, la emoción de no pelear contra un nuevo oponente desde hace años hace que Goku decida pelear. Desafortunadamente, Goku no es reto para el poder extremo de Bils y es derrotado. Bils se va, preguntando si hay alguien en la Tierra que sea merecedor de ser destruido. ¿Lograrán Goku y los guerreros Z detener al Dios de la Destrucción?",
			"Al policía del Capitolio John Cale le acaban de negar el trabajo de sus sueños: proteger al presidente James Sawyer. Para no defraudar a su pequeña hija con la noticia, la lleva a hacer un tour por la Casa Blanca cuando el edificio es sorprendido por un grupo paramilitar fuertemente armado. Ahora, mientras en gobierno de la nación está en medio del caos y el tiempo corre, dependerá de Cale salvar al presidente, a su hija y al país.",
			"Como cada día, Marcelo recoge a sus hijos en el piso de su exmujer. Como cada día juegan a 'a ver quién llega antes': ellos bajan por las escaleras, él en el ascensor, un divertimento que a su expareja no le gusta. Pero cuando un día Marcelo llega el primero al piso de abajo los niños no están. No están en ningún sitio. El miedo empieza a aflorar cuando una llamada telefónica les catapulta al horror: un secuestrador pone precio para la liberación de sus hijos. Marcelo tendrá que asumir la fragilidad de su mundo y decidir hasta dónde está dispuesto a llegar para recuperarlo" };

	private String posterURL[] = {
			"http://www.hoyts.com.ar/files/movies/picture/HO00001296.jpg",
			"http://www.hoyts.com.ar/files/movies/picture/HO00001317.jpg",
			"http://www.hoyts.com.ar/files/movies/picture/HO00001342.jpg",
			"http://www.hoyts.com.ar/files/movies/picture/HO00001366.jpg",
			"http://www.hoyts.com.ar/files/movies/picture/HO00001370.jpg",
			"http://www.hoyts.com.ar/files/movies/picture/HO00001307.jpg",
			"http://www.hoyts.com.ar/files/movies/picture/HO00001380.jpg",
			"http://www.hoyts.com.ar/files/movies/picture/HO00001369.jpg",
			"http://www.hoyts.com.ar/files/movies/picture/HO00001348.jpg",
			"http://www.hoyts.com.ar/files/movies/picture/HO00001351.jpg" };

	private String clasification[] = { "P16", "P13", "ATPr", "P16", "ATP",
			"ATP", "P16r", "ATP", "P13", "P13" };

	private int duration[] = { 112, 119, 96, 95, 118, 109, 109, 85, 131, 90 };

	private String gendre[] = { "Terror", "Comedia", "Infantil", "Terror",
			"Musical", "Comedia", "Acción", "Infantil", "Acción", "Suspenso" };

	private String youtubeURL[] = {
			"http://www.youtube.com/watch?v=OJgDCNyfWfQ",
			"http://www.youtube.com/watch?v=30v_FQxGmaA",
			"http://www.youtube.com/watch?v=GpTivtieQq8",
			"http://www.youtube.com/watch?v=ocwdjfpteMA",
			"http://www.youtube.com/watch?v=eb_4NENERzY",
			"http://www.youtube.com/watch?v=2qMRdY35NjE",
			"http://www.youtube.com/watch?v=E1optOUhHU8",
			"http://www.youtube.com/watch?v=Lt4zb36L7lM",
			"http://www.youtube.com/watch?v=F2shqzlRdmk",
			"http://www.youtube.com/watch?v=BpWqVRbx3o8" };

	private int movieId;
	private IRetrieveMovieInfoServiceDelegate delegate;

	@Override
	public void retrieveMovieInfo(IRetrieveMovieInfoServiceDelegate delegate,
			int movieId) {
		this.movieId = movieId;
		this.delegate = delegate;
		execute("");
	}

	private IMovie createMockMovie(int movieId) {
		return new Movie(movieId, title[movieId], synopsis[movieId],
				posterURL[movieId], clasification[movieId], duration[movieId],
				gendre[movieId], youtubeURL[movieId]);
	}

	@Override
	protected String doInBackground(String... arg0) {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return "";
	}

	@Override
	protected void onPostExecute(String result) {
		if (0 > movieId || title.length <= movieId)
			delegate.retrieveMovieInfoFinishWithError(this, -1);
		else
			delegate.retrieveMovieInfoFinish(this, createMockMovie(movieId));
	}
}
