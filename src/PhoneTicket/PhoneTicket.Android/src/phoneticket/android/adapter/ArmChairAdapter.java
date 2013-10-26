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

public class ArmChairAdapter extends ArrayAdapter<ArmChair> {

	private List<ArmChair> armChairs;

	public ArmChairAdapter(Context context, int resource,
			List<ArmChair> armChairs) {
		super(context, resource, armChairs);
		this.armChairs = armChairs;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ImageViewHolder holder;
		View row = convertView;
		if (row == null) {
			LayoutInflater inflater = ((Activity) getContext())
					.getLayoutInflater();
			row = inflater.inflate(R.layout.row_arm_chair, parent, false);

			holder = new ImageViewHolder();
			holder.armChairView = (ArmChairView) row
					.findViewById(R.id.armChairView);
			holder.armChairView.setState(armChairs.get(position));

			row.setTag(holder);
		} else {
			holder = (ImageViewHolder) row.getTag();
		}
		return row;
	}

	private class ImageViewHolder {
		ArmChairView armChairView;
	}

}
