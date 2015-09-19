package com.cs4911.video_editor.pipeline;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.avro.AvroRemoteException;
import org.apache.avro.ipc.Callback;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.highgui.Highgui;

import android.util.Log;

import com.cs4911.video_editor.android.CloudClient;
import com.cs4911.video_editor.effects.*;
/**
 * Manages the pipelines and provides an interface to offer frames
 * for processing and retrieve processed frames.
 */
public class FrameProcessor implements Callback<List<ByteBuffer>> {

	private static final String TAG = "FrameProcessor";

	private BlockingQueue<Mat> unprocessedFrameQueue, processedFrameQueue;

	private HashSet<Pipeline> pipelines;
	
	Mat originalFrame;
	CloudClient cloudClient;
	private final int K = 1;
	private List<ByteBuffer> frameBuffer;
	private AtomicInteger numFrames = new AtomicInteger(0);
	public boolean useCloud = true;
	
	/**
	 * Creates a new FrameProcessor with a pipeline with the given effects.
	 * @param effects the effects that the pipelines in this FrameProcessor should apply
	 */
	public FrameProcessor(EffectTask[] effects) {
		unprocessedFrameQueue = new LinkedBlockingQueue<Mat>(2);
		processedFrameQueue = new LinkedBlockingQueue<Mat>(2);
		pipelines = new HashSet<Pipeline>();
		addPipeline(effects);
		
		frameBuffer = new ArrayList<ByteBuffer>();
	}
	
	public Mat getMatFromByteBuffer(ByteBuffer pixels) 
	{
		byte[] raw_data = pixels.array();
		System.out.println("Size of data received is " + raw_data.length);
		MatOfByte m = new MatOfByte(pixels.array());
		Mat frame = Highgui.imdecode(m, -1);
		
		return frame;
	}
	
	public ByteBuffer getByteBufferFromMat(Mat frame)
	{
		MatOfByte m = new MatOfByte();
		Highgui.imencode(".png", frame, m);
		return ByteBuffer.wrap(m.toArray());
	}

