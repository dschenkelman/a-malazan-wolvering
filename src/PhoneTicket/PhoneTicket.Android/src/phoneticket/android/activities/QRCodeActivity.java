package phoneticket.android.activities;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import phoneticket.android.R;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.Bitmap;
import android.widget.ImageView;

public class QRCodeActivity extends Activity {

	public static final String EXTRA_QR_STRING = "extra.qrcodeactivity.qrstring";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qrcode);
		
		String content = getIntent().getStringExtra(EXTRA_QR_STRING);
		if (null != content) {
			if (0 != content.length()) {
				ImageView qrImage = (ImageView) findViewById(R.id.qrImageView);

				try {
					BarcodeFormat barcodeFormat = BarcodeFormat.QR_CODE;

			        int width = 300;
			        int height = 300;

			        MultiFormatWriter barcodeWriter = new MultiFormatWriter();
			        BitMatrix bitMatrix = barcodeWriter.encode(content, barcodeFormat, width, height);
			        
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
}
