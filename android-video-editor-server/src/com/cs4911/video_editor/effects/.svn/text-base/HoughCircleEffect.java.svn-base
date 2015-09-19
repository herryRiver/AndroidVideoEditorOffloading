package com.cs4911.video_editor.effects;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
/**
 * @author Senior design team
 * @version 1.0
 * class that votes in 3d hough space for projective circles in an image 
 *  
 */
public class HoughCircleEffect extends Effect {
	
	/**
	 * the function converts a cv::mat to gray scale then runs a gaussian blur on it to reduce noise. 
	 * Then runs the canny edge detector to get projective circles in image space
	 *  then runs the hough circle algorithm to find the most likely circles.
	 * @param A matrix of the current frame in the pipeline
	 * @return A matrix of the frame after the effect has been applied
	 */
	@Override
	public Mat applyTo(Mat frame) {
		Mat newFrame = new Mat();
		Imgproc.cvtColor( frame, newFrame, Imgproc.COLOR_BGR2GRAY );
		Imgproc.GaussianBlur( newFrame, newFrame,new Size(9, 9), 2, 2 );
		Imgproc.Canny(frame, newFrame, 80, 100);

		Mat circles = new Mat();

		Imgproc.HoughCircles( newFrame, circles, Imgproc.CV_HOUGH_GRADIENT, 1, newFrame.rows()/8, 200, 100, 0, 0 );

		/// Draw the circles detected
		for( int i = 0; i < circles.cols(); i++ )
		{
			double vCircle[] = circles.get(0,i);
			if (vCircle == null) break;

			Point center = new Point(Math.round(vCircle[0]), Math.round(vCircle[1]));
			int  radius = (int)Math.round(vCircle[2]);
			// circle center
			Core.circle( frame, center, 3, new Scalar(0,255,0), -1, 8, 0 );
			// circle outline
			Core.circle( frame, center, radius, new Scalar(0,0,255), 3, 8, 0 );
		}


		frame.copyTo(newFrame);

		return newFrame;
	}
	
	public String toString() {
		return "Circle Detection";
	}
}