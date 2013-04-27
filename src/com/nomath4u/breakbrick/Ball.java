package com.nomath4u.breakbrick;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

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
	public PhysVector velocity;
	private MainActivity parent;
	private static int maxAngle = 80;
	Ball(Context context){
		parent = (MainActivity) context;
		setBallCharacteristics();
		getDisplay(context);
		spawn();
		
	}
	
	public void setBallCharacteristics(){
			paint = new Paint();
			paint.setColor(Color.RED);
			ballwidth = 5;
			ballheight = ballwidth; //Because the ball should be square
			velocity = new PhysVector(5,135);
	}
	
	public void spawn(){
		image = new RectF((screenwidth/2) - (ballwidth/2), (screenheight/2)- (ballheight/2), (screenwidth/2) + (ballwidth/2), (screenheight/2) + (ballheight/2)); //Spawn center of ball at center of screen
	}

	/*Gets the Display characteristics and does it the way it should regardless of android version they are running*/
	/*Cases are for subtracting the notification bar height*/
	@SuppressWarnings("deprecation")
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
	
	public void tick(){
		image.offset((float)velocity.speedX(),-(float)velocity.speedY());
		
		/*Check for paddle collisions*/
		if(image.intersect(parent.panel.mainPaddle.selfimage)){
			rotateToAngle(parent.panel.mainPaddle.selfimage.centerX(), parent.panel.mainPaddle.paddlewidth);
		}
		
		/*Check against screen edges*/
		
		if((image.left < 0) || (image.right > screenwidth)){
			flipXSpeed();
		}
		
		if((image.top < 0)){
			flipYSpeed();
		}
		
		if(image.bottom > screenheight){
			parent.addLife(-1);
			spawn();
		}
		
		/*Check for brick collisions*/
		
		
		 List<Brick> bricks = parent.panel.bricks;
		    Iterator<Brick> brickIterator = bricks.iterator();
		    Brick brick = null;
		    boolean intersected = false;
		    while (! intersected && brickIterator.hasNext())
		    {
		      brick = brickIterator.next();
		      intersected = image.intersect(brick.image);
		    }
		    if (intersected)
		    {
		      bricks.remove(brick);
		      flipYSpeed();
		    }
		
		

	}
	
	private void flipXSpeed(){
		//image.offset(-2 * (float)velocity.speedX(), 0); // Move the ball twice as far because it already moved once too far above
		  //Flip direction
		velocity.flipX();
	}
	
	private void flipYSpeed(){
		//image.offset(0, -2 * (float)velocity.speedY());
		velocity.flipY();
	}
	
	private void rotateToAngle(float paddleCenter, int paddlewidth){
		float distance = (paddleCenter - this.image.right);
		float multiplier = distance/paddlewidth;
		if(distance >0 ){
			velocity.direction = 90 + (multiplier * maxAngle);
		}
		else{
			velocity.direction = 90 - (-1 * multiplier * maxAngle);
		}
		
		
		
	}
}