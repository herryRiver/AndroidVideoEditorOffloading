package com.cs4911.video_editor.effects;

import java.io.Serializable;
import java.util.List;

import org.opencv.core.Mat;

/**
 * @author Senior design team
 * @version 1.0
 * abstract class that all other effects inherit from.
 * detection algorithm
 */
public abstract class Effect implements Serializable {
	private static final long serialVersionUID = -72404756670729731L;
	
	/**
	 * standard applyTo matrix that all effects overload 
	 * @param A matrix of the current frame in the pipeline
	 * @return A matrix of the frame after the effect has been applied
	 */
	public Mat applyTo(Mat matrix) {
		return matrix;
	}
}
