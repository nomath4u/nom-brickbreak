package com.nomath4u.breakbrick;

import java.math.BigDecimal;
import java.util.List;

import android.graphics.RectF;

public class Brick {
	private float brickWidth;
	private float brickHeight;
	public RectF image;
	public List<Brick> bricklist;
	private static float xmult = ((float)10/(float)540); //getting 0
	private static float ymult = ((float)15/(float)912);
	private static float brickXmult = ((float)78/(float)540);
	private static float brickYmult = ((float)15/(float)912);
	
	Brick(int number,int screenwidth, int screenheight){ 
		float xspace = xmult * screenwidth;
		float yspace = ymult * screenheight;
		brickWidth = brickXmult * screenwidth;
		brickHeight = brickYmult * screenheight;

		image = new RectF((xspace+(xspace+brickWidth)*(number % 6)), ((yspace+brickHeight)*(number /6)), (xspace+(xspace+brickWidth)*(number % 6))+brickWidth, ((yspace+brickHeight)*(number /6)+brickHeight));
		
	}
	


}
