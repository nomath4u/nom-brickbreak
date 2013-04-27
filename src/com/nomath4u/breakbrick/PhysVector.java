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
		double xspeed = mag * Math.cos(direction);
		return xspeed;
	}
	
	public double speedY(){
		double yspeed = mag * Math.sin(direction);
		return yspeed;
	}
	
	public void flipY(){
		this.direction = this.direction * -1;
	}
	
	public void flipX(){
		this.direction = 180 - this.direction;
		flipY();
	}
}

