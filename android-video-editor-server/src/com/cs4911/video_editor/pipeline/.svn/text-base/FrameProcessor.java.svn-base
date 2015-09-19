package com.cs4911.video_editor.pipeline;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.opencv.core.Mat;

import com.cs4911.video_editor.effects.*;

/**
 * Manages the pipelines and provides an interface to offer frames
 * for processing and retrieve processed frames.
 */
public class FrameProcessor {

	private static final String TAG = "FrameProcessor";

	private BlockingQueue<Mat> unprocessedFrameQueue, processedFrameQueue;

	private HashSet<Pipeline> pipelines;
	
	Mat originalFrame;
	int numFrames = 0;

	/**
	 * Creates a new FrameProcessor with a pipeline with the given effects.
	 * @param effects the effects that the pipelines in this FrameProcessor should apply
	 */
	public FrameProcessor(EffectTask[] effects) {
		unprocessedFrameQueue = new LinkedBlockingQueue<Mat>(20);
		processedFrameQueue = new LinkedBlockingQueue<Mat>(20);
		pipelines = new HashSet<Pipeline>();
		addPipeline(effects);
	}

	/**
	 * Adds a frame to be processed by one of the pipelines.
	 * @param frame a frame to be processed
	 */
	public void addFrame(Mat frame) {
		if (!unprocessedFrameQueue.offer(frame))
			System.err.println("Failed to add frame to unprocessed Q");
	}

	/**
	 * Returns a processed frame if one is ready.
	 * If no frame is ready, returns null.
	 * @return a processed frame
	 */
	public Mat getFrame() {
		//return processedFrameQueue.poll();
		System.out.println("Giving Processed frame : " + processedFrameQueue.size());
		
		Mat m = null;
		try {
			m =  processedFrameQueue.take();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return m;
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
	}

	/**
	 * Resets all pipelines so that they do not apply
	 * any effect to frames passing through them.
	 */
	public void clearEffects() {
		for (Pipeline pipeline : pipelines) {
			pipeline.clearEffects();
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
		pipelines.add(new Pipeline(unprocessedFrameQueue, processedFrameQueue,effects));
	}
}
