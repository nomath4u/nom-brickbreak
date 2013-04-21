package com.nomath4u.breakbrick;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;

public class Paddle {
	
	public Rect selfimage;
	public Paint selfstyle;
	private Handler timer;
	private Runnable moveSelf;
	private SensorManager mSensorManager;
	private int adcval;
	
	Paddle(Context context){
		this.selfimage = new Rect(0,0, 100, 100); //set coordinates to upper left
		
		/*Set Paint Style*/
		this.selfstyle = new Paint();
		selfstyle.setColor(Color.BLUE);
		
		/*set Sensor manager*/
		mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		Sensor adcsensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		SensorEventListener tmp = new SensorEventListener(){
			@Override
			public void onSensorChanged(SensorEvent event){
				adcval = (int) event.values[0];
				
			}
			@Override
			public void onAccuracyChanged(Sensor sensor, int accuracy){
			
			}
		};
		mSensorManager.registerListener(tmp,adcsensor, 1000000000);
		
		
		/*Set up moving self */
		timer = new Handler();
		moveSelf = new Runnable(){
			public void run(){
				move();
				timer.postDelayed(moveSelf, (17)); //just slower than 60fps
			}
		};
		
		/*Start Moving*/
		this.startMoving();
			
	}
	
	public void move(){
		selfimage.set((adcval*-10) + 90, 0, (adcval*-10)+110, 10); 
	}
	
	public void startMoving(){
		moveSelf.run();
	}
	
	public void stopMoving(){
		timer.removeCallbacks(moveSelf);
	}
}
