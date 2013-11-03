package phoneticket.android.adapter;

import java.util.List;

import phoneticket.android.R;
import phoneticket.android.activities.interfaces.IDiscountSelectedListener;
import phoneticket.android.model.DiscountCountable;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class DiscountAdapter extends ArrayAdapter<DiscountCountable> {

	private List<DiscountCountable> discounts;
	private IDiscountSelectedListener discountSelectedListener;

	public DiscountAdapter(Context context, int resource,
			List<DiscountCountable> discounts) {
		super(context, resource, discounts);
		this.discounts = discounts;
	}

	public void setDiscountSelectedListener(IDiscountSelectedListener listener) {
		discountSelectedListener = listener;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		final DiscountHolder holder = new DiscountHolder();

		LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
		View row = inflater.inflate(R.layout.row_discount, parent, false);
		final DiscountCountable discount = discounts.get(position);

		holder.checkBox = (CheckBox) row.findViewById(R.id.discountCheckBox);
		holder.plusDiscountButton = (Button) row
				.findViewById(R.id.plusDiscountCount);
		holder.decreaseDiscountButton = (Button) row
				.findViewById(R.id.decreaseDiscountCount);
		holder.discountsCount = (TextView) row.findViewById(R.id.discountCount);
		holder.discountDescription = (TextView) row
				.findViewById(R.id.discountDescription);

		holder.checkBox.setChecked(discount.isSelected());
		holder.checkBox.setEnabled(discountSelectedListener
				.discountPosible(discount) || discount.isSelected());
		holder.plusDiscountButton.setEnabled(holder.checkBox.isChecked());
		holder.decreaseDiscountButton.setEnabled(holder.checkBox.isChecked());
		holder.discountsCount.setText(String.valueOf(discount.getCount()));
		holder.discountDescription.setText(discount.getDescription());
		holder.plusDiscountButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				discount.plusCount();
				if (discountSelectedListener.discountIncrease(discount)) {
					holder.discountsCount.setText(String.valueOf(discount
							.getCount()));
				} else {
					Toast t = Toast.makeText(getContext(),
							"No se pueden seleccionar más promociones",
							Toast.LENGTH_SHORT);
					t.show();
					discount.decreaseCount();
				}
				notifyDataSetChanged();
			}
		});

		holder.decreaseDiscountButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (discount.getCount() > 0) {
					discount.decreaseCount();
					holder.discountsCount.setText(String.valueOf(discount
							.getCount()));
					discountSelectedListener.discountDecrease(discount);
				}
				notifyDataSetChanged();
			}
		});
		holder.checkBox
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							discount.clearCount();
							discount.plusCount();
							if (!discountSelectedListener
									.discountIncrease(discount)) {
								discount.decreaseCount();
								isChecked = false;
								holder.checkBox.setChecked(isChecked);
							}
						} else {
							discountSelectedListener
									.discountUnSelected(discount);
							discount.clearCount();
						}
						holder.discountsCount.setText(String.valueOf(discount
								.getCount()));
						holder.plusDiscountButton.setEnabled(isChecked);
						holder.decreaseDiscountButton.setEnabled(isChecked);
						discount.select(isChecked);
						notifyDataSetChanged();
					}
				});

		row.setTag(holder);
		return row;
	}

	public class DiscountHolder {

		CheckBox checkBox;
		TextView discountsCount;
		TextView discountDescription;
		Button plusDiscountButton;
		Button decreaseDiscountButton;

	}
}
