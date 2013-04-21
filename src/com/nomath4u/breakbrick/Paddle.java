package com.nomath4u.breakbrick;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class Paddle {
	
	public Rect selfimage;
	public Paint selfstyle;
	
	Paddle(){
		this.selfimage = new Rect(0,0, 100, 100); //set coordinates to upper left
		
		/*Set Paint Style*/
		this.selfstyle = new Paint();
		selfstyle.setColor(Color.BLUE);
		
	}

}
