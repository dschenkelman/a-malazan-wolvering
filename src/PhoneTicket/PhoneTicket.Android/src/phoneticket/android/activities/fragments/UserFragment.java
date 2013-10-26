package phoneticket.android.activities.fragments;

import com.google.inject.Inject;

import phoneticket.android.R;
import phoneticket.android.activities.LoginActivity;
import phoneticket.android.model.User;
import phoneticket.android.services.get.IRetrieveUserInfoService;
import phoneticket.android.services.get.IRetrieveUserInfoServiceDelegate;
import phoneticket.android.utils.UserManager;
import roboguice.fragment.RoboFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class UserFragment extends RoboFragment implements
		IRetrieveUserInfoServiceDelegate {

	@Inject
	private IRetrieveUserInfoService userInfoService;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		UserManager.initialize(getActivity().getPreferences(0));
		View view = inflater.inflate(R.layout.fragment_user, container, false);
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();

		if (UserManager.getInstance().isUserLoged()) {
			if (shouldRetrieveUserInfo()) {
				showLoadingLayoutVisibility();
				userInfoService.retrieveUserInfo(this, UserManager
						.getInstance().getLogedUser().getDni());
			} else {
				showLogedLayoutVisibility();
				showUserInfo();
			}
		} else {
			showLoginLayoutVisibility();
		}

		Button loginButton = (Button) getView().findViewById(R.id.loginButton);
		loginButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onLoginAction();
			}
		});

		Button logoutButton = (Button) getView()
				.findViewById(R.id.logoutButton);
		logoutButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onLogoutAction();
			}
		});
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	private boolean shouldRetrieveUserInfo() {
		User user = UserManager.getInstance().getLogedUser();
		boolean result = null == user.getFirstName();
		result = result || null != user.getLastName();
		result = result || null != user.getEmail();
		result = result || null != user.getBirthday();
		result = result || null != user.getCellPhone();
		return result;
	}

	protected void onLoginAction() {
		Intent intent = new Intent(getActivity(), LoginActivity.class);
		startActivity(intent);
	}

	protected void onLogoutAction() {
		UserManager.getInstance().logoutUser();
		showLoginLayoutVisibility();
	}

	private void showUserInfo() {
		TextView nameView = (TextView) getView()
				.findViewById(R.id.nameTextView);
		TextView dniView = (TextView) getView().findViewById(R.id.dniTextView);
		TextView emailView = (TextView) getView().findViewById(
				R.id.emailTextView);
		TextView birthdayView = (TextView) getView().findViewById(
				R.id.birthdayTextView);
		TextView mobilePhoneView = (TextView) getView().findViewById(
				R.id.mobilePhoneTextView);

		User user = UserManager.getInstance().getLogedUser();

		nameView.setText((null != user.getFirstName() ? user.getFirstName()
				+ " " : "")
				+ (null != user.getLastName() ? user.getLastName() : ""));
		dniView.setText(user.getDni() + "");
		emailView.setText(null != user.getEmail() ? user.getEmail() : "-");
		birthdayView.setText(null != user.getBirthday() ? user.getBirthday()
				: "-");
		mobilePhoneView.setText(null != user.getCellPhone() ? user
				.getCellPhone() : "-");
	}

	private void showLoginLayoutVisibility() {
		LinearLayout noSessionView = (LinearLayout) getView().findViewById(
				R.id.noSessionView);
		RelativeLayout loadingView = (RelativeLayout) getView().findViewById(
				R.id.loadingDataLayout);
		LinearLayout userView = (LinearLayout) getView().findViewById(
				R.id.userView);

		noSessionView.setVisibility(LinearLayout.VISIBLE);
		loadingView.setVisibility(RelativeLayout.GONE);
		userView.setVisibility(LinearLayout.GONE);
	}

	private void showLoadingLayoutVisibility() {
		LinearLayout noSessionView = (LinearLayout) getView().findViewById(
				R.id.noSessionView);
		RelativeLayout loadingView = (RelativeLayout) getView().findViewById(
				R.id.loadingDataLayout);
		LinearLayout userView = (LinearLayout) getView().findViewById(
				R.id.userView);

		noSessionView.setVisibility(LinearLayout.GONE);
		loadingView.setVisibility(RelativeLayout.VISIBLE);
		userView.setVisibility(LinearLayout.GONE);
	}

	private void showLogedLayoutVisibility() {
		LinearLayout noSessionView = (LinearLayout) getView().findViewById(
				R.id.noSessionView);
		RelativeLayout loadingView = (RelativeLayout) getView().findViewById(
				R.id.loadingDataLayout);
		LinearLayout userView = (LinearLayout) getView().findViewById(
				R.id.userView);

		noSessionView.setVisibility(LinearLayout.GONE);
		loadingView.setVisibility(RelativeLayout.GONE);
		userView.setVisibility(LinearLayout.VISIBLE);
	}

	@Override
	public void retrieveUserInfoFinish(
			IRetrieveUserInfoService retrieveUserInfoService) {
		showLogedLayoutVisibility();
		showUserInfo();
	}

	@Override
	public void retrieveUserInfoFinishWithError(
			IRetrieveUserInfoService retrieveUserInfoService, int errorCode) {
		onLogoutAction();
	}
}
