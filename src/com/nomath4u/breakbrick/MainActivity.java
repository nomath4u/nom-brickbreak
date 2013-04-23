package com.nomath4u.breakbrick;


import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;


public class MainActivity extends Activity {
	public int lives;
	public int score;
	public SensorManager mSensorManager;
	public float adcval;
	public float maxval;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		createSensorManager();
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
    	
    }
    
    @Override
    protected void onRestart(){
    	super.onRestart();


    }
    
    private void createSensorManager(){
    	mSensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
		Sensor adcsensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		SensorEventListener tmp = new SensorEventListener(){
			@Override
			public void onSensorChanged(SensorEvent event){
				adcval = event.values[0];
				
			}
			@Override
			public void onAccuracyChanged(Sensor sensor, int accuracy){
			
			}
		};
		mSensorManager.registerListener(tmp,adcsensor, 1000000000);
		maxval = adcsensor.getMaximumRange();
    }
    
    @Override
    protected void onPause(){
    	super.onPause();

    }
    
    @Override
    protected void onDestroy(){
    	super.onDestroy();

    }
	
	public void addScore(int add){
		score = score + add;
		
	}
	
	public void addLife(int add){
		lives = lives + add;
	}
}
