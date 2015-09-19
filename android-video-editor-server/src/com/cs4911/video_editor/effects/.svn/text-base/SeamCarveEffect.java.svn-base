package com.cs4911.video_editor.effects;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
/**
 * @author Senior design team
 * @version 1.0
 * class that implements the seam Carving effect
 * reduces the number of least significant seams from a matrix
 */
public class SeamCarveEffect extends Effect {
	
	/**
	 * the function that applies the seam carving
	 * At the moment it finds 10 least significant seams to remove
	 * @param A matrix of the current frame in the pipeline
	 * @return A matrix of the frame after the effect has been applied
	 */
	@Override
	public Mat applyTo(Mat frame) {

		Mat newFrame = frame.clone();
		for(int i = 0; i < 10; i++) {
			//Gradient Magnitude for intensity of image.
			Mat gradientMagnitude = computeGradientMagnitude(newFrame);
			
			//Use DP to create the real energy map that is used for path calculation.  Strictly using vertical paths for testing simplicity.
			Mat pathIntensityMat = computePathIntensityMat(gradientMagnitude);
			
			if(pathIntensityMat == null) return frame;
			
			//Take the path of least importance from the path map and remove it.
			newFrame = removeLeastImportantPath(newFrame, pathIntensityMat);
			
			if(newFrame == null) return frame;
		}
		return newFrame;
	}
	/**
	 * The first step of the seam carving is to compute  the gradient magnitude of the image
	 * to get the x and y gradients we use the sobel operator on an un-blurred image
	 * @param A matrix of the current frame sent in
	 * @return A matrix of the frame after the gradient magnitude is computed 
	 */
	private Mat computeGradientMagnitude(Mat frame) {
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
		
		return mag;
	}

	/**
	 * Computes paths of intensity for the given energy map.  The path intensity is given by the bottom row in the matrix.
	 * 
	 * @param rawEnergyMap the matrix contains the raw energy data from the original image
	 * @return a matrix that contains the path intensities of the given input.
	 */
	private Mat computePathIntensityMat(Mat rawEnergyMap) {
		Mat pathIntensityMap = new Mat(rawEnergyMap.size(), CvType.CV_32FC1);
		
		//Log.i(TAG, rawEnergyMap.size() + " " + rawEnergyMap.type() + " " + rawEnergyMap.channels() + " " + pathIntensityMap.size() + " " + pathIntensityMap.type() + " " + pathIntensityMap.channels());
		
		//Get java primitive for efficiency.
		float[] buffRaw = new float[(int) rawEnergyMap.total()];
		rawEnergyMap.get(0, 0, buffRaw);
		
		//Will put output in to Java primitive for efficiency.
		float[] buffPath = new float[(int) pathIntensityMap.total()];
		
		if(buffRaw.length == 0 || buffPath.length == 0) return null;
		
		//First row of intensity paths is the same as the energy map
		for(int col = 0; col < pathIntensityMap.cols(); col++) {
			buffPath[col] = buffRaw[col];
		}
		
		float max = 0;
		
		//The rest of them use the DP calculation using the minimum of the 3 pixels above them + their own intensity.
		for(int row = 1; row < pathIntensityMap.rows(); row++) {
			for(int col = 0; col < pathIntensityMap.cols(); col++) {
				//The initial intensity of the pixel is its raw intensity
				float pixelIntensity = buffRaw[col + row*rawEnergyMap.cols()];
				
				//The the minimum intensity from the current path of the 3 pixels above it is added to its intensity.
				float p1 = intensity(buffPath, col - 1, row - 1, pathIntensityMap.cols());
				float p2 = intensity(buffPath, col, row - 1, pathIntensityMap.cols());
				float p3 = intensity(buffPath, col + 1, row - 1, pathIntensityMap.cols());
				
				float minIntensity = Math.min(p1, p2);
				minIntensity = Math.min(minIntensity, p3);
				
				pixelIntensity += minIntensity;
				
				max = Math.max(max, pixelIntensity);
				
				buffPath[col + row*pathIntensityMap.cols()] = pixelIntensity;
			}
		}
		
		/*for(int row = 0; row < pathIntensityMap.rows(); row++) {
			for(int col = 0; col < pathIntensityMap.cols(); col++) {
				buffPath[col + row*pathIntensityMap.cols()] /= max;
			}
		}*/
		
		//Put the computed path matrix in to the native object.
		pathIntensityMap.put(0, 0, buffPath);

		return pathIntensityMap;
	}
	
	/**
	 * Helper to deal with edge cases.
	 * @param a float representation of the data
	 * @param int col the current col of the matrix
	 * @param the row the current row of the matrix
	 * @param int nCols the number of cols of the matrix
	 * @return the float intensity of the map 
	 */
	private float intensity(float[] map, int col, int row, int nCols) {
		if(col < 0 || col >= nCols) {
			return Float.MAX_VALUE;
		} else {
			return map[col + row*nCols];
		}
	}
	
