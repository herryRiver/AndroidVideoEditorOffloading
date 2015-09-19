package com.cs4911.video_editor.android;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

/**
 * Starting point of the app that makes sure that the user has the OpenCV package installed on their device.
 */
public class OpenCVInitActivity extends Activity {
	private final String TAG = "OpenCVInitActivity";
	
	/**
	 * Starts the VideoViewingActivity if OpenCV is present on the system.
	 * If OpenCV is not present, then the user is prompted to install it.
	 */
	private BaseLoaderCallback mOpenCVCallBack = new BaseLoaderCallback(this) {
		@Override
		public void onManagerConnected(int status) {
			if(status == LoaderCallbackInterface.SUCCESS) {
				Log.i(TAG, "OpenCV loaded successfully");

				Intent i = new Intent(mAppContext, VideoViewingActivity.class);
				mAppContext.startActivity(i);
			} else {
				Log.e(TAG, "OpenCV did not load successfully");
				super.onManagerConnected(status);
			}
		}
	};
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
	    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    
	    Log.i(TAG, "Trying to load OpenCV library");
	    if (!OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_2, this, mOpenCVCallBack))
	    {
	    	Log.e(TAG, "Cannot connect to OpenCV Manager");
	    }
	}
}