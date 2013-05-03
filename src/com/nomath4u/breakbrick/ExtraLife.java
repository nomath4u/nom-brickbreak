package com.nomath4u.breakbrick;

import android.graphics.RectF;

public class ExtraLife {
	public RectF image;
	public PhysVector velocity;
	private static int width = 7;
	private static int height = 7;
	ExtraLife(int left, int top){
		this.velocity = new PhysVector(2, 270);
		this.image = new RectF(left, top, left + 7, top + 7);
	}
}
