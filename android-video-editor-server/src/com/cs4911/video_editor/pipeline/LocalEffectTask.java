package com.cs4911.video_editor.pipeline;

import org.opencv.core.Mat;

import com.cs4911.video_editor.effects.Effect;

/**
 * Applies an effect to frames locally, on the device.
 */
public class LocalEffectTask extends EffectTask {

	private final String TAG = "Android Video Editor";

	/**
	 * Creates a LocalEffectTask that applies the given effect.
	 * @param effect the effect to be applied
	 */
	public LocalEffectTask(Effect effect) {
		super(effect);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		while (true) {
			Mat frame;
			try {
				frame = inputQueue.take();
			} catch (InterruptedException e) {
				break;
			}
			if (frame != null) {
				System.out.println("Frame catched");
				frame = effect.applyTo(frame);
				if (Thread.interrupted()) {
					break;
				}

				outputQueue.offer(frame);
			}
			if (Thread.interrupted()) {
				break;
			}
		}
	}

}
