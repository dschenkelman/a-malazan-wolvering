package phoneticket.android.services.post;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import phoneticket.android.model.User;
import phoneticket.android.utils.APIService;

import com.google.gson.Gson;

public class RegisterUserService extends PostService implements
		IRegisterUserService {

	private User postBodyObject;
	private IRegisterUserServiceDelegate delegate;
	
	public RegisterUserService()
	{
		performingRequest = false;
	}
	
	@Override
	public void registerUser(User user, IRegisterUserServiceDelegate delegate)
	{
		if(performingRequest || null == delegate || null == user)
			return;
		postBodyObject = user;
		performingRequest = true;
		this.delegate = delegate;
		execute(APIService.registerUserPostURL());
	}
	
    @Override
    protected void onPostExecute(String result) 
    {
        super.onPostExecute(result);
        if(performingRequest)
        {
            if(201 == statusLine.getStatusCode())
            {
            	//postBodyObject.setID(UUID.fromString(result.substring(1, result.length() - 1)));
                delegate.registerUserFinish(this, postBodyObject);
            }
            else
            {
            	delegate.registerUserFinishWithError(this, "registerUserFinishWithError");
            }
            performingRequest = false;
            delegate = null;
        }
    }

    @Override
    protected void handleStatusCodeNotOk(IOException e, int statusCode)
	{
    	delegate.registerUserFinishWithError(this, "handleStatusCodeNotOk");
        performingRequest = false;
        delegate = null;
	}
    
    @Override
	protected void handleClientProtocolException(ClientProtocolException e)
	{
    	delegate.registerUserFinishWithError(this, "handleClientProtocolException");
        performingRequest = false;
        delegate = null;
	}

	@Override
	protected String generatePostBodyObject()
	{
		String jsonString = new Gson().toJson(postBodyObject, User.class);
		return jsonString;
	}

}
