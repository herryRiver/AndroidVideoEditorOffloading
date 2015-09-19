package com.cs4911.video_editor.android;

public class MyTimer {
	private long startTime;
	private long stopTime;
	
	public MyTimer() {
		startTime = 0;
		stopTime  = 0;
	}
	
	public void InitTimer() {
		startTime = System.nanoTime();
	}
	
	public void StopTimer() {
		stopTime = System.nanoTime();
	}
	
	public long TimeElapsedInMSec() {
		return (stopTime - startTime) / 1000000; 
	}
}
