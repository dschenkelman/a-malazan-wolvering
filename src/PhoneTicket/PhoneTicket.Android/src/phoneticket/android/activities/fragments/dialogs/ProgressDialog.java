package phoneticket.android.activities.fragments.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.widget.ProgressBar;

public class ProgressDialog extends DialogFragment {
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		builder.setTitle("Espere...").setView(
				new ProgressBar(getActivity().getBaseContext()));
		return builder.create();
	}
}
