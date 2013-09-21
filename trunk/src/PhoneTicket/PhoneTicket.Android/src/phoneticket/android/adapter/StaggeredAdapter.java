package phoneticket.android.adapter;

import java.util.List;

import phoneticket.android.R;
import phoneticket.android.model.IMovieListItem;
import phoneticket.android.views.ScaleImageView;
import phoneticket.android.views.utils.ImageLoader;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class StaggeredAdapter extends ArrayAdapter<IMovieListItem> {

	private ImageLoader mLoader;

	public StaggeredAdapter(Context context, int textViewResourceId,
			List<IMovieListItem> movieList) {
		super(context, textViewResourceId, movieList);
		mLoader = new ImageLoader(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;

		if (convertView == null) {
			LayoutInflater layoutInflator = LayoutInflater.from(getContext());
			convertView = layoutInflator.inflate(R.layout.row_staggered, null);
			holder = new ViewHolder();
			holder.imageView = (ScaleImageView) convertView
					.findViewById(R.id.scaleImageView);
			convertView.setTag(holder);
		}

		holder = (ViewHolder) convertView.getTag();

		mLoader.displayImage(getItem(position).getImageURL(), holder.imageView);

		return convertView;
	}

	static class ViewHolder {
		ScaleImageView imageView;
	}
}
