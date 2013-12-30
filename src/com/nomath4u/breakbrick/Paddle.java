package com.nomath4u.breakbrick;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

public class Paddle {
	
	public RectF selfimage;
	public Paint selfstyle;
	private Handler timer;
	private Handler timer2;
	private Runnable moveSelf;
	private float maxval;
	public int screenwidth;
	public int screenheight;
	private float unit;
	public int paddlewidth;
	private int paddleheight;
	private MainActivity mainA;
	
	Paddle(Context context){
		this.selfimage = new RectF(0,0, 100, 100); //set coordinates to upper left
		this.mainA = (MainActivity) context;
		
		paddlewidth = 80;
		paddleheight = 15;
		
		/*Set Paint Style*/
		this.selfstyle = new Paint();
		selfstyle.setColor(Color.BLUE);
		
		
		
		/*Get Window dimensions*/
		Point size = new Point();
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
		      wm.getDefaultDisplay().getSize(size);
		      screenwidth = size.x;
		      DisplayMetrics metrics = new DisplayMetrics();
		      wm.getDefaultDisplay().getMetrics(metrics);
		      

		      switch (metrics.densityDpi) {
                  case DisplayMetrics.DENSITY_XXXHIGH:
                      Log.i("display", "XXXHIGH");
                      screenheight = size.y - 128;
                  case DisplayMetrics.DENSITY_XXHIGH:
                      Log.i("display", "XXHIGH");
                      screenheight = size.y - 96;
                  case DisplayMetrics.DENSITY_XHIGH:
                      Log.i("display", "XHIGH");
                      screenheight = size.y - 64;
		          case DisplayMetrics.DENSITY_HIGH:
		              Log.i("display", "high");
		              screenheight = size.y - 48;
		              break;
		          case DisplayMetrics.DENSITY_MEDIUM:
		              Log.i("display", "medium/default");
		              screenheight = size.y - 32;
		              break;
		          case DisplayMetrics.DENSITY_LOW:
		              Log.i("display", "low");
		              screenheight = size.y - 24;
		              break;
		          default:
		              Log.i("display", "Unknown density"); 
		      }
		    
		      }else{
		      Display d = wm.getDefaultDisplay(); 
		      DisplayMetrics metrics = new DisplayMetrics();
		      wm.getDefaultDisplay().getMetrics(metrics);
		   

		      switch (metrics.densityDpi) {
		          case DisplayMetrics.DENSITY_HIGH:
		              Log.i("display", "high");
		              screenheight = d.getHeight() - 48;
		              break;
		          case DisplayMetrics.DENSITY_MEDIUM:
		              Log.i("display", "medium/default");
		              screenheight = d.getHeight() - 32;
		              break;
		          case DisplayMetrics.DENSITY_LOW:
		              Log.i("display", "low");
		              screenheight = d.getHeight() - 24;
		              break;
		          default:
		              Log.i("display", "Unknown density");
		      }
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
		
		float top = screenheight - paddleheight;
		float left = (mainA.adcval  * unit * -1) + (screenwidth/2) - (paddlewidth/2);
		float right = (mainA.adcval * unit * -1)+ (screenwidth/2) + (paddlewidth/2);
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
		unit = (screenwidth / ( .5f *(mainA.maxval)));
	}
	
}
