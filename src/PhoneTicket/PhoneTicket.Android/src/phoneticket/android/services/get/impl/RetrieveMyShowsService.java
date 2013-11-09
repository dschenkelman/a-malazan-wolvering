package phoneticket.android.services.get.impl;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import com.google.gson.Gson;

import phoneticket.android.model.IMyShow;
import phoneticket.android.model.MyShow;
import phoneticket.android.services.get.GetService;
import phoneticket.android.services.get.IRetrieveMyShowsService;
import phoneticket.android.services.get.IRetrieveMyShowsServiceDelegate;
import phoneticket.android.utils.APIService;

public class RetrieveMyShowsService extends GetService implements
		IRetrieveMyShowsService {

	private IRetrieveMyShowsServiceDelegate delegate;

	public RetrieveMyShowsService() {
		performingRequest = false;
	}

	@Override
	public void retrieveMyShows(IRetrieveMyShowsServiceDelegate delegate) {
		if (true == performingRequest || null == delegate) {
			return;
		}
		this.delegate = delegate;
		execute(APIService.getRetrieveMyShowsServiceGetURL());
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		List<IMyShow> myShows = new ArrayList<IMyShow>();
		if (!isStatusOk || hasCLientProtocolRecieveException || null == result) {
			delegate.retrieveMyShowsServiceFinishedWithError(this, null == statusLine ? 1 : statusLine.getStatusCode());
		} else {
			try {
				JSONArray jsonMyShows = new JSONArray(result);
				for (int i = 0; i < jsonMyShows.length(); i++) {
					JSONObject jsonMyShow = jsonMyShows
							.getJSONObject(i);
					IMyShow myShow = new Gson().fromJson(
							jsonMyShow.toString(), MyShow.class);
					if (null != myShow) {
						myShows.add(myShow);
					}
				}
				Collections.sort(myShows, new Comparator<IMyShow>() {
					@SuppressLint("SimpleDateFormat")
					@Override
					public int compare(IMyShow lhs, IMyShow rhs) {
						SimpleDateFormat sdf = new SimpleDateFormat(
								"dd'/'MM mm:HH'Hs'");
						Date ldate = null;
						Date rdate = null;
						try {
							ldate = sdf.parse(lhs.getShowDateAndTime());
							rdate = sdf.parse(rhs.getShowDateAndTime());
						} catch (ParseException e) {
							e.printStackTrace();
							return 0;
						}
						return ldate.compareTo(rdate);
					}
				});
				delegate.retrieveMyShowsServiceFinished(this, myShows);
			} catch (JSONException e) {
				e.printStackTrace();
				delegate.retrieveMyShowsServiceFinishedWithError(this, 2);
			}
		}
		delegate = null;
		performingRequest = false;
	}

	@Override
	protected void handleStatusCodeNotOk(IOException e, int statusCode) {
		super.handleStatusCodeNotOk(e, statusCode);
		isStatusOk = false;
	}

	@Override
	protected void handleClientProtocolException(ClientProtocolException e) {
		super.handleClientProtocolException(e);
		hasCLientProtocolRecieveException = true;
	}
}
