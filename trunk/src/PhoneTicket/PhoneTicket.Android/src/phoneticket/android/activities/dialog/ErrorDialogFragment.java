package phoneticket.android.activities.dialog;

import phoneticket.android.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class ErrorDialogFragment extends DialogFragment
{
	private IErrorDialogDataSource datasource;
	
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        
        
        builder.setMessage(datasource.getErrorMessage())
        		.setTitle(datasource.getErrorTitle())
        		.setPositiveButton(R.string.errorDialogContinue, null);
        return builder.create();
    }
    
    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        try
        {
        	datasource = (IErrorDialogDataSource)activity;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(activity.toString()
                    + " must implement IErrorDialogDataSource");
        }
    }

    public interface IErrorDialogDataSource
    {
        String getErrorMessage();
        String getErrorTitle();
    }
}
