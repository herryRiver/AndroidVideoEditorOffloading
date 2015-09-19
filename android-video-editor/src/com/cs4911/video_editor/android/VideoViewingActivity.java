package com.cs4911.video_editor.android;

import java.util.ArrayList;

import org.opencv.android.JavaCameraView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.cs4911.video_editor.effects.Effect;
import com.cs4911.video_editor.pipeline.LocalEffectTask;

/**
 * Main Activity where the effects are viewed on screen as being aplied to the camera image.
 */
public class VideoViewingActivity extends Activity {

	private static final String TAG = "VideoViewingActivity";
	private static final int EDIT_PIPELINE = 1;
	private static boolean useCloud = false;
	
	//The view that passes frames to the ImageProcessor from the camera and displays the frames from the pipeline.
	private JavaCameraView mView;
	
	//The ImageProcessor that catches OpenCV frames and communicates with the pipeline.
	ImageProcessor imageProcessor;
	
	//A list of effects that is kept in persistence in case the editing activity is canceled.
	ArrayList<Effect> effectList;

	//Menu items that can be selected from the menu.
	MenuItem mItemEditPipeline;
	MenuItem mItemClearPipeline;
	
	CloudClient    cloudClient;
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		
		//Sets fullscreen and keeps the screen on.
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		setContentView(R.layout.activity_video_viewing);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {
		Log.i(TAG, "onPause");
		super.onPause();
		
		//Shuts off the camera view so that it doesn't use unneeded resources.
		mView.disableView();
		mView.setCvCameraViewListener(null);
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		Log.i(TAG, "onResume");
		super.onResume();
		
		// Restarts the camera view to start retrieving frams.
		mView.setCvCameraViewListener(imageProcessor);
		mView.enableView();
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onStart()
	 */
	@Override
	public void onStart() {
		Log.i(TAG, "onStart");
		super.onStart();
				
		if (useCloud && cloudClient == null) {
			cloudClient = new CloudClient();
			Thread td   = new Thread(cloudClient);
			td.start();
		}
		//Safety checks in case the activity's resources were deallocated.
		if(imageProcessor == null) {
			imageProcessor = new ImageProcessor(cloudClient);
		}
		
		mView = (JavaCameraView) this.findViewById(R.id.frameView);
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onStop()
	 */
	@Override
	public void onStop() {
		Log.i(TAG, "onStop");
		super.onStop();
		
		//Problems occur if the same processor is used after the activity stops.
		imageProcessor = null;
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	public void onDestroy() {
		Log.i(TAG, "onDestroy()");
		super.onDestroy();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Log.i(TAG, "onCreateOptionsMenu");
		mItemEditPipeline = menu.add("Edit Pipeline");
		mItemClearPipeline = menu.add("Clear Pipeline");
		return true;
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.i(TAG, "Menu Item selected " + item);
		
		//Handles clicking of the menu items.
		if(item == mItemEditPipeline) {
			//Puts the current pipeline effects in to an Intent to send to the editing activity.
			Intent editPipelineIntent = new Intent(this, PipelineEditingActivity.class);
			effectList = imageProcessor.getEffects();
			editPipelineIntent.putExtra("effects", effectList);
			
			//Start the editing activity.
			this.startActivityForResult(editPipelineIntent, EDIT_PIPELINE);
		} else if(item == mItemClearPipeline) {
			imageProcessor.clearPipeline();
		} 
		
		return true;
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i(TAG, "onActivityResult");
		
		//If the activity requested a pipeline edit, handle the result.
		if (requestCode == EDIT_PIPELINE) {
			
			//If the editing activity returned a result, use it.  Otherwise, load the old effect list.
			if (resultCode == RESULT_OK) {
				Log.i(TAG, "RESULT_OK");

				//Get the list of effects returned from the editing activity.
				effectList = (ArrayList<Effect>) data.getSerializableExtra("result");

				//Safety check
				if(effectList == null) {
					Log.e(TAG, "Got null effects list");
					effectList = new ArrayList<Effect>();
				}
			}
			
			//Load the effects in to the new ImageProcessor
			imageProcessor = new ImageProcessor(cloudClient);
			imageProcessor.clearPipeline();
			for(Effect effect : effectList) {
				imageProcessor.addEffect(new LocalEffectTask(effect));
			}
		}
	}
}
