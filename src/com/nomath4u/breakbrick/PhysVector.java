package com.nomath4u.breakbrick;

import java.lang.Math;

public class PhysVector {
	public double mag;
	public double direction;

	
	PhysVector(float mag, float direction){
		this.mag = mag;
		this.direction = direction;
	}
	
	public double speedX(){
		return this.mag * Math.cos(Math.toRadians(direction));
	}
	
	public double speedY(){
		return Math.sin(Math.toRadians(direction))*mag;
	}
	
	public void flipY(){
		
		this.direction = (-1 * this.direction); 
	}
	
	public void flipX(){
		
		flipY();
		this.direction = (this.direction + 180);
	}
}

