package com.cs4911.video_editor.android;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.LinearLayout;

import com.cs4911.video_editor.effects.BlurEffect;
import com.cs4911.video_editor.effects.ColorSaturationEffect;
import com.cs4911.video_editor.effects.DrawingEffect;
import com.cs4911.video_editor.effects.EdgeDetectionEffect;
import com.cs4911.video_editor.effects.Effect;
import com.cs4911.video_editor.effects.GradientMagnitudeEffect;
import com.cs4911.video_editor.effects.GrayscaleEffect;
import com.cs4911.video_editor.effects.HorizontalFlipEffect;
import com.cs4911.video_editor.effects.HoughCircleEffect;
import com.cs4911.video_editor.effects.HoughLineEffect;
import com.cs4911.video_editor.effects.MotionHistoryEffect;
import com.cs4911.video_editor.effects.NegativeEffect;
import com.cs4911.video_editor.effects.SeamCarveEffect;
import com.cs4911.video_editor.effects.SepiaEffect;
import com.cs4911.video_editor.effects.VerticalFlipEffect;

/**
 * Activity where the user is able to edit the pipeline by inserting and removing effects.
 */
public class PipelineEditingActivity extends Activity implements OnClickListener {
	
	private final String TAG = "PipelineEdittingActivity";
	
	//The effects view and the pipeline view.
	LinearLayout effectsLinearLayout;
	LinearLayout pipelineLinearLayout;
	
	//Effect buttons
	Button buttonNone;
	Button buttonGrayScale;
	Button buttonBlur;
	Button buttonNegative;
	Button buttonColorSaturation;
	Button buttonHFlip;
	Button buttonVFlip;
	Button buttonEdgeDetection;
	Button buttonGradientMagnitude;
	Button buttonMotionHistory;
	Button buttonObjectDetection;
    Button buttonLineDetection;
	Button buttonCircleDetection;
	Button buttonDrawing;
	Button buttonSepia;
	Button buttonSeamCarving;
	
	//Static Buttons
	Button buttonClearPipeline;
	Button buttonCancel;
	Button buttonOk;
	
