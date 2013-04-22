package com.nomath4u.breakbrick;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Handler;
import android.view.Display;
import android.view.WindowManager;

public class Paddle {
	
	public RectF selfimage;
	public Paint selfstyle;
	private Handler timer;
	private Runnable moveSelf;
	private SensorManager mSensorManager;
	private int adcval;
	private float maxval;
	private int screenwidth;
	private int screenheight;
	private float unit;
	private int paddlewidth;
	private int paddleheight;
	
	Paddle(Context context){
		this.selfimage = new RectF(0,0, 100, 100); //set coordinates to upper left
		
		paddlewidth = 30;
		paddleheight = 10;
		
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
		maxval = adcsensor.getMaximumRange();
		
		/*Get Window dimensions*/
		Point size = new Point();
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
		      wm.getDefaultDisplay().getSize(size);
		      screenwidth = size.x;
		      screenheight = size.y; 
		    }else{
		      Display d = wm.getDefaultDisplay(); 
		      screenwidth = d.getWidth(); 
		      screenheight = d.getHeight(); 
		    }
		
		setUnit();
		
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
		
		/*Get these values to be smoother and make it so screenheight is the bottom in manifest*/
		float top = 900;
		float left = (adcval  * unit * -1) + (screenwidth/2) - (paddlewidth/2);
		float right = (adcval * unit * -1)+ (screenwidth/2) + (paddlewidth/2);
		float bottom = screenheight;
		selfimage.set(left, top, right , bottom); 
	}
	
	public void startMoving(){
		moveSelf.run();
	}
	
	public void stopMoving(){
		timer.removeCallbacks(moveSelf);
	}
	
	private void setUnit(){
		unit = screenwidth / maxval;
	}
}