	/**
	 * Helper to deal with edge cases.
	 * @param Mat original The input matrix
	 * @param Mat importanceMap the mat of what is important
	 * @return the matrix of values after you remove the seams 
	 */
	private Mat removeLeastImportantPath(Mat original, Mat importanceMap) {
		Size size = new Size(original.width()-1, original.height());
		Mat newMat = new Mat(size, CvType.CV_8UC4);
		
		float[] buffPath = new float[(int) importanceMap.total()];
		importanceMap.get(0, 0, buffPath);
		
		byte[] buffOrigin = new byte[(int) original.total()*original.channels()];
		original.get(0, 0, buffOrigin);
		
		byte[] buffNew = new byte[(int) newMat.total()*newMat.channels()];
		
		if(buffPath.length == 0 || buffOrigin.length == 0) return null;
		
		//Find the beginning of the least important path.  Trying an averaging approach because absolute min wasn't very reliable.
		float minImportance = buffPath[(importanceMap.rows()-1)*importanceMap.cols()];
		
		int minCol = 0;

		for(int col = 1; col < importanceMap.cols(); col++) {
			float currPixel = buffPath[(importanceMap.rows()-1)*importanceMap.cols() + col];
			
			if(currPixel < minImportance) {
				minCol = col;
				minImportance = currPixel;
			}
		}
		
		removePixel(buffOrigin, buffNew, original.cols(), original.rows() - 1, original.channels(), minCol);
		
		for(int row = original.rows() - 2; row >= 0; row--) {
			float p1 = intensity(buffPath, minCol - 1, row, importanceMap.cols());
			float p2 = intensity(buffPath, minCol, row, importanceMap.cols());
			float p3 = intensity(buffPath, minCol + 1, row, importanceMap.cols());
			
			//Adjust the min column for path following
			if(p1 < p2 && p1 < p3) {
				minCol -= 1;
			} else if(p3 < p1 && p3 < p2) {
				minCol += 1;
			}
			
			removePixel(buffOrigin, buffNew, original.cols(), row, original.channels(), minCol);
		}
		
		newMat.put(0, 0, buffNew);
		
		return newMat;
	}
	
	/**
	 * does the actual pixel reductions
	 * @param  byte[] original -input matrix in byte form
	 * @param  byte[] neo -new matrix in byte form
	 * @param int width - width of input mat
	 * @param int row - height of the input mat
	 * @param int channels - number of color channels
	 * @param minCol - location of the least significant column to remove
	 */
	private void removePixel(byte[] original, byte[] neo, int width, int row, int channels, int minCol) {
		int originRowStart = row*channels*width;
		int newRowStart = row*channels*(width-1);
		int firstNum = minCol*channels;
		System.arraycopy(original, originRowStart, neo, newRowStart, firstNum);
		
		int originRowMid = originRowStart + (minCol + 1)*channels;
		int newRowMid = newRowStart + minCol*channels;
		int secondNum = (width-1)*channels - firstNum;
		System.arraycopy(original, originRowMid, neo, newRowMid, secondNum);
		
		int leftPixel = minCol - 1;
		int rightPixel = minCol + 1;
		
		int byte1 = original[originRowStart + minCol*channels];
		int byte2 = original[originRowStart + minCol*channels+1];
		int byte3 = original[originRowStart + minCol*channels+2];
		
		if(byte1 < 0) byte1 += 256;
		if(byte2 < 0) byte2 += 256;
		if(byte3 < 0) byte3 += 256;
		
		if(rightPixel < width) {
			int byte1R = original[originRowStart + rightPixel*channels];
			int byte2R = original[originRowStart + rightPixel*channels+1];
			int byte3R = original[originRowStart + rightPixel*channels+2];
			
			if(byte1R < 0) byte1R += 256;
			if(byte2R < 0) byte2R += 256;
			if(byte3R < 0) byte3R += 256;
			
			neo[newRowStart + minCol*channels] = (byte) ((byte1 + byte1R)/2);
			neo[newRowStart + minCol*channels+1] = (byte) ((byte2 + byte2R)/2);
			neo[newRowStart + minCol*channels+2] = (byte) ((byte3 + byte3R)/2);
		}  
		
		if(leftPixel >= 0) {
			int byte1L = original[originRowStart + leftPixel*channels];
			int byte2L = original[originRowStart + leftPixel*channels+1];
			int byte3L = original[originRowStart + leftPixel*channels+2];
			
			if(byte1L < 0) byte1L += 256;
			if(byte2L < 0) byte2L += 256;
			if(byte3L < 0) byte3L += 256;
			
			neo[newRowStart + leftPixel*channels] = (byte) ((byte1 + byte1L)/2);
			neo[newRowStart + leftPixel*channels+1] = (byte) ((byte2 + byte2L)/2);
			neo[newRowStart + leftPixel*channels+2] = (byte) ((byte3 + byte3L)/2);
		}
	}
	
	public String toString() {
		return "Seam Carver";
	}
}
