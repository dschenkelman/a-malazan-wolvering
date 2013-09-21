package phoneticket.android.services.get.mock;

import java.util.ArrayList;
import java.util.Collection;

import phoneticket.android.model.IMovieListItem;
import phoneticket.android.model.MovieListItem;
import phoneticket.android.services.get.IRetrieveMovieListService;
import phoneticket.android.services.get.IRetrieveMovieListServiceDelegate;

public class MockRetrieveMovieListService implements IRetrieveMovieListService {

	private String urls[] = {
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

	@Override
	public void retrieveMovieList(IRetrieveMovieListServiceDelegate delegate) {
		Collection<IMovieListItem> movieList = new ArrayList<IMovieListItem>();
		for (int i = 0; i < urls.length; i++) {
			String url = urls[i];
			IMovieListItem movie = new MovieListItem(i, url, url);
			movieList.add(movie);
		}
		delegate.retrieveMovieListFinish(this, movieList);
	}

}
