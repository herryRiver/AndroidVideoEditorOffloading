package com.cs4911.video_editor.pipeline;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;

import org.opencv.core.Mat;

import com.cs4911.video_editor.effects.Effect;

/**
 * An Runnable that takes in Mats from an input queue,
 * applies an Effect to them, and places them into an output queue.
 * The run method is left unimplemented to allow for varied implementations.
 */
public abstract class EffectTask implements Runnable {

	protected BlockingQueue<Mat> inputQueue, outputQueue;
	protected Effect effect;

	/**
	 * Creates a new EffectTask with the given effect, inputQueue, and outputQueue.
	 * @param effect the Effect to apply to Mats
	 * @param inputQueue the queue to pull unprocessed Mats from
	 * @param outputQueue the queue to put processed Mats in
	 */
	public EffectTask(Effect effect, BlockingQueue<Mat> inputQueue, BlockingQueue<Mat> outputQueue) {
		this.effect = effect;
		this.inputQueue = inputQueue;
		this.outputQueue = outputQueue;
	}

	/**
	 * Creates a new EffectTask with the given effect.
	 * @param effect the Effect to apply to Mats
	 */
	public EffectTask(Effect effect) {
		this(effect, null, null);
	}

	/**
	 * Returns the Effect that this EffectTask applies to Mats.
	 * @return this EffectTask's effect
	 */
	public Effect getEffect() {
		return effect;
	}

	/**
	 * Return the queue that this EffectTask pulls unprocessed Mats from.
	 * @return this EffectTask's inputQueue
	 */
	public Queue<Mat> getInputQueue() {
		return inputQueue;
	}

	/**
	 * Sets the queue that this EffectTask pulls unprocessed Mats from.
	 * @param inputQueue a queue containing Mats to be processed
	 */
	public void setInputQueue(BlockingQueue<Mat> inputQueue) {
		this.inputQueue = inputQueue;
	}

	/**
	 * Return the queue that this EffectTask puts processed Mats in.
	 * @return this EffectTask's outputQueue
	 */
	public Queue<Mat> getOutputQueue() {
		return outputQueue;
	}

	/**
	 * Sets the queue that this EffectTask puts processed Mats in.
	 * @param outputQueue a queue to receive processed Mats
	 */
	public void setOutputQueue(BlockingQueue<Mat> outputQueue) {
		this.outputQueue = outputQueue;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return effect.toString();
	}
}
