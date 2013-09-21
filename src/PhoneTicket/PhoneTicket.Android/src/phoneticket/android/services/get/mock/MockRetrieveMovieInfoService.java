package phoneticket.android.services.get.mock;

import android.os.AsyncTask;
import phoneticket.android.model.IMovie;
import phoneticket.android.model.Movie;
import phoneticket.android.services.get.IRetrieveMovieInfoService;
import phoneticket.android.services.get.IRetrieveMovieInfoServiceDelegate;

public class MockRetrieveMovieInfoService extends
		AsyncTask<String, String, String> implements IRetrieveMovieInfoService {

	private String title[] = { "El Conjuro", "Aprendices Fuera de L�nea",
			"Aviones", "Cacer�a Macabra", "Cenicienta", "Coraz�n de Le�n",
			"Dos Armas Letales", "Dragon Ball Z: La Batalla de los Dioses",
			"El Ataque", "S�ptimo" };

	private String synopsis[] = {
			"El conjuro se basa en los sucesos sobrenaturales que ocurrieron en la casa de Rhode Island de la familia Perron y que investigaron Ed y Lorraine Warren, expertos en actividades paranormales",
			"Billy (Vince Vaughn) y Nick (Owen Wilson) son vendedores cuyas carreras han sido bombardeada por el mundo digital. Intentando probar que no son obsoletos, ellos desaf�an todos los obst�culos y aplican para una pasant�a en Google, junto a un batall�n de brillantes estudiantes. Sin embargo, entrar a la pasant�a es tan s�lo el comienzo. Ahora deben competir contra un grupo de los mejores estudiantes del pa�s, genios de la tecnolog�a y Billy (Vince Vaughn) y Nick (Owen Wilson) les probar�n que la necesidad es la madre de toda reinvenci�n.",
			"Aviones est� protagonizada por Dusty, un veloz avi�n fumigador de gran coraz�n que sue�a con competir como corredor de alto vuelo. Pero Dusty no fue precisamente construido para competir y, adem�s, le teme a las alturas. As� es como recurre al aviador naval Skipper, quien lo ayuda a calificar para enfrentarse a Ripslinger, el campe�n defensor del circuito de carreras. Dusty deber� demostrar todo su valor para llegar a alturas inimaginables, y le conceder� al mundo la inspiraci�n necesaria para volar",
			"En esta divertida pel�cula de terror, en una reuni�n familiar, todo sale mal. Atrincherados en una retirada casa de vacaciones, la familia Davison es atacada violentamente por una pandilla de asesinos enmascarados. Desafortunadamente para los delincuentes, han elegido a la familia equivocada, ya que una de las v�ctimas parece tener una habilidad mortal para contraatacar.",
			"CENICIENTA, esta nueva interpretaci�n coreogr�fica fue estrenada por el prestigioso Ballet Nacional de Holanda en el Music Theatre de Amsterdam en diciembre de 2012, bajo la direcci�n del aclamado Christopher Wheeldon. Inspirado en la historia de la Cenicienta de los Hermanos Grimm, Wheeldon ha recreado una nueva interpretaci�n coreogr�fica junto al ballet de Holanda, acompa�ado por la grandiosa partitura de Prokofiev, representado bajo una gran puesta en escena y el vestuario a cargo del dise�ador brit�nico, Juli�n Crouch; hacen que esta obra sea conmovedoramente impactante y encantadora. El excelente reparto incluye el maridaje perfecto que conforman Anna Tsygankova y Matthew Golding, acompa�ados por los grandes bailarines del ballet nacional de Holanda. Cenicienta Ballet, te har� disfrutar de manera diferente y original el cl�sico cuento de princesas que te acompa�� durante varias generaciones",
			"Ivana Cornejo se una exitosa abogada dedicada a los litigios de familia. Desde hace 3 a�os est� divorciada de Diego Bisoni, tambi�n abogado y socio del estudio Cornejo/Bisoni. Tras la p�rdida de su celular, Ivana recibe la llamada de alguien que lo encontr�, con intenciones de devolv�rselo. Es Le�n Godoy, un arquitecto de gran renombre con una personalidad arrolladora: simp�tico, galante, carism�tico...y tambi�n divorciado. En la charla telef�nica que mantienen se establece una empat�a inquietante y ambos sienten un inmediato inter�s. Durante esa misma charla coordinan para encontrarse al d�a siguiente en una confiter�a y all� concretar la devoluci�n del celular. Ivana es la primera en llegar y Le�n llega unos minutos m�s tarde. Cuando lo ve queda perpleja, Le�n es todo lo que ella percibi�, pero mide 1,35 m. Es el hombre perfecto, pero...demasiado bajo. A partir de ese encuentro, Ivana buscar� superar esos 45 cm que le faltan al hombre de su vida. As� se enfrentar� a las convicciones de una sociedad implacable y a sus propios prejuicios, que exigen a los hombres el �xito econ�mico, profesional y esos ineludibles 180 cent�metros de altura.",
			"Dos armas letales nos habla de dos ladrones que preparan un gran atraco a un banco que, seg�n creen, pertenece a la mafia. Ambos son agentes encubiertos, uno de la DEA y otro de la Inteligencia Militar, pero ninguno conoce la verdadera identidad del otro. Adem�s el banco que acechan esconde una gran cantidad de dinero que la CIA utiliza para sus Black-Ops, u operaciones sin el control del gobierno.",
			"La historia tiene lugar algunos a�os despu�s de la feroz batalla contra Majin Buu. Bils, el Dios de la Destrucci�n que mantiene el balance del universo, despierta tras un largo sue�o. Escuchando rumores sobre un Saiyan que derrot� a Freezer, Bils sale en la b�squeda del guerrero que logr� esta victoria: Goku. El Rey Kai advierte a Goku y le dice que evite esta confrontaci�n. Sin embargo, la emoci�n de no pelear contra un nuevo oponente desde hace a�os hace que Goku decida pelear. Desafortunadamente, Goku no es reto para el poder extremo de Bils y es derrotado. Bils se va, preguntando si hay alguien en la Tierra que sea merecedor de ser destruido. �Lograr�n Goku y los guerreros Z detener al Dios de la Destrucci�n?",
			"Al polic�a del Capitolio John Cale le acaban de negar el trabajo de sus sue�os: proteger al presidente James Sawyer. Para no defraudar a su peque�a hija con la noticia, la lleva a hacer un tour por la Casa Blanca cuando el edificio es sorprendido por un grupo paramilitar fuertemente armado. Ahora, mientras en gobierno de la naci�n est� en medio del caos y el tiempo corre, depender� de Cale salvar al presidente, a su hija y al pa�s.",
			"Como cada d�a, Marcelo recoge a sus hijos en el piso de su exmujer. Como cada d�a juegan a 'a ver qui�n llega antes': ellos bajan por las escaleras, �l en el ascensor, un divertimento que a su expareja no le gusta. Pero cuando un d�a Marcelo llega el primero al piso de abajo los ni�os no est�n. No est�n en ning�n sitio. El miedo empieza a aflorar cuando una llamada telef�nica les catapulta al horror: un secuestrador pone precio para la liberaci�n de sus hijos. Marcelo tendr� que asumir la fragilidad de su mundo y decidir hasta d�nde est� dispuesto a llegar para recuperarlo" };

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
			"Musical", "Comedia", "Acci�n", "Infantil", "Acci�n", "Suspenso" };

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
