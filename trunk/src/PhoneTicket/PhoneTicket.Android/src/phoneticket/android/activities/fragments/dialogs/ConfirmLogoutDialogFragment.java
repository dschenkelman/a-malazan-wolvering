package phoneticket.android.activities.fragments.dialogs;

import phoneticket.android.activities.fragments.UserFragment;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class ConfirmLogoutDialogFragment extends DialogFragment {

	public interface IConfirmLogoutDialogFragmentDelegate {
		public void onDialogPositiveClick(DialogFragment dialog);
	}

	private IConfirmLogoutDialogFragmentDelegate delegate;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		builder.setMessage("¿Desea cerrar la sesión?")
				.setTitle("Confirme la operación")
				.setPositiveButton("Aceptar",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								if (null != delegate)
									delegate.onDialogPositiveClick(ConfirmLogoutDialogFragment.this);
							}
						}).setNegativeButton("Cancelar", null);
		return builder.create();
	}

	@SuppressLint("NewApi")
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			delegate = (IConfirmLogoutDialogFragmentDelegate) getFragmentManager()
					.findFragmentByTag(UserFragment.TAG);
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement "
					+ IConfirmLogoutDialogFragmentDelegate.class.toString());
		}
	}
}
