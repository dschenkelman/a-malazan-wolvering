package phoneticket.android.services.delete;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;

import phoneticket.android.utils.HttpClientFactory;
import android.os.AsyncTask;

public abstract class DeleteService extends AsyncTask<String, String, String> {

	protected boolean performingRequest;
	protected StatusLine statusLine;
	protected boolean connectionSuccess;

	@Override
	protected String doInBackground(String... uri) {
		connectionSuccess = false;
		HttpClient httpclient = HttpClientFactory.createClient();
		HttpDelete httpDelete = new HttpDelete(uri[0]);
		HttpResponse response = null;
		try {
			response = httpclient.execute(httpDelete);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			handleClientProtocolException(e);
		} catch (IOException e) {
			e.printStackTrace();
			handleStatusCodeNotOk(e,
					((null == statusLine) ? -1 : statusLine.getStatusCode()));
		} catch (RuntimeException e) {
			// This happens on FIUBA's Wifi
			e.printStackTrace();
			handleStatusCodeNotOk(e,
					((null == statusLine) ? -1 : statusLine.getStatusCode()));
		}
		String responseMessage = "";
		if (null != response) {
			connectionSuccess = true;
			statusLine = response.getStatusLine();
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			try {
				response.getEntity().writeTo(out);
			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			responseMessage = out.toString();
		}
		return responseMessage;
	}

	protected void handleStatusCodeNotOk(Exception e, int statusCode) {
		performingRequest = false;
	}

	protected void handleClientProtocolException(ClientProtocolException e) {
		performingRequest = false;
	}
}
