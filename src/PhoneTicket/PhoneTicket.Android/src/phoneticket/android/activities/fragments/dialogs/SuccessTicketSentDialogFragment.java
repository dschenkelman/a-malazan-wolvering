package phoneticket.android.activities.fragments.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class SuccessTicketSentDialogFragment extends DialogFragment {

	private OnClickListener listener;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		builder.setTitle("OK").setMessage("Acción Realizada con éxito")
				.setPositiveButton("Continuar", this.listener);
		return builder.create();
	}

	public void setListener(OnClickListener listener) {
		this.listener = listener;
	}

}
