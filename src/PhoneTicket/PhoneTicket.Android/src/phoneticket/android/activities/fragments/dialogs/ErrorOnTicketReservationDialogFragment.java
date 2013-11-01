package phoneticket.android.activities.fragments.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class ErrorOnTicketReservationDialogFragment extends DialogFragment {
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		builder.setMessage("Error")
				.setTitle("Error al reservar. Intente nuevamente.")
				.setPositiveButton("Continuar", null);
		return builder.create();
	}
}
