package phoneticket.android.adapter;

import java.util.ArrayList;
import java.util.Collection;

import phoneticket.android.R;
import phoneticket.android.model.IFunction;
import phoneticket.android.model.IMovieFunctions;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.TextView;

public class ExpandableMovieFunctionsAdapter extends BaseExpandableListAdapter {

	private FragmentActivity context;
	private Collection<IMovieFunctions> movieFunctions;

	public ExpandableMovieFunctionsAdapter(FragmentActivity context,
			Collection<IMovieFunctions> movieFunctions) {
		this.context = context;
		this.movieFunctions = movieFunctions;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		IMovieFunctions movieFuntions = (IMovieFunctions) movieFunctions
				.toArray()[groupPosition];
		return movieFuntions.getFunctions();
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		@SuppressWarnings("unchecked")
		Collection<IFunction> groupFunctions = (Collection<IFunction>) getChild(
				groupPosition, childPosition);
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.exp_moviefuncts_child,
					null);
		}
		TextView txtListChild = (TextView) convertView
				.findViewById(R.id.childTextView);

		Collection<String> days = getDays(groupPosition);
		Collection<IFunction> childFunctions = new ArrayList<IFunction>();

		String text = (String) days.toArray()[childPosition];
		for (IFunction function : groupFunctions) {
			if (function.getDay().equals(days.toArray()[childPosition])) {
				childFunctions.add(function);
			}
		}

		GridView gridView = (GridView) convertView.findViewById(R.id.timeGrid);
		gridView.setAdapter(new TimeFunctionAdapter(context, childFunctions));
	    
		txtListChild.setText(text);
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		Collection<String> days = getDays(groupPosition);
		return days.size();
	}

	private Collection<String> getDays(int groupPosition) {
		IMovieFunctions movieFuntions = (IMovieFunctions) movieFunctions
				.toArray()[groupPosition];
		ArrayList<String> days = new ArrayList<String>();
		for (IFunction function : movieFuntions.getFunctions()) {
			if (false == days.contains(function.getDay())) {
				days.add(function.getDay());
			}
		}
		return days;
	}

	@Override
	public Object getGroup(int groupPosition) {
		IMovieFunctions movieFuntions = (IMovieFunctions) movieFunctions
				.toArray()[groupPosition];
		return movieFuntions.getCinemaName();
	}

	@Override
	public int getGroupCount() {
		return movieFunctions.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		String headerTitle = (String) getGroup(groupPosition);
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(
					R.layout.exp_moviefuncts_header_group, null);
		}
		TextView titleTextView = (TextView) convertView
				.findViewById(R.id.titleTextView);
		titleTextView.setTypeface(null, Typeface.BOLD);
		titleTextView.setText(headerTitle);
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

}
