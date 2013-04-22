package com.nomath4u.breakbrick;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

public class Ball {
	public Paint paint;
	public RectF image;
	private int screenwidth;
	private int screenheight;
	private int ballwidth;
	private int ballheight;
	
	Ball(Context context){
		setBallCharacteristics();
		getDisplay(context);
		spawnAtStart();
	}
	
	private void setBallCharacteristics(){
			paint = new Paint();
			paint.setColor(Color.RED);
			ballwidth = 5;
			ballheight = ballwidth; //Because the ball should be square
	}
	
	private void spawnAtStart(){
		image = new RectF((screenwidth/2) - (ballwidth/2), (screenheight/2)- (ballheight/2), (screenwidth/2) + (ballwidth/2), (screenheight/2) + (ballheight/2)); //Spawn center of ball at center of screen
	}

	private void getDisplay(Context context){
		Point size = new Point();
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
		      wm.getDefaultDisplay().getSize(size);
		      screenwidth = size.x;
		      DisplayMetrics metrics = new DisplayMetrics();
		      wm.getDefaultDisplay().getMetrics(metrics);
		      

		      switch (metrics.densityDpi) {
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
	}
}
