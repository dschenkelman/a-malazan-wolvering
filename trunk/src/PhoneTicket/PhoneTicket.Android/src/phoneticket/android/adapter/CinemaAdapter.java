package phoneticket.android.adapter;

import java.util.List;

import phoneticket.android.R;

import phoneticket.android.model.ICinema;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CinemaAdapter extends ArrayAdapter<ICinema> {

	public CinemaAdapter(Context context, int resource, List<ICinema> objects) {
		super(context, resource, objects);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ListHolder holder;
		View row = convertView;
		//
		if (row == null) {
			LayoutInflater inflater = ((Activity) getContext())
					.getLayoutInflater();
			row = inflater.inflate(R.layout.row_cinema_list, parent, false);

			holder = new ListHolder();
			holder.cinemaName = (TextView) row.findViewById(R.id.cinemaName);
			holder.cinemaAddress = (TextView) row
					.findViewById(R.id.cinemaAddress);
			row.setTag(holder);
		} else {
			holder = (ListHolder) row.getTag();
		}
		holder.cinemaName.setText(getItem(position).getName());
		holder.cinemaAddress.setText(getItem(position).getAddress());
		return row;

	}

	private class ListHolder {
		TextView cinemaName;
		TextView cinemaAddress;
	}
}
