package com.cs4911.video_editor.effects;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
/**
 * @author Senior design team
 * @version 1.0
 * class that implements a blur effect
 */
public class BlurEffect extends Effect {

	/**
	 * the function that applies the blur
	 * @param A matrix of the current frame in the pipeline
	 * @return A matrix of the frame after the effect has been applied
	 */
	@Override
	public Mat applyTo(Mat frame) {
		
		Mat newFrame = new Mat();
		Size size = new Size(50,50);;
		Imgproc.blur(frame, newFrame, size);
		
		return newFrame;
	}
	
	public String toString() {
		return "Blur";
	}
}
