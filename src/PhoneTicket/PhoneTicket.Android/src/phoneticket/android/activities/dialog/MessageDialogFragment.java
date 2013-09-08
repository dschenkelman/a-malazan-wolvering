package phoneticket.android.activities.dialog;

import phoneticket.android.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class MessageDialogFragment extends DialogFragment
{
	private IMessageDialogDataSource datasource;
	
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        
        
        builder.setMessage(datasource.getMessage())
        		.setTitle(datasource.getMessageTitle())
        		.setPositiveButton(R.string.errorDialogContinue, null);
        return builder.create();
    }
    
    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        try
        {
        	datasource = (IMessageDialogDataSource)activity;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(activity.toString()
                    + " must implement IMessageDialogDataSource");
        }
    }

    public interface IMessageDialogDataSource
    {
        String getMessage();
        String getMessageTitle();
    }
}
