package com.nomath4u.breakbrick;

import java.util.List;

import android.graphics.RectF;

public class Brick {
	private int brickWidth = 70;
	private int brickHeight = 15;
	public RectF image;
	public List<Brick> bricklist;
	
	Brick(int number,int screenwidth, int screenheight/*, List<Brick>bricks*/){
		/*six in a row with 10 on each side*/
		//bricklist = bricks;
		int xoffset = 10 + 10 * (number % 7) ;
		int yoffset = 30 + (30 * (number/7));
		image = new RectF(xoffset + (((number % 7)-1) * (brickWidth)), yoffset, xoffset + (((number % 7) - 1) * (brickWidth)) + brickWidth, yoffset + brickHeight);
		
	}
	
	public void destroySelf(){
		//bricklist.remove(this);
	}
	

}
