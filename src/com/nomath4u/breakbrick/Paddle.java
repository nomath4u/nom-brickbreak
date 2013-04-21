package com.nomath4u.breakbrick;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;

public class Paddle {
	
	public Rect selfimage;
	public Paint selfstyle;
	private Handler timer;
	private Runnable moveSelf;
	
	Paddle(){
		this.selfimage = new Rect(0,0, 100, 100); //set coordinates to upper left
		
		/*Set Paint Style*/
		this.selfstyle = new Paint();
		selfstyle.setColor(Color.BLUE);
		
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
		selfimage.offset(1,1); //Move the rectangle southeast
	}
	
	public void startMoving(){
		moveSelf.run();
	}
	
	public void stopMoving(){
		timer.removeCallbacks(moveSelf);
	}
}
