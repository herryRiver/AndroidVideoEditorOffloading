package com.cs4911.video_editor.pipeline;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.opencv.core.Mat;

import android.util.Log;

import com.cs4911.video_editor.effects.Effect;
import com.cs4911.video_editor.effects.IdentityEffect;

/**
 * Pulls frames from the input queue, processes them by applying
 * a list of effects in order, and deposits the processed frames
 * into the output queue. Runs all effects as separate threads
 * to prevent waiting for offloaded effects to complete.
 */
public class Pipeline {
	private static final String TAG = "Pipeline";

	private BlockingQueue<Mat> unprocessedFrameQueue, processedFrameQueue;

	private boolean running;

	private ArrayList<Thread> effectThreads;
	private ArrayList <EffectTask> effectTasks;

	/**
	 * Creates a new pipeline that pulls frames from the unprocessedFrameQueue,
	 * processes them by applying the given list of effects, and deposits them
	 * into the processedFrameQueue.
	 * @param unprocessedFrameQueue the queue containing frames to be processed
	 * @param processedFrameQueue the queue that processed frames should be put into
	 * @param effects the effects to apply to each frame
	 */
	public Pipeline(BlockingQueue<Mat> unprocessedFrameQueue, BlockingQueue<Mat> processedFrameQueue, EffectTask[] effects) {
		this.unprocessedFrameQueue = unprocessedFrameQueue;
		this.processedFrameQueue = processedFrameQueue;
		effectThreads = new ArrayList<Thread>();
		effectTasks = new ArrayList<EffectTask>();
		running = false;

		if (effects != null && effects.length >= 1) {
			BlockingQueue<Mat> queue = unprocessedFrameQueue;
			for (int i = 0; i < effects.length - 1; i++) {
				EffectTask effect = effects[i];
				effect.setInputQueue(queue);
				queue = new LinkedBlockingQueue<Mat>(2);
				effect.setOutputQueue(queue);
				effectTasks.add(effect);
			}
			EffectTask effect = effects[effects.length - 1];
			effect.setInputQueue(queue);
			effect.setOutputQueue(processedFrameQueue);
			effectTasks.add(effect);
			createThreads();
		}
	}

	/**
	 * Creates a new pipeline with the same effects,
	 * input queue, and output queue as the given pipeline.
	 * @param pipeline the pipeline that the new pipeline should copy
	 */
	public Pipeline(Pipeline pipeline) {
		this(pipeline.unprocessedFrameQueue, pipeline.processedFrameQueue, pipeline.effectTasks.toArray(new EffectTask[0]));
	}

	/**
	 * Returns the list of effects that are applied by this pipeline.
	 * @return the effects that this pipeline applies
	 */
	public ArrayList<Effect> getEffects() {
		ArrayList<Effect> effects = new ArrayList<Effect>();
		for(EffectTask et : effectTasks) {
			if(!(et.getEffect() instanceof IdentityEffect)) {
				effects.add(et.getEffect());
			}
		}
		return effects;
	}

	/**
	 * Starts all effect threads that have not yet been started.
	 */
	public void start() {
		Log.d(TAG, "start()");
		for (Thread thread : effectThreads) {
			if (thread.getState() == Thread.State.NEW) {
				thread.start();
			}
		}
		running = true;
	}

	/**
	 * Stops all effect threads.
	 */
	public void stop() {
		Log.d(TAG, "stop()");
		for (Thread thread : effectThreads) {
			thread.interrupt();
		}
		running = false;
	}

	/**
	 * Adds the given effect at the end of the pipeline.
	 * @param effect the effect to add to the pipeline
	 */
	public void addEffect(EffectTask effect) {
		Log.d(TAG, "addEffect(" + effect + ")");
		effect.setOutputQueue(processedFrameQueue);
		if (!effectTasks.isEmpty()) {
			Thread oldEffectThread = effectThreads.remove(effectThreads.size() - 1);
			oldEffectThread.interrupt();
			EffectTask oldEffectTask = effectTasks.get(effectTasks.size() - 1);
			BlockingQueue<Mat> queue = new LinkedBlockingQueue<Mat>(2);
			oldEffectTask.setOutputQueue(queue);
			effectThreads.add(new Thread(oldEffectTask));
			effect.setInputQueue(queue);
		} else {
			effect.setInputQueue(unprocessedFrameQueue);
		}
		effectTasks.add(effect);
		effectThreads.add(new Thread(effect));
		if (running) {
			start();
		}
	}

