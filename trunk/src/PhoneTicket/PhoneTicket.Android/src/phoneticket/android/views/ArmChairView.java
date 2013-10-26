package phoneticket.android.views;

import phoneticket.android.R;
import phoneticket.android.model.ArmChair;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class ArmChairView extends ImageView {

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
		Drawable d;
		switch (armChair.getState()) {
		case 0: {
			d = getResources().getDrawable(R.drawable.sin_butaca);
			break;
		}
		case 1: {
			d = getResources().getDrawable(R.drawable.butaca_ocupada);
			break;
		}
		case 2: {
			d = getResources().getDrawable(R.drawable.butaca_libre);
			break;
		}

		default: {
			d = getResources().getDrawable(R.drawable.butaca_libre);
			break;
		}
		}

		this.setImageDrawable(d);
	}
}
