package com.cs4911.video_editor.effects;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
/**
 * @author Senior design team
 * @version 1.0
 * class that implements a Edge detection using the Canny Edge
 * detection algorithm
 */
public class EdgeDetectionEffect extends Effect {
	
	/**
	 * the function that applies the edge detection effect 
	 * @param A matrix of the current frame in the pipeline
	 * @return A matrix of the frame after the effect has been applied
	 */
	@Override
	public Mat applyTo(Mat frame) {
		Mat newFrame = new Mat();
		Imgproc.Canny(frame, newFrame, 80, 100);
		Imgproc.cvtColor(newFrame, newFrame, Imgproc.COLOR_GRAY2BGRA, 4);
		
		return newFrame;
	}

	public String toString() {
		return "Edge Detection";
	}
}