	/**
	 * Adds the given effect at the given index.
	 * @param index the desired index of the new effect
	 * @param effect the effect to add to the pipeline
	 */
	public void addEffect(int index, EffectTask effect) {
		if (index >= effectTasks.size()) {
			addEffect(effect);
		} else if (index == 0) {
			EffectTask nextEffectTask = effectTasks.get(index);
			effect.inputQueue = nextEffectTask.inputQueue;
			BlockingQueue<Mat> queue = new LinkedBlockingQueue<Mat>(2);
			effect.outputQueue = queue;
			nextEffectTask.inputQueue = queue;
			effectTasks.add(index, effect);
			Thread effectThread = new Thread(effect);
			effectThreads.add(index, effectThread);
			Thread nextEffectThread = effectThreads.remove(index + 1);
			nextEffectThread.interrupt();
			nextEffectThread = new Thread(nextEffectTask);
			effectThreads.add(index + 1, nextEffectThread);
		} else {
			EffectTask nextEffectTask = effectTasks.get(index);
			EffectTask previousEffectTask = effectTasks.get(index - 1);
			effect.inputQueue = previousEffectTask.outputQueue;
			BlockingQueue<Mat> queue = new LinkedBlockingQueue<Mat>(2);
			effect.outputQueue = queue;
			nextEffectTask.inputQueue = queue;
			effectTasks.add(index, effect);
			Thread previousEffectThread = effectThreads.remove(index - 1);
			Thread nextEffectThread = effectThreads.remove(index - 1);
			Thread effectThread = new Thread(effect);
			previousEffectThread = new Thread(previousEffectTask);
			nextEffectThread = new Thread(nextEffectTask);
			effectThreads.add(index - 1, previousEffectThread);
			effectThreads.add(index, effectThread);
			effectThreads.add(index + 1, nextEffectThread);
		}
		if (running) {
			start();
		}
	}

	/**
	 * Removes the effect at the given index.
	 * @param index the index of the effect to be removed
	 */
	public void removeEffect(int index) {
		if (index < effectTasks.size() && index >= 0) {
			Thread effectThread = effectThreads.remove(index);
			EffectTask effectTask = effectTasks.remove(index);
			effectThread.interrupt();
			if (index < effectTasks.size()) {
				Thread nextEffectThread = effectThreads.remove(index);
				EffectTask nextEffectTask = effectTasks.get(index);
				nextEffectTask.inputQueue = effectTask.inputQueue;
				nextEffectThread.interrupt();
				nextEffectThread = new Thread(nextEffectTask);
				effectThreads.add(index, nextEffectThread);
			} else if (effectTasks.size() == 0) {
				clearEffects();
			} else {
				Thread previousEffectThread = effectThreads.remove(index - 1);
				EffectTask previousEffectTask = effectTasks.get(index - 1);
				previousEffectTask.outputQueue = effectTask.outputQueue;
				previousEffectThread.interrupt();
				previousEffectThread = new Thread(previousEffectTask);
				effectThreads.add(index - 1, previousEffectThread);
			}
			if (running) {
				start();
			}
		}
	}

	/**
	 * Moves an effect from the startIndex to the endIndex.
	 * @param startIndex the current location of the effect
	 * @param endIndex the desired location of the effect
	 */
	public void moveEffect(int startIndex, int endIndex) {
		EffectTask effect = effectTasks.get(startIndex);
		removeEffect(startIndex);
		addEffect(endIndex, effect);
	}

	/**
	 * Stops all threads and removes all effect tasks.
	 * Adds IdentityEffect task to process frames.
	 */
	public void clearEffects() {
		Log.d(TAG, "clearEffects()");
		effectTasks.clear();

		EffectTask effectTask = new LocalEffectTask(new IdentityEffect());
		effectTask.setInputQueue(unprocessedFrameQueue);
		effectTask.setOutputQueue(processedFrameQueue);

		effectTasks.add(effectTask);
		createThreads();
	}

	/**
	 * Stops all currently running threads and creates and runs
	 * new threads using the current effect tasks.
	 */
	private void createThreads() {
		boolean wasRunning = running;
		stop();
		effectThreads.clear();
		for (EffectTask effectTask : effectTasks) {
			effectThreads.add(new Thread(effectTask));
		}
		if (wasRunning) {
			start();
		}
	}
}
