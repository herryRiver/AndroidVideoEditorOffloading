package com.cs4911.video_editor.android;

import java.util.ArrayList;

import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import android.util.Log;

import com.cs4911.video_editor.effects.Effect;
import com.cs4911.video_editor.effects.IdentityEffect;
import com.cs4911.video_editor.pipeline.EffectTask;
import com.cs4911.video_editor.pipeline.FrameProcessor;
import com.cs4911.video_editor.pipeline.LocalEffectTask;

/**
 * Is handed the frames from the CameraView. Acts as the edge between the Android and non-Android code.
 * Hands the input frames to the FrameProcessor and grabs the output frames from the Frame processor.
 */
public class ImageProcessor implements CvCameraViewListener {	
	private static final String TAG = "ImageProcessor";
	
	//The underlying pipeline system for the image processor to pass frames to.
	FrameProcessor frameProcessor;
	CloudClient    cloudClient;
	
	public ImageProcessor(CloudClient client) {
		cloudClient = client;
		frameProcessor = new FrameProcessor(new EffectTask[]{new LocalEffectTask(new IdentityEffect())});
		frameProcessor.setCloudClient(cloudClient);
	}
	
	/**
	 * Gets the list of effects in the underlying pipeline system
	 * @return A list of effects in the underlying pipeline system
	 */
	public ArrayList<Effect> getEffects() {
		return frameProcessor.getEffects();
	}

	/**
	 * Adds an Effect to the underlying pipeline system
	 * @param effect The EffectTask for the effect to be added
	 */
	public void addEffect(EffectTask effect) {
		if(effect != null) {
			frameProcessor.addEffect(effect);
		}
	}
	
	/**
	 * Clears the pipeline in the underlying pipeline system
	 */
	public void clearPipeline() {
		frameProcessor.clearEffects();		
	}

	/* (non-Javadoc)
	 * @see org.opencv.android.CameraBridgeViewBase.CvCameraViewListener#onCameraViewStarted(int, int)
	 */
	@Override
	public void onCameraViewStarted(int width, int height) {
		Log.d(TAG, "onCameraViewStarted");
		frameProcessor.start();
	}

	/* (non-Javadoc)
	 * @see org.opencv.android.CameraBridgeViewBase.CvCameraViewListener#onCameraViewStopped()
	 */
	@Override
	public void onCameraViewStopped() {
		Log.d(TAG, "onCameraViewStoppped");
		frameProcessor.stop();
	}
	
	
	/* (non-Javadoc)
	 * @see org.opencv.android.CameraBridgeViewBase.CvCameraViewListener#onCameraFrame(org.opencv.core.Mat)
	 */
	@Override
	public Mat onCameraFrame(Mat inputFrame) {
		//Makes a clone of the inputFrame to pass to the pipeline.
		Mat newFrame = inputFrame.clone();
		frameProcessor.addFrame(newFrame);
		
		//Gets a frame from the pipeline
		newFrame = frameProcessor.getFrame();
		
		//Makes sure that the frame passed back is the size needed by OpenCV to display it.
		if(newFrame != null && newFrame.width() < inputFrame.width()) {
			int diff = (int) (inputFrame.width() - newFrame.width());
			Scalar value = new Scalar(0,0,0);
			Mat mat = new Mat();
			Imgproc.copyMakeBorder(newFrame, mat, 0, 0, diff/2, diff - diff/2, Imgproc.BORDER_CONSTANT, value);
			newFrame = mat;
		}
		
		return newFrame;
	}
}
