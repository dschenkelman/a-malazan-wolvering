package phoneticket.android.activities.dialog;

import phoneticket.android.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class ConfirmBackActionDialogFragment extends DialogFragment {
	
	private IConfirmBackActionDialogDelegate delegate;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		builder.setMessage("¿Desea canclar la compra?")
				.setPositiveButton(R.string.confirmDialogGoBack, new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                       delegate.onDialogPositiveClick();
	                   }
	               })
				.setNegativeButton(R.string.confirmDialogStay, null);
		return builder.create();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			delegate = (IConfirmBackActionDialogDelegate) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement IConfirmBackActionDialogDelegate");
		}
	}

	public interface IConfirmBackActionDialogDelegate {
		void onDialogPositiveClick();
	}
}
