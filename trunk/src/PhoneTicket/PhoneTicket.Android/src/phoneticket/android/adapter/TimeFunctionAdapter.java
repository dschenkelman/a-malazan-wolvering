package phoneticket.android.adapter;

import java.util.Collection;

import phoneticket.android.model.IFunction;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.TextView;

public class TimeFunctionAdapter extends BaseAdapter {

	private Context context;
	private Collection<IFunction> functions;

	public TimeFunctionAdapter(FragmentActivity context,
			Collection<IFunction> functions) {
		this.context = context;
		this.functions = functions;
	}

	@Override
	public int getCount() {
		return functions.size();
	}

	@Override
	public Object getItem(int position) {
		return functions.toArray()[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView imageView;
		if (convertView == null) {
			imageView = new TextView(context);
			imageView.setPadding(8, 8, 8, 8);
		} else {
			imageView = (TextView) convertView;
		}
		IFunction function = (IFunction) getItem(position);
		imageView.setText(function.getTime());
		return imageView;
	}

}
