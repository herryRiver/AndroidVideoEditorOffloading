package com.cs4911.video_editor.effects;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
/**
 * @author Senior design team
 * @version 1.0
 * class that finds the hough lines
 *  
 */
public class HoughLineEffect extends Effect {
	
	/**
	 * the function that converts an image to gray scale, gaussian blurs it to reduce noise, then runs the canny edge
	 * detector to find votes for lines in image space. plotting these lines using euclidian points in hough space reveals
	 * lines that we can plot on the rgb image 
	 * @param A matrix of the current frame in the pipeline
	 * @return A matrix of the frame after the effect has been applied
	 */
	@Override
	public Mat applyTo(Mat frame) {
		Mat newFrame = new Mat();
		Imgproc.cvtColor( frame, newFrame, Imgproc.COLOR_BGR2GRAY );
		Imgproc.GaussianBlur( newFrame, newFrame,new Size(9, 9), 2, 2 );
		Imgproc.Canny(frame, newFrame, 80, 100);

		Mat lines = new Mat();

		Imgproc.HoughLinesP(newFrame, lines, 1, Math.PI/180, 80, 30, 10);
		//Imgproc.HoughLines(newFrame, lines, 1, Math.PI/180, 100);
		for( int i = 0; i < lines.cols(); i++ )
		{
			double vLines[] = lines.get(0,i);
			if (vLines == null) break;

			double x0 = vLines[0],  y0 = vLines[1],
					x1 = vLines[2],  y1 = vLines[3];
			Point d0 = new Point(x0,y0);
			Point d1 = new Point(x1,y1);

			/*double rho = vLines[0],  theta = vLines[1];
			double a = Math.cos(theta), b = Math.sin(theta);
			//hough space
			double x0 = a*rho, y0 = b*rho;
			//euclidean space
			double euc_x0 = Math.round(x0 + 1000*(-b));
			double euc_y0 = Math.round(y0 + 1000*(a));

			double euc_x1 = Math.round(x0 - 1000*(-b));
			double euc_y1 = Math.round(y0 - 1000*(a));

			Point d0 = new Point(euc_x0,euc_y0);
			Point d1 = new Point(euc_x1,euc_y1);*/

			Core.line( frame, d0, d1, new Scalar(0,0,255), 3);
		}

		frame.copyTo(newFrame);

		return newFrame;
	}
	
	public String toString() {
		return "Line Detection";
	}
}