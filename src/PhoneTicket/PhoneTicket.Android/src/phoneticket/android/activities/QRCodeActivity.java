package phoneticket.android.activities;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import phoneticket.android.R;
import android.os.Build;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class QRCodeActivity extends Activity {

	public static final String EXTRA_QR_STRING = "extra.qrcodeactivity.qrstring";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qrcode);
		setupActionBar();
		String content = getIntent().getStringExtra(EXTRA_QR_STRING);
		if (null != content) {
			if (0 != content.length()) {
				ImageView qrImage = (ImageView) findViewById(R.id.qrImageView);

				try {
					BarcodeFormat barcodeFormat = BarcodeFormat.QR_CODE;

					int width = 300;
					int height = 300;

					MultiFormatWriter barcodeWriter = new MultiFormatWriter();
					BitMatrix bitMatrix = barcodeWriter.encode(content,
							barcodeFormat, width, height);

					int[] pixels = new int[width * height];
					for (int y = 0; y < height; y++) {
						int offset = y * width;
						for (int x = 0; x < width; x++) {
							pixels[offset + x] = bitMatrix.get(x, y) ? 0xFF000000
									: 0xFFFFFFFF;
						}
					}

					Bitmap bitmap = Bitmap.createBitmap(width, height,
							Bitmap.Config.ARGB_8888);
					bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
					qrImage.setImageBitmap(bitmap);
				} catch (WriterException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		ActionBar actionBar = getActionBar();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB
				&& actionBar != null) {

			actionBar.setDisplayHomeAsUpEnabled(false);
			actionBar.setDisplayShowTitleEnabled(false);
			actionBar.setDisplayUseLogoEnabled(false);
			actionBar.setDisplayShowHomeEnabled(false);
			actionBar.setDisplayShowCustomEnabled(true);

			LayoutInflater inflator = (LayoutInflater) this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View v = inflator.inflate(R.layout.default_action_bar, null);
			((TextView) v.findViewById(R.id.actionTitle))
					.setText(getResources().getString(
							R.string.title_activity_qrcode));

			actionBar.setCustomView(v);
		}
	}

}
