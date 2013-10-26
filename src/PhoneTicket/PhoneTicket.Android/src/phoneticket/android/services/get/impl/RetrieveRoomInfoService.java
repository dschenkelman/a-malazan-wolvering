package phoneticket.android.services.get.impl;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

import phoneticket.android.services.get.GetService;
import phoneticket.android.services.get.IRetrieveRoomInfoService;
import phoneticket.android.services.get.IRetrieveRoomInfoServiceDelegate;
import phoneticket.android.utils.APIService;

public class RetrieveRoomInfoService extends GetService implements
		IRetrieveRoomInfoService {

	private IRetrieveRoomInfoServiceDelegate delegate;

	public RetrieveRoomInfoService() {
		performingRequest = false;
	}

	@Override
	public void retrieveRoomInfo(IRetrieveRoomInfoServiceDelegate delegate,
			int roomId) {
		if (true == performingRequest || null == delegate) {
			return;
		}
		this.delegate = delegate;
		execute(APIService.getRetrieveRoomInfoServiceGetURL(String
				.valueOf(roomId)));
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if (!isStatusOk || hasCLientProtocolRecieveException || null == result) {
			delegate.retrieveRoomInfoFinishWithError(this,
					null == statusLine ? 1 : statusLine.getStatusCode());
		} else {
			try {
				JSONArray jsonRoom = new JSONArray(result);
				LinkedList<Collection<Integer>> room = new LinkedList<Collection<Integer>>();
				for (int i = 0; i < jsonRoom.length(); i++) {
					JSONArray jsonRow = jsonRoom.getJSONArray(i);
					LinkedList<Integer> row = new LinkedList<Integer>();
					for (int j = 0; j < jsonRow.length(); j++) {
						JSONObject jsonArmchair = jsonRoom.getJSONObject(j);
						Integer armchair = new Gson().fromJson(
								jsonArmchair.toString(), Integer.class);
						if (null != armchair) {
							row.addLast(armchair);
						}
					}
					room.addLast(row);
				}
				delegate.retrieveRoomInfoFinish(this, room);
			} catch (JSONException e) {
				e.printStackTrace();
				delegate.retrieveRoomInfoFinishWithError(this, 2);
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
