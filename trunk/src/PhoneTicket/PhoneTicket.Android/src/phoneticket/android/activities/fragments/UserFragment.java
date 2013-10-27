package phoneticket.android.activities.fragments;

import com.google.inject.Inject;

import phoneticket.android.R;
import phoneticket.android.activities.LoginActivity;
import phoneticket.android.activities.interfaces.IUserShowsListener;
import phoneticket.android.model.User;
import phoneticket.android.services.get.IRetrieveUserInfoService;
import phoneticket.android.services.get.IRetrieveUserInfoServiceDelegate;
import phoneticket.android.utils.UserManager;
import roboguice.fragment.RoboFragment;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class UserFragment extends RoboFragment implements
		IRetrieveUserInfoServiceDelegate {

	@Inject
	private IRetrieveUserInfoService userInfoService;

	private boolean ignoreServicesCallbacks;

	private IUserShowsListener myFunctionsListener;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_user, container, false);
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();

		ignoreServicesCallbacks = false;

		if (UserManager.getInstance().isUserLoged()) {
			if (shouldRetrieveUserInfo()) {
				onLoadDataAction();
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

		Button reloadDataButton = (Button) getView().findViewById(
				R.id.reloadDataButton);
		reloadDataButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onLoadDataAction();
			}
		});

		Button myFunctionsButton = (Button) getView().findViewById(
				R.id.buyedAndReservedFunctionsButton);
		myFunctionsButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				myFunctionsListener.onShowUserShowsAction();
			}
		});
	}

	@Override
	public void onPause() {
		super.onPause();
		ignoreServicesCallbacks = true;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			myFunctionsListener = (IUserShowsListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement IMyFunctionsListener");
		}
	}

	private boolean shouldRetrieveUserInfo() {
		User user = UserManager.getInstance().getLogedUser();
		boolean result = null == user.getFirstName()
				|| 0 == user.getFirstName().length();
		result = result || null == user.getLastName()
				|| 0 == user.getLastName().length();
		result = result || null == user.getEmail()
				|| 0 == user.getEmail().length();
		result = result || null == user.getBirthday()
				|| 0 == user.getBirthday().length();
		result = result || null == user.getCellPhone()
				|| 0 == user.getCellPhone().length();
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

	protected void onLoadDataAction() {
		showLoadingLayoutVisibility();
		userInfoService.retrieveUserInfo(this, UserManager.getInstance()
				.getLogedUser().getDni());
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
		LinearLayout errorView = (LinearLayout) getView().findViewById(
				R.id.errorLoadingDataView);
		RelativeLayout loadingView = (RelativeLayout) getView().findViewById(
				R.id.loadingDataLayout);
		ScrollView userView = (ScrollView) getView()
				.findViewById(R.id.userView);

		noSessionView.setVisibility(LinearLayout.VISIBLE);
		errorView.setVisibility(LinearLayout.GONE);
		loadingView.setVisibility(RelativeLayout.GONE);
		userView.setVisibility(ScrollView.GONE);
	}

	private void showLoadingLayoutVisibility() {
		LinearLayout noSessionView = (LinearLayout) getView().findViewById(
				R.id.noSessionView);
		LinearLayout errorView = (LinearLayout) getView().findViewById(
				R.id.errorLoadingDataView);
		RelativeLayout loadingView = (RelativeLayout) getView().findViewById(
				R.id.loadingDataLayout);
		ScrollView userView = (ScrollView) getView()
				.findViewById(R.id.userView);

		noSessionView.setVisibility(LinearLayout.GONE);
		errorView.setVisibility(LinearLayout.GONE);
		loadingView.setVisibility(RelativeLayout.VISIBLE);
		userView.setVisibility(ScrollView.GONE);
	}

	private void showLogedLayoutVisibility() {
		LinearLayout noSessionView = (LinearLayout) getView().findViewById(
				R.id.noSessionView);
		LinearLayout errorView = (LinearLayout) getView().findViewById(
				R.id.errorLoadingDataView);
		RelativeLayout loadingView = (RelativeLayout) getView().findViewById(
				R.id.loadingDataLayout);
		ScrollView userView = (ScrollView) getView()
				.findViewById(R.id.userView);

		noSessionView.setVisibility(LinearLayout.GONE);
		errorView.setVisibility(LinearLayout.GONE);
		loadingView.setVisibility(RelativeLayout.GONE);
		userView.setVisibility(ScrollView.VISIBLE);
	}

	private void showErrorLayoutVisibility() {
		LinearLayout noSessionView = (LinearLayout) getView().findViewById(
				R.id.noSessionView);
		LinearLayout errorView = (LinearLayout) getView().findViewById(
				R.id.errorLoadingDataView);
		RelativeLayout loadingView = (RelativeLayout) getView().findViewById(
				R.id.loadingDataLayout);
		ScrollView userView = (ScrollView) getView()
				.findViewById(R.id.userView);

		noSessionView.setVisibility(LinearLayout.GONE);
		errorView.setVisibility(LinearLayout.VISIBLE);
		loadingView.setVisibility(RelativeLayout.GONE);
		userView.setVisibility(ScrollView.GONE);
	}

	@Override
	public void retrieveUserInfoFinish(
			IRetrieveUserInfoService retrieveUserInfoService) {
		if (ignoreServicesCallbacks) {
			return;
		}
		showLogedLayoutVisibility();
		showUserInfo();
	}

	@Override
	public void retrieveUserInfoFinishWithError(
			IRetrieveUserInfoService retrieveUserInfoService, int errorCode) {
		if (ignoreServicesCallbacks) {
			return;
		}
		showErrorLayoutVisibility();
	}
}
