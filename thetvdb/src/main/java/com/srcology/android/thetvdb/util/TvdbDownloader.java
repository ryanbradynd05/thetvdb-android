package com.srcology.android.thetvdb.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.os.Environment;
import android.util.Log;

import com.srcology.android.thetvdb.TvdbApp;

public class TvdbDownloader {
	private static final String TAG = TvdbApp.TAG;
	private Context mContext;
	
    public TvdbDownloader(Context context) {
    	mContext = context;
    }
	
	private InputStream openHttpConnection(String urlString) throws IOException {
		InputStream in = null;
		int response = -1;

		URL url = new URL(urlString); 
		URLConnection conn = url.openConnection();

		if (!(conn instanceof HttpURLConnection)) {                   
			throw new IOException("Not an HTTP connection");
		}
		try{
			HttpURLConnection httpConn = (HttpURLConnection) conn;
			httpConn.setAllowUserInteraction(false);
			httpConn.setInstanceFollowRedirects(true);
			httpConn.setRequestMethod("GET");
			httpConn.connect(); 

			response = httpConn.getResponseCode();                 
			if (response == HttpURLConnection.HTTP_OK) {
				in = httpConn.getInputStream();                                 
			}                     
		} catch (Exception ex) {
	    	Log.e(TAG, ex.getLocalizedMessage());
			throw new IOException("Error connecting");            
		}
		return in;     
	}

	public Bitmap getImage(String URL) {        
		Bitmap bitmap = null;
		InputStream in = null;        
		try {
			in = openHttpConnection(URL);
			bitmap = BitmapFactory.decodeStream(in);
			in.close();
		} catch (IOException e1) {
	    	Log.e(TAG, e1.getLocalizedMessage());
			e1.printStackTrace();
		}
		return bitmap;                
	}

	public String getText(String URL) {
		int BUFFER_SIZE = 2000;
		InputStream in = null;
		try {
			in = openHttpConnection(URL);
		} catch (IOException e1) {
	    	Log.e(TAG, e1.getLocalizedMessage());
			e1.printStackTrace();
			return "";
		}

		InputStreamReader isr = new InputStreamReader(in);
		int charRead;
		String str = "";
		char[] inputBuffer = new char[BUFFER_SIZE];          
		try {
			while ((charRead = isr.read(inputBuffer))>0) {                    
				//---convert the chars to a String---
				String readString =  String.copyValueOf(inputBuffer, 0, charRead);                    
				str += readString;
				inputBuffer = new char[BUFFER_SIZE];
			}
			in.close();
		} catch (IOException e) {
	    	Log.e(TAG, e.getLocalizedMessage());
			e.printStackTrace();
			return "";
		}    
		return str;        
	}
	
	public OutputStream getOutputStream(String URL, String fileLoc) {
        InputStream in = null;
        OutputStream out = null;
        if (isExternalStorageWriteable()) {
            try {
                in = openHttpConnection(URL);
                // write the inputStream to a FileOutputStream
                out = new FileOutputStream(new File(fileLoc));
             
            	int read = 0;
            	byte[] bytes = new byte[1024];
             
            	while ((read = in.read(bytes)) != -1) {
            		out.write(bytes, 0, read);
            	}
             
            	in.close();
            	out.flush();
            	out.close();
            } catch (IOException e1) {
    	    	Log.e(TAG, e1.getLocalizedMessage());
                e1.printStackTrace();            
            }
        }
        return out;
    }
	
	public void getFile(String URL, String fileLoc) {
        InputStream in = null;
        if (isExternalStorageWriteable()) {
            try {
                in = openHttpConnection(URL);
                // write the inputStream to a FileOutputStream
                OutputStream out = new FileOutputStream(new File(fileLoc));
             
            	int read = 0;
            	byte[] bytes = new byte[1024];
             
            	while ((read = in.read(bytes)) != -1) {
            		out.write(bytes, 0, read);
            	}
             
            	in.close();
            	out.flush();
            	out.close();
            } catch (IOException e1) {
    	    	Log.e(TAG, e1.getLocalizedMessage());
                e1.printStackTrace();            
            }
        }
    }
	
	public String getXmlString(String url) {
		HttpGet getRequest = new HttpGet(url);
		DefaultHttpClient client = new DefaultHttpClient();

		try {
			HttpResponse getResponse = client.execute(getRequest);
			final int statusCode = getResponse.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				return null;
			}
			HttpEntity getResponseEntity = getResponse.getEntity();
			if (getResponseEntity != null) {
				return EntityUtils.toString(getResponseEntity);
			}
		}
		catch (IOException e) {
			getRequest.abort();
			Log.w(TAG, "Error for URL " + url, e);
		}
		return null;
	}

	
	public boolean isExternalStorageWriteable() {
		boolean mExternalStorageWriteable = false;
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) {
		    // We can read and write the media
		    mExternalStorageWriteable = true;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
		    // We can only read the media
		    mExternalStorageWriteable = false;
		} else {
		    // Something else is wrong. It may be one of many other states, but all we need
		    //  to know is we can neither read nor write
		    mExternalStorageWriteable = false;
		}
    	Log.d(TAG, "isExternalStorageWriteable: " + mExternalStorageWriteable);
		return mExternalStorageWriteable;
	}
	
	public boolean isNetworkConnectionAvailable() {  
	    ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo info = cm.getActiveNetworkInfo();     
	    if (info == null) return false;
	    State network = info.getState();
	    boolean isNetworkAvailable = (network == NetworkInfo.State.CONNECTED || network == NetworkInfo.State.CONNECTING);
    	Log.d(TAG, "isNetworkAvailable: " + isNetworkAvailable);
	    return isNetworkAvailable;
	} 
}
