package com.cs4911.video_editor.effects;

import java.io.ByteArrayOutputStream;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import android.graphics.Bitmap;
/**
 * @author Senior design team
 * @version 1.0
 * class that acts as a JNI cover
 */
public class Frame 
{
	private Bitmap bmp;
	private Mat enc_mat;
	private byte[] byteArray;
	/**
	 * A constructor that takes in a cv::Mat converts it to a bitmap and pulls the byte array out of it from that
	 * @param A matrix of the current frame in the pipeline
	 * 
	 */
	public Frame(Mat m)
	{
		Imgproc.cvtColor(m, m, Imgproc.COLOR_RGB2BGRA);
		bmp = Bitmap.createBitmap(m.cols(), m.rows(), Bitmap.Config.ARGB_8888);
		Utils.matToBitmap(m, bmp);
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
		byteArray = stream.toByteArray();
	}
	/**
	 * getter for the bitmap
	 * @param A matrix of the current frame in the pipeline
	 * @return returns the bitmap 
	 */
	public Bitmap getBitMap()
	{
		return bmp;
	}
	
	/**
	 * getter for the byte array
	 * @return returns the byte array 
	 */
	public byte[] getByteArr()
	{
		return byteArray;
	}
	/**
	 * getter for the Matrix
	 * @return returns the Matrix
	 */
	public Mat getMat()
	{
		return enc_mat;
	}
}
