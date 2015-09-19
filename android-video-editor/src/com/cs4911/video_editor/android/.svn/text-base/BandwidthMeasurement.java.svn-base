package com.cs4911.video_editor.android;

import android.util.Log;

public class BandwidthMeasurement {
	
	static String TAG = "CloudClient";
	final MyTimer timer = new MyTimer();
	String serverIpString;
	
	static {
		System.loadLibrary("launchbed");
	}

	// send the message to the server in a thread
	public class ClientClass implements Runnable {
		
		boolean TestServerBandwidth() {
			return TestBandwidth();
		}
		
		public boolean TestBandwidth() {
	    	
	    	//bandwidthResult br = launchBandwidthTest();
			Log.i("test", "server ip is " + serverIpString);
	    	boolean status = launchBandwidthTest(serverIpString);
	    	
	    	if (status) {
	    		Log.e("test", "Error: bandwidth test failed\n");
	    		return status;
	    	}
	    	
	    	long bytes_sent     = getUploadedBytes();
	    	long bytes_recv     = getDownloadedBytes();
	    	double time_taken   = getTimeTaken();
	    	long hostCpuUtil    = getHostCpuUtilization();
	    	long serverCpuUtil  = getServerCpuUtilization();
	    	
	    	uploadBandwidth     = bytes_sent / ((1 << 20) * time_taken); 
	    	downloadBandwidth   = bytes_recv / ((1 << 20) * time_taken);
	    	
	    	String bandwidthMsg = String.format("%.2f", uploadBandwidth) + " MBps";
	    	
	    	Log.i("test", "test done\n");
	    	
	    	return status;
	    }

		@Override
		public void run() {
			if (TestServerBandwidth() == false) {
				Log.e(TAG, "Could not test bandwidth");
				uploadBandwidth = 0;
				downloadBandwidth = 0;
			}
		}
	}
	
	public native boolean launchBandwidthTest(String serverIp);
	public native long getUploadedBytes();
	public native long getDownloadedBytes();
	public native double getTimeTaken();
	public native long getHostCpuUtilization();
	public native long getServerCpuUtilization();
	
	public double uploadBandwidth, downloadBandwidth;
	
	public double GetUploadBandwidth() {
		return uploadBandwidth;
	}
	
	public double GetDownloadBandwidth() {
		return downloadBandwidth;
	}
	
	ClientClass myClient;
	
	BandwidthMeasurement(String serverIp) {
		serverIpString = serverIp;
		myClient = new ClientClass();
		Thread td = new Thread(myClient);
        td.start();
		myClient.TestServerBandwidth();
	}
}
