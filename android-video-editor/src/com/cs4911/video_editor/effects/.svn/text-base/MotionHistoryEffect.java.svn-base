package com.cs4911.video_editor.effects;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
/**
 * @author Senior design team
 * @version 1.0
 * shows a motion image that decays older motions over time
 *  
 */
public class MotionHistoryEffect extends Effect {
	transient Mat greyImage = null;
	transient Mat movingAverage = null;
	transient Mat difference = null;
	transient Mat temp = null;
	
	/**
	 * this is the function that when applied causes more recent motions to be evaluated higher than older ones.
	 * @param A matrix of the current frame in the pipeline
	 * @return A matrix of the frame after the effect has been applied
	 */
	@Override
	public Mat applyTo(Mat frame) {
		if(greyImage == null || movingAverage == null || difference == null || temp == null) {
			greyImage = new Mat( frame.size(), CvType.CV_8UC1);
			movingAverage = new Mat(frame.size(), CvType.CV_32FC1);
			difference = greyImage.clone();
			temp = frame.clone();
		} else {
			Imgproc.cvtColor(frame, greyImage, Imgproc.COLOR_RGBA2GRAY);
			Imgproc.accumulateWeighted(greyImage, movingAverage, 0.5);
			Core.convertScaleAbs(movingAverage, temp, 1.0, 0.0);
			Core.absdiff(greyImage, temp, difference);
		}
		
		Mat newFrame = new Mat();
		Imgproc.cvtColor(difference, newFrame, Imgproc.COLOR_GRAY2RGBA);
		return newFrame;
	}

	public String toString() {
		return "Motion History";
	}
}
