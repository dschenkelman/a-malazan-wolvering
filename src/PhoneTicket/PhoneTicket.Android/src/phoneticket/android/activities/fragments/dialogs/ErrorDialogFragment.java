package phoneticket.android.activities.fragments.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class ErrorDialogFragment extends DialogFragment {

	private CharSequence message;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		builder.setTitle("Error al reservar. Intente nuevamente.")
				.setMessage(this.message).setPositiveButton("Continuar", null);
		return builder.create();
	}

	public void setMessage(CharSequence message) {
		this.message = message;
	}
}
