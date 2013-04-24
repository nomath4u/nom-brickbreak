package com.nomath4u.breakbrick;

import android.graphics.RectF;

public class Brick {
	private int brickWidth = 70;
	private int brickHeight = 15;
	public RectF image;
	
	Brick(int number){
		image = new RectF(260, 340, 260 + brickWidth, 340 + brickHeight);
		
	}
	
	public void destroySelf(){
		image.set(0,0,0,0);
	}
	

}
