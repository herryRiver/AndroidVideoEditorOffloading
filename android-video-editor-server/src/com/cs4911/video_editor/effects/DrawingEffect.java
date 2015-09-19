package com.cs4911.video_editor.effects;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
/**
 * @author Senior design team
 * @version 1.0
 * class that implements the drawing effect
 */
public class DrawingEffect extends Effect {

	double sp = 10.0;
	double sr = 35.0;
	@Override
	/**
	 * the function that applies a Drawing effect 
	 * @param A matrix of the current frame in the pipeline
	 * @return A matrix of the frame after the effect has been applied
	 */
	public Mat applyTo(Mat frame) {
		Mat newFrame = new Mat();
		Mat bgr      = new Mat();
		Mat gray     = new Mat();
		Mat edges    = new Mat();
		Mat edgesBgr = new Mat();
		
		Imgproc.cvtColor(frame,bgr,Imgproc.COLOR_BGRA2BGR);
		Imgproc.pyrMeanShiftFiltering(bgr, bgr, sp, sr);
		Imgproc.cvtColor(frame,gray,Imgproc.COLOR_BGRA2GRAY);
		
		Imgproc.Canny(gray, edges, 150, 150);
		
		Imgproc.cvtColor(edges,edgesBgr,Imgproc.COLOR_GRAY2BGR);
		Core.subtract(bgr, edgesBgr, bgr);
		Imgproc.cvtColor(bgr,newFrame,Imgproc.COLOR_BGR2BGRA);
		return newFrame;
	}

	public String toString() {
		return "Drawing";
	}
}