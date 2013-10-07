package phoneticket.android.adapter;

import java.util.List;

import phoneticket.android.R;
import phoneticket.android.model.IMovieListItem;
import phoneticket.android.views.ScaleImageView;
import phoneticket.android.views.utils.ImageLoader;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

public class ImageAdapter extends ArrayAdapter<IMovieListItem> {

	private ImageLoader mLoader;

	public ImageAdapter(Context context, int textViewResourceId,
			List<IMovieListItem> list) {
		super(context, textViewResourceId, list);
		mLoader = new ImageLoader(context);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ImageViewHolder holder;
		View row = convertView;
		//
		if (row == null) {
			LayoutInflater inflater = ((Activity) getContext())
					.getLayoutInflater();
			row = inflater.inflate(R.layout.row_staggered, parent, false);

			holder = new ImageViewHolder();
			holder.imageView = (ScaleImageView) row
					.findViewById(R.id.scaleImageView);

			row.setTag(holder);
		} else {
			holder = (ImageViewHolder) row.getTag();
		}
		mLoader.displayImage(getItem(position).getImageURL(), holder.imageView);

		return row;

	}

	private class ImageViewHolder {
		ScaleImageView imageView;
	}

}