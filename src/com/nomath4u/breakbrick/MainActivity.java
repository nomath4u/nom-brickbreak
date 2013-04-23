package com.nomath4u.breakbrick;


import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;


public class MainActivity extends Activity {
	public int lives;
	public int score;
	public SensorManager mSensorManager;
	public float adcval;
	public float maxval;
	private SensorEventListener adcListener;
	private Sensor adcsensor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		createSensorManager();
		//WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
		getWindow().addFlags(LayoutParams.FLAG_KEEP_SCREEN_ON); //Get active wakelock
		setContentView(new SurfacePanel(this));
		lives = 5;
		score = 0;
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
    @Override
    protected void onResume(){
    	super.onResume();
    	mSensorManager.registerListener(adcListener, adcsensor, SensorManager.SENSOR_DELAY_NORMAL);
    }
    
    @Override
    protected void onRestart(){
    	super.onRestart();
    	mSensorManager.registerListener(adcListener, adcsensor, SensorManager.SENSOR_DELAY_NORMAL);

    }
    
    private void createSensorManager(){
    	mSensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
		adcsensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		adcListener = new SensorEventListener(){
			@Override
			public void onSensorChanged(SensorEvent event){
				adcval = event.values[0];
				
			}
			@Override
			public void onAccuracyChanged(Sensor sensor, int accuracy){
			
			}
		};
		mSensorManager.registerListener(adcListener,adcsensor, SensorManager.SENSOR_DELAY_NORMAL);
		maxval = adcsensor.getMaximumRange();
    }
    
    @Override
    protected void onPause(){
    	super.onPause();
    	mSensorManager.unregisterListener(adcListener);
    }
    
    @Override
    protected void onDestroy(){
    	super.onDestroy();
    	mSensorManager.unregisterListener(adcListener);
    }
	
	public void addScore(int add){
		score = score + add;
		
	}
	
	public void addLife(int add){
		lives = lives + add;
	}
}