	//Current pipeline
	ArrayList<Effect> effects;
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_pipeline_editting);
		
		//Gets the views from the layout
		effectsLinearLayout = (LinearLayout) this.findViewById(R.id.effectsLinearLayout);
		pipelineLinearLayout = (LinearLayout) this.findViewById(R.id.pipelinesLinearLayout);
		
		// Gets the array of effects in the current pipeline from the passed in Intent.
		effects = (ArrayList<Effect>) this.getIntent().getSerializableExtra("effects");
		if(effects == null) {
			Log.e(TAG, "Got null effects list");
			effects = new ArrayList<Effect>();
		}
		
		setupButtons();
	}

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		ViewParent vp = v.getParent();
		
		//Checks if the click was on the effects view, the pipeline view, or one of the confirmation buttons.
		//If the click is on the effects view, the chosen effect is added to the pipeline.
		//If it is on the pipeline view, the chosen effect is removed from the pipeline.
		//If it is on any of the comfirmation buttons, they run the desired task.
		if(vp.equals(effectsLinearLayout)) {
			Button newButton = new Button(this);
			Effect newEffect = null;
			
			//Checks which effect button was pressed to add it to the pipeline
			if (v == buttonGrayScale) {
				newEffect = new GrayscaleEffect();
			} else if (v == buttonBlur) {
				newEffect = new BlurEffect();
			} else if (v == buttonNegative) {
				newEffect = new NegativeEffect();
			} else if (v == buttonColorSaturation) {
				newEffect = new ColorSaturationEffect();
			} else if (v == buttonHFlip) {
				newEffect = new HorizontalFlipEffect();
			} else if (v == buttonVFlip) {
				newEffect = new VerticalFlipEffect();
			} else if (v == buttonEdgeDetection) {
				newEffect = new EdgeDetectionEffect();
			} else if(v == buttonGradientMagnitude) {
				newEffect = new GradientMagnitudeEffect();
			} else if (v == buttonMotionHistory) {
				newEffect = new MotionHistoryEffect();
			} else if (v == buttonLineDetection) {
				newEffect = new HoughLineEffect();
			} else if (v == buttonCircleDetection) {
				newEffect = new HoughCircleEffect();
			} else if (v == buttonDrawing) {
				newEffect = new DrawingEffect();
			} else if (v == buttonSepia) {
				newEffect = new SepiaEffect();
			} else if (v == buttonSeamCarving) {
				newEffect = new SeamCarveEffect();
			}
			
			if(newEffect != null) {
				effects.add(newEffect);
				
				newButton.setText(newEffect.toString());
				newButton.setOnClickListener(this);
				pipelineLinearLayout.addView(newButton);
			}
		} else if(vp.equals(pipelineLinearLayout)) {
			int index = pipelineLinearLayout.indexOfChild(v);
			pipelineLinearLayout.removeViewAt(index);
			effects.remove(index);
		} else {
			if(v == buttonClearPipeline) {
				pipelineLinearLayout.removeAllViews();
				effects.clear();
			} else if(v == buttonCancel) {
				this.setResult(RESULT_CANCELED);
				this.finish();
			} else if(v == buttonOk) {
				Intent result = new Intent();
				result.putExtra("result", effects);
				this.setResult(RESULT_OK, result);
				this.finish();
			}
		}
	}
	
	/**
	 * Sets up the view of the effects that can be added to the pipeline.
	 */
	private void setupButtons() {
		//Effect Buttons
		buttonGrayScale = new Button(this);
		buttonGrayScale.setText("Grayscale");
		buttonGrayScale.setOnClickListener(this);
		effectsLinearLayout.addView(buttonGrayScale);
		
		buttonBlur = new Button(this);
		buttonBlur.setText("Blur");
		buttonBlur.setOnClickListener(this);
		effectsLinearLayout.addView(buttonBlur);
		
		buttonNegative = new Button(this);
		buttonNegative.setText("Negative");
		buttonNegative.setOnClickListener(this);
		effectsLinearLayout.addView(buttonNegative);
		
		buttonColorSaturation = new Button(this);
		buttonColorSaturation.setText("Color Saturation");
		buttonColorSaturation.setOnClickListener(this);
		effectsLinearLayout.addView(buttonColorSaturation);
		
		buttonHFlip = new Button(this);
		buttonHFlip.setText("Horizontal Flip");
		buttonHFlip.setOnClickListener(this);
		effectsLinearLayout.addView(buttonHFlip);
		
		buttonVFlip = new Button(this);
		buttonVFlip.setText("Vertical Flip");
		buttonVFlip.setOnClickListener(this);
		effectsLinearLayout.addView(buttonVFlip);
		
		buttonEdgeDetection = new Button(this);
		buttonEdgeDetection.setText("Edge Detection");
		buttonEdgeDetection.setOnClickListener(this);
		effectsLinearLayout.addView(buttonEdgeDetection);
		
		buttonGradientMagnitude = new Button(this);
		buttonGradientMagnitude.setText("Gradient Magnitude");
		buttonGradientMagnitude.setOnClickListener(this);
		effectsLinearLayout.addView(buttonGradientMagnitude);
		
		buttonMotionHistory = new Button(this);
		buttonMotionHistory.setText("Motion History");
		buttonMotionHistory.setOnClickListener(this);
		effectsLinearLayout.addView(buttonMotionHistory);
		
	    buttonLineDetection = new Button(this);
	    buttonLineDetection.setText("Line Detection");
		buttonLineDetection.setOnClickListener(this);
		effectsLinearLayout.addView(buttonLineDetection);
	    
		buttonCircleDetection = new Button(this);
		buttonCircleDetection.setText("Circle Detection");
		buttonCircleDetection.setOnClickListener(this);
		effectsLinearLayout.addView(buttonCircleDetection);
		
		buttonDrawing = new Button(this);
		buttonDrawing.setText("Drawing");
		buttonDrawing.setOnClickListener(this);
		effectsLinearLayout.addView(buttonDrawing);
		
		buttonSepia = new Button(this);
		buttonSepia.setText("Sepia");
		buttonSepia.setOnClickListener(this);
		effectsLinearLayout.addView(buttonSepia);
		
		buttonSeamCarving = new Button(this);
		buttonSeamCarving.setText("Seam Carving");
		buttonSeamCarving.setOnClickListener(this);
		effectsLinearLayout.addView(buttonSeamCarving);
		
		//Static Buttons
		buttonClearPipeline = (Button) this.findViewById(R.id.button_edit_pipeline_clear);
		buttonClearPipeline.setOnClickListener(this);
		
		buttonCancel = (Button) this.findViewById(R.id.button_edit_pipeline_cancel);
		buttonCancel.setOnClickListener(this);
		
		buttonOk = (Button) this.findViewById(R.id.button_edit_pipeline_ok);
		buttonOk.setOnClickListener(this);
		
		//Pipeline Buttons
		for(Effect e : effects) {
			Button newButton = new Button(this);
			newButton.setText(e.toString());
			newButton.setOnClickListener(this);
			pipelineLinearLayout.addView(newButton);
		}
	}
}
