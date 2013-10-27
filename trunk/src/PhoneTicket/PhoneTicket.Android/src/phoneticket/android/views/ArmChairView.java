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

	public void setState(int armChairState) {
		Drawable d;
		switch (armChairState) {
		case ArmChair.OCUPADA: {
			d = getResources().getDrawable(R.drawable.butaca_ocupada);
			break;
		}
		case ArmChair.LIBRE: {
			d = getResources().getDrawable(R.drawable.butaca_libre);
			break;
		}
		case ArmChair.SELECCIONADA: {
			d = getResources().getDrawable(R.drawable.butaca_seleccionada);
			break;
		}

		default: {
			d = getResources().getDrawable(R.drawable.sin_butaca);
			break;
		}
		}

		this.setImageDrawable(d);
	}
}
