package phoneticket.android.activities.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.ProgressBar;

public class ProgressDialogFragment extends DialogFragment {
	private IProgressDialogDataSource datasource;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		builder.setTitle(datasource.getProgressMessageTitle()).setView(
				new ProgressBar(getActivity().getBaseContext()));
		return builder.create();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			datasource = (IProgressDialogDataSource) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement IProgressDialogDataSource");
		}
	}

	public interface IProgressDialogDataSource {
		String getProgressMessageTitle();
	}
}
