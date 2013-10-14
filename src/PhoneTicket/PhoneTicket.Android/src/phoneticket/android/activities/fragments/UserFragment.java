package phoneticket.android.activities.fragments;

import phoneticket.android.R;
import phoneticket.android.activities.LoginActivity;
import phoneticket.android.activities.dialog.MessageDialogFragment;
import phoneticket.android.utils.UserManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class UserFragment extends Fragment {

	private Button logoutButton;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		UserManager.initialize(getActivity().getPreferences(0));
		View view = inflater.inflate(R.layout.fragment_user, container, false);
		logoutButton = (Button) view.findViewById(R.id.logoutButton);
		logoutButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onLogoutButtonTapped(v);
			}
		});
		Button actionButton = (Button) view.findViewById(R.id.actionButton);
		actionButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onActionButtonTapped(v);
			}
		});

		return view;
	}

	private void updateLogoutButtonVisibility() {

		int visibility = UserManager.getInstance().isUserLoged() ? Button.VISIBLE
				: Button.GONE;
		logoutButton.setVisibility(visibility);
	}

	public void onActionButtonTapped(View sender) {
		if (UserManager.getInstance().isUserLoged()) {
			MessageDialogFragment dialog = new MessageDialogFragment();
			dialog.show(getActivity().getSupportFragmentManager(),
					"dialog.actionmessage");
		} else {
			Intent intent = new Intent(getActivity(), LoginActivity.class);
			startActivity(intent);
		}
	}

	public void onLogoutButtonTapped(View sender) {
		UserManager.getInstance().logoutUser();
		updateLogoutButtonVisibility();
		((TextView) getView().findViewById(R.id.loginUser)).setText("");
	}
}
