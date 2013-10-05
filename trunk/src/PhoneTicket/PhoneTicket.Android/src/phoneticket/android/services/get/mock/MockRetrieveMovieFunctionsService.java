package phoneticket.android.services.get.mock;

import java.util.ArrayList;
import java.util.Collection;

import android.os.AsyncTask;
import phoneticket.android.model.IFunction;
import phoneticket.android.model.IMovieFunctions;
import phoneticket.android.services.get.IRetrieveMovieFunctionsService;
import phoneticket.android.services.get.IRetrieveMovieFunctionsServiceDelegate;

public class MockRetrieveMovieFunctionsService extends
		AsyncTask<String, String, String> implements
		IRetrieveMovieFunctionsService {

	private int movieId;
	private IRetrieveMovieFunctionsServiceDelegate delegate;

	private IMovieFunctions createMockMovieFunctions(final int movieId) {
		IMovieFunctions movieFunctions = new IMovieFunctions() {
			@Override
			public int getMovieId() {
				return movieId;
			}
			@Override
			public Collection<IFunction> getFunctions() {
				Collection<IFunction> functions = new ArrayList<IFunction>();
				functions.add(new IFunction() {
					@Override
					public String getTime() {
						return "20:30";
					}
					@Override
					public int getId() {
						return 1;
					}
					@Override
					public String getDay() {
						return "Lunes";
					}
				});
				functions.add(new IFunction() {
					@Override
					public String getTime() {
						return "23:30";
					}
					@Override
					public int getId() {
						return 2;
					}
					@Override
					public String getDay() {
						return "Lunes";
					}
				});
				functions.add(new IFunction() {
					@Override
					public String getTime() {
						return "16:15";
					}
					@Override
					public int getId() {
						return 3;
					}
					@Override
					public String getDay() {
						return "Martes";
					}
				});
				functions.add(new IFunction() {
					@Override
					public String getTime() {
						return "12:30";
					}
					@Override
					public int getId() {
						return 4;
					}
					@Override
					public String getDay() {
						return "Miercoles";
					}
				});
				functions.add(new IFunction() {
					@Override
					public String getTime() {
						return "19:40";
					}
					@Override
					public int getId() {
						return 5;
					}
					@Override
					public String getDay() {
						return "Miercoles";
					}
				});
				return functions;
			}
			@Override
			public String getCinemaName() {
				return "Cinemar1";
			}
			@Override
			public int getCinemaId() {
				return 1;
			}
		};
		return movieFunctions;
	}
	
	@Override
	public void retrieveMovieFunctions(
			IRetrieveMovieFunctionsServiceDelegate delegate, int movieId) {
		this.movieId = movieId;
		this.delegate = delegate;
		execute("");
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
		if (0 > movieId)
			delegate.retrieveMovieFunctionsFinishWithError(this, -1);
		else
			delegate.retrieveMovieFunctionsFinish(this, createMockMovieFunctions(movieId));
	}
}
