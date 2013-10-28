package phoneticket.android.activities.dialog;

import phoneticket.android.activities.fragments.DetailUserShowFragment;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class ConfirmShowReserveCancelationDialogFragment extends DialogFragment {

	public interface IConfirmShowReserveCancelationDialogDelegate {
		public void onDialogPositiveClick(DialogFragment dialog);
	}

	private IConfirmShowReserveCancelationDialogDelegate delegate;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		builder.setMessage("¿Esta seguro que desea cancelar la reserva?")
				.setTitle("Confirme la operación")
				.setPositiveButton("Continuar",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								if (null != delegate)
									delegate.onDialogPositiveClick(ConfirmShowReserveCancelationDialogFragment.this);
							}
						})
				.setNegativeButton("Cancelar", null);
		return builder.create();
	}

	@SuppressLint("NewApi")
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			delegate = (IConfirmShowReserveCancelationDialogDelegate) getFragmentManager()
					.findFragmentByTag(DetailUserShowFragment.TAG);
		} catch (ClassCastException e) {
			throw new ClassCastException(
					activity.toString()
							+ " must implement "
							+ IConfirmShowReserveCancelationDialogDelegate.class
									.toString());
		}
	}
}
