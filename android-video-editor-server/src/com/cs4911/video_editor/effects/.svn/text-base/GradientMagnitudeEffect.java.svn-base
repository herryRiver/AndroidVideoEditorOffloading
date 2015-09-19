package com.cs4911.video_editor.effects;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
/**
 * @author Senior design team
 * @version 1.0
 * class that implements a Edge detection using the Sobel operator
 *  
 */
public class GradientMagnitudeEffect extends Effect {

	/**
	 * the function that applies the edge detection effect with the sobel operator
	 * @param A matrix of the current frame in the pipeline
	 * @return A matrix of the frame after the effect has been applied
	 */
	@Override
	public Mat applyTo(Mat frame) {
		Mat grayScale = new Mat();
		Imgproc.cvtColor(frame, grayScale, Imgproc.COLOR_RGBA2GRAY);
		
		Mat drv = new Mat(grayScale.size(), CvType.CV_16SC1);
		Mat drv32f = new Mat(grayScale.size(), CvType.CV_32FC1);
		Mat mag = Mat.zeros(grayScale.size(), CvType.CV_32FC1);

		Imgproc.Sobel(grayScale, drv, CvType.CV_16SC1, 1, 0);
		drv.convertTo(drv32f, CvType.CV_32FC1);
		Imgproc.accumulateSquare(drv32f, mag);

		Imgproc.Sobel(grayScale, drv, CvType.CV_16SC1, 0, 1);
		drv.convertTo(drv32f, CvType.CV_32FC1);
		Imgproc.accumulateSquare(drv32f, mag);

		Core.sqrt(mag, mag);
		
		mag.convertTo(mag, CvType.CV_8U);
		Imgproc.cvtColor(mag, mag, Imgproc.COLOR_GRAY2RGBA);
		return mag;
	}

	public String toString() {
		return "Gradient Magnitude";
	}
}
