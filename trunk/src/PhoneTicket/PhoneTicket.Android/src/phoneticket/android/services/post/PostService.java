package phoneticket.android.services.post;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;

public abstract class PostService extends AsyncTask<String, String, String> {

	protected boolean performingRequest;
	protected StatusLine statusLine;
	protected boolean connectionSuccess;

	@Override
	protected String doInBackground(String... uri) {
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(uri[0]);
		HttpResponse response = null;
		try {
			String object = generatePostBodyObject();

			StringEntity se = new StringEntity(object);

			se.setContentEncoding("UTF-8");
			se.setContentType("application/json");

			httppost.setEntity(se);
			response = httpclient.execute(httppost);
		} catch (ClientProtocolException e) {
			handleClientProtocolException(e);
		} catch (IOException e) {
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
			responseMessage  = out.toString();
		}
		return responseMessage ;
	}

	abstract protected String generatePostBodyObject();

	protected void handleStatusCodeNotOk(IOException e, int statusCode) {
		performingRequest = false;
	}

	protected void handleClientProtocolException(ClientProtocolException e) {
		performingRequest = false;
	}
}
