package com.nomath4u.breakbrick;


import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;


public class MainActivity extends Activity {
	public int lives;
	public int score;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(new SurfacePanel(this));
		lives = 5;
		score = 0;
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	public void addScore(int add){
		score = score + add;
		
	}
	
	public void addLife(int add){
		lives = lives + add;
	}
}
