package phoneticket.android.services.put;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;

import phoneticket.android.utils.HttpClientFactory;

import android.os.AsyncTask;

public abstract class PutService extends AsyncTask<String, String, String>
{
	protected boolean performingRequest;
	protected StatusLine statusLine;

	@Override
	protected String doInBackground(String... uri)
	{
		HttpClient httpclient = HttpClientFactory.createClient();
		HttpPut httppost = new HttpPut(uri[0]);
	    HttpResponse response = null;
	    try 
	    {
	    	String object = generatePutBodyObject();
	    	
	    	StringEntity se = new StringEntity(object);

	        se.setContentEncoding("UTF-8");
	        se.setContentType("application/json");
	        
	        httppost.setEntity(se);
	    	response = httpclient.execute(httppost);
            statusLine = response.getStatusLine();
	    }
	    catch (ClientProtocolException e)
	    {
            handleClientProtocolException(e);
        }
        catch (IOException e)
        {
        	handleStatusCodeNotOk(e, ((null == statusLine) ? -1 : statusLine.getStatusCode()));
	    }

	    if(null != response.getEntity())
	    {
	        ByteArrayOutputStream out = new ByteArrayOutputStream();
	        try
	        {
				response.getEntity().writeTo(out);
			} 
	        catch (IOException e) { e.printStackTrace(); }
	        
	        try
	        {
				out.close();
			} 
	        catch (IOException e) { e.printStackTrace(); }
	        return out.toString();
	    }
	    return statusLine.getStatusCode()+"";
	}
	
	abstract protected String generatePutBodyObject();

	protected void handleStatusCodeNotOk(IOException e, int statusCode)
	{
        performingRequest = false;
	}

	protected void handleClientProtocolException(ClientProtocolException e)
	{
        performingRequest = false;
	}
}
