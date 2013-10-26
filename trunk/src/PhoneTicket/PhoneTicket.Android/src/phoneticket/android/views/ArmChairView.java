package phoneticket.android.views;

import phoneticket.android.R;
import phoneticket.android.model.ArmChair;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class ArmChairView extends ScaleImageView {

	public ArmChairView(Context context) {
		super(context);
	}

	public ArmChairView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public ArmChairView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setState(ArmChair armChair) {
		Drawable d = getResources().getDrawable(R.drawable.butaca_libre);
		this.setImageDrawable(d);
	}
}
