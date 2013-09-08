package phoneticket.android.activities.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class ConfirmUserRegisterDialogFragment extends DialogFragment {

    public interface IConfirmUserRegisterDialogDelegate {
        public void onDialogPositiveClick(DialogFragment dialog);
    }
	
    private IConfirmUserRegisterDialogDelegate delegate;
    
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage("Se le ha enviado un e-mail a la casilla de correo. Haga click en el link para confirmar su cuenta.")
        		.setTitle("Confirme su cuenta")
        		 .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       delegate.onDialogPositiveClick(ConfirmUserRegisterDialogFragment.this);
                   }
               });
        return builder.create();
    }
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            delegate = (IConfirmUserRegisterDialogDelegate) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement " + IConfirmUserRegisterDialogDelegate.class.toString());
        }
    }
}
