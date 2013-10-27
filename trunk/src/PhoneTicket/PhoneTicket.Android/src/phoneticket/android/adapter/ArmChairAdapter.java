package phoneticket.android.adapter;

import java.util.List;

import phoneticket.android.R;
import phoneticket.android.model.ArmChair;
import phoneticket.android.views.ArmChairView;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ArmChairAdapter extends ArrayAdapter<ArmChair> {

	private List<ArmChair> armChairs;

	public ArmChairAdapter(Context context, int resource,
			List<ArmChair> armChairs) {
		super(context, resource, armChairs);
		this.armChairs = armChairs;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ArmChairHolder holder;
		View row = convertView;
		if (row == null) {
			ArmChair armChair = armChairs.get(position);
			LayoutInflater inflater = ((Activity) getContext())
					.getLayoutInflater();
			row = inflater.inflate(R.layout.row_arm_chair, parent, false);

			holder = new ArmChairHolder();
			holder.setArmChairView((ArmChairView) row
					.findViewById(R.id.armChairView));
			holder.setArmChairTextView((TextView) row
					.findViewById(R.id.armChairTextView));
			if (armChair.existente()) {
				holder.getArmChairTextView().setText(
						String.valueOf(armChair.getColumn()));
			}
			holder.getArmChairView().setState(armChair.getState());

			row.setTag(holder);
		} else {
			holder = (ArmChairHolder) row.getTag();
		}
		return row;
	}

	public class ArmChairHolder {
		private ArmChairView armChairView;
		private TextView armChairTextView;

		public ArmChairView getArmChairView() {
			return armChairView;
		}

		public void setArmChairView(ArmChairView armChairView) {
			this.armChairView = armChairView;
		}

		public TextView getArmChairTextView() {
			return armChairTextView;
		}

		public void setArmChairTextView(TextView armChairTextView) {
			this.armChairTextView = armChairTextView;
		}

	}

}
