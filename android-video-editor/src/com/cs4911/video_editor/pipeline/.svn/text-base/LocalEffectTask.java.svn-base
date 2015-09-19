package com.cs4911.video_editor.pipeline;

import org.opencv.core.Mat;

import android.util.Log;

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
				Log.w(TAG, "LocalEffectTask caught InterruptedException while trying to take from queue.");
				break;
			}
			if (frame != null) {
				Log.d("LocalEffectTask", effect.toString() + " - inputQueue size: " + inputQueue.size());
				frame = effect.applyTo(frame);
				if (Thread.interrupted()) {
					break;
				}

				outputQueue.offer(frame);
				Log.d("LocalEffectTask", effect.toString() + " - outputQueue size: " + outputQueue.size());
			}
			if (Thread.interrupted()) {
				break;
			}
		}
	}

}