	/**
	 * Adds a frame to be processed by one of the pipelines.
	 * @param frame a frame to be processed
	 */
	public void addFrame(Mat frame) {
		Log.d("FrameProcessor", "unprocessedFrameQueue size: " + unprocessedFrameQueue.size());
		originalFrame = frame;
		numFrames.incrementAndGet();
		
		if (useCloud && cloudClient != null) {
			
			ByteBuffer pixels = getByteBufferFromMat(frame);
			// perform the computation on the server
			frameBuffer.add(pixels);
			if (frameBuffer.size() > K) {
				try {
					cloudClient.addFrames(frameBuffer, this);
					frameBuffer.clear();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			// perform the computation on the mobile device
			unprocessedFrameQueue.offer(frame);
		}
	}

	/**
	 * Returns a processed frame if one is ready.
	 * If no frame is ready, returns null.
	 * @return a processed frame
	 */
	public Mat getFrame() {
		Log.d("FrameProcessor", "processedFrameQueue size: "
				+ processedFrameQueue.size());
		if (useCloud == true)
			if (processedFrameQueue.size() == 0)
				return originalFrame;
		
			try {
				return processedFrameQueue.take();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		return originalFrame;
	}

	/**
	 * Returns the list of effects that the pipelines apply.
	 * If there are no pipelines, returns an empty list.
	 * @return the effects that the pipelines apply
	 */
	public ArrayList<Effect> getEffects() {
		if (!pipelines.isEmpty()) {
			return pipelines.iterator().next().getEffects();
		} else {
			return new ArrayList<Effect>();
		}
	}

	/**
	 * Adds a new effect to the end of all existing pipelines.
	 * @param effect the effect to add to the pipelines
	 */
	public void addEffect(EffectTask effect) {
		for (Pipeline pipeline : pipelines) {
			pipeline.addEffect(effect);
		}
		
		if (useCloud && cloudClient != null) {
			try {
				if (effect.getEffect() instanceof BlurEffect) {
					cloudClient.addBlurEffect();
				} else if (effect.getEffect() instanceof ColorSaturationEffect) {
					cloudClient.addColorSaturationEffect();
				} else if (effect.getEffect() instanceof DrawingEffect) {
					cloudClient.addDrawingEffect();
				} else if (effect.getEffect() instanceof EdgeDetectionEffect) {
					cloudClient.addEdgeDetectionEffect();
				} else if (effect.getEffect() instanceof GradientMagnitudeEffect) {
					cloudClient.addGradientMagnitudeEffect();
				} else if (effect.getEffect() instanceof GrayscaleEffect) {
					cloudClient.addGrayscaleEffect();
				} else if (effect.getEffect() instanceof HorizontalFlipEffect) {
					cloudClient.addHorizontalFlipEffect();
				} else if (effect.getEffect() instanceof HoughCircleEffect) {
					cloudClient.addHoughCircleEffect();
				} else if (effect.getEffect() instanceof HoughLineEffect) {
					cloudClient.addHoughLineEffect();
				/*} else if (effect.getEffect() instanceof IdentityEffect) {
					cloudClient.addIdentityEffect();*/
				} else if (effect.getEffect() instanceof MotionHistoryEffect) {
					cloudClient.addMotionHistoryEffect();
				} else if (effect.getEffect() instanceof NegativeEffect) {
					cloudClient.addNegativeEffect();
				} else if (effect.getEffect() instanceof SeamCarveEffect) {
					cloudClient.addSeamCarveEffect();
				} else if (effect.getEffect() instanceof SepiaEffect) {
					cloudClient.addSepiaEffect();
				} else if (effect.getEffect() instanceof VerticalFlipEffect) {
					cloudClient.addVerticalEffect();
				} else if (effect.getEffect() instanceof XrayEffect) {
					cloudClient.addXrayEffect();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Resets all pipelines so that they do not apply
	 * any effect to frames passing through them.
	 */
	public void clearEffects() {
		for (Pipeline pipeline : pipelines) {
			pipeline.clearEffects();
		}
		
		if (useCloud && cloudClient != null) {
			try {
				cloudClient.clearEffects();
			} catch (AvroRemoteException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Tells all pipelines to begin processing frames.
	 */
	public void start() {
		for (Pipeline pipeline : pipelines) {
			pipeline.start();
		}
	}

	/**
	 * Stops all pipelines from processing frames.
	 * Also clears the queues for processed and unprocessed frames.
	 */
	public void stop() {
		for (Pipeline pipeline : pipelines) {
			pipeline.stop();
		}
		unprocessedFrameQueue.clear();
		processedFrameQueue.clear();
	}

	/**
	 * Adds a new pipeline with the same effects as an already existing pipeline.
	 * If no pipeline currently exists, does nothing.
	 */
	private void addPipeline() {
		if (!pipelines.isEmpty()) {
			pipelines.add(new Pipeline(pipelines.iterator().next()));
		}
	}

	/**
	 * Adds a new pipeline with the given effects.
	 * @param effects the effects that the new pipeline should apply to frames
	 */
	private void addPipeline(EffectTask[] effects) {
		pipelines.add(new Pipeline(unprocessedFrameQueue, processedFrameQueue,
				effects));
	}
	
	public void setCloudClient(CloudClient client) {
		this.cloudClient = client;
	}

	@Override
	public void handleError(Throwable error) {
		error.printStackTrace();
	}

	@Override
	public void handleResult(List<ByteBuffer> result) {
		
		Log.e("CloudClient", "Got a result frame " + result.size());
		for (ByteBuffer buf: result) {
			Mat resultMat = getMatFromByteBuffer(buf);
			processedFrameQueue.offer(resultMat);
			numFrames.decrementAndGet();
		}
	}
	
	public void waitForCompletion() {
		int v = numFrames.intValue();
		while (v != 0) {
			v = numFrames.intValue();
			try {
				
				// wait for bandwidth test to complete
				Thread.sleep(5);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
