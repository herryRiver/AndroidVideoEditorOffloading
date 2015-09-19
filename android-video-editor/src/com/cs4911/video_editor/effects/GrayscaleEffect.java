package com.cs4911.video_editor.effects;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

/**
 * @author Senior design team
 * @version 1.0
 * class that reduces the color channels of an image to 1
 *  
 */
public class GrayscaleEffect extends Effect {

	/**
	 * the function that applies a color conversion to grayscale
	 * @param A matrix of the current frame in the pipeline
	 * @return A matrix of the frame after the effect has been applied
	 */
	@Override
	public Mat applyTo(Mat frame) {
		Mat newFrame = new Mat();
		Imgproc.cvtColor(frame, newFrame, Imgproc.COLOR_RGBA2GRAY);
		Imgproc.cvtColor(newFrame, newFrame, Imgproc.COLOR_GRAY2RGBA);
		
		return newFrame;
	}

	public String toString() {
		return "Grayscale";
	}
}
