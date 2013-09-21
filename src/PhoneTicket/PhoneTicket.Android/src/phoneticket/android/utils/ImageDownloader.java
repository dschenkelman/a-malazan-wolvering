package phoneticket.android.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

public class ImageDownloader extends AsyncTask<Void, Integer, Void> {

	private String url;
	private ImageView img;
	private Bitmap bmp;
	private ImageLoaderListener listener;

	/*--- constructor ---*/
	public ImageDownloader(String url,
			ImageView img, Bitmap bmp,
			ImageLoaderListener listener) {
		/*--- we need to pass some objects we are going to work with ---*/
		this.url = url;
		this.img = img;
		this.bmp = bmp;
		this.listener = listener;
	}

	/*--- we need this interface for keeping the reference to our Bitmap from the MainActivity. 
	 *  Otherwise, bmp would be null in our MainActivity*/
	public interface ImageLoaderListener {
		void onImageDownloaded(Bitmap bmp);
	}

	@Override
	protected Void doInBackground(Void... arg0) {
		bmp = getBitmapFromURL(url);
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		if (listener != null) {
			listener.onImageDownloaded(bmp);
		}
		img.setImageBitmap(bmp);
		super.onPostExecute(result);
	}

	public static Bitmap getBitmapFromURL(String link) {
		/*--- this method downloads an Image from the given URL, 
		 *  then decodes and returns a Bitmap object
		 ---*/
		try {
			URL url = new URL(link);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Bitmap myBitmap = BitmapFactory.decodeStream(input);
			return myBitmap;
		} catch (IOException e) {
			e.printStackTrace();
			Log.e("PhoneTicket", "getBmpFromUrl error: "
					+ e.getMessage().toString());
			return null;
		}
	}
}
