package com.nomath4u.breakbrick;

import android.graphics.RectF;

public class Brick {
	private int brickWidth = 70;
	private int brickHeight = 15;
	public RectF image;
	
	Brick(int number,int screenwidth, int screenheight){
		/*six in a row with 10 on each side*/
		int xoffset = 10 * (number % 7) ;
		int yoffset = 30 + (30 * (number/7));
		image = new RectF(xoffset + (((number % 7)-1) * (brickWidth)), yoffset, xoffset + (((number % 7) - 1) * (brickWidth)) + brickWidth, yoffset + brickHeight);
		
	}
	
	public void destroySelf(){
		image.set(0,0,0,0);
	}
	

}
