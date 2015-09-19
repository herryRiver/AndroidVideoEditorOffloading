package com.cs4911.video_editor.effects;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Size;
import org.opencv.core.Mat;

import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
/**
 * @author Senior design team
 * @version 1.0
 * the sepia effect
 *  
 */
public class SepiaEffect extends Effect {

	float sepia_data[] = {0.272f,  0.534f,  0.131f, 0,0.349f, 0.686f, 0.168f,0,0.393f,0.769f,0.189f,0,0,0,0,1};
	/**
	 * this function convolves a 4 by 4 kernel across the image getting and old gold tint to the cv::Mat
	 * @param A matrix of the current frame in the pipeline
	 * @return A matrix of the frame after the effect has been applied
	 */
	@Override
	public Mat applyTo(Mat frame) {
		
		Mat effect = new Mat();
		
		Mat m_sepiaKernel = new Mat(new Size(4,4),CvType.CV_32F);
		m_sepiaKernel.put(0, 0, sepia_data);
		Core.transform(frame, effect, m_sepiaKernel);
		
		Imgproc.cvtColor(effect,effect,Imgproc.COLOR_BGRA2RGBA);
		return effect;
		
	}
	
	public String toString() {
		return "Sepia";
	}
}