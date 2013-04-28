package com.nomath4u.breakbrick;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class SurfacePanel extends SurfaceView implements SurfaceHolder.Callback {
	public boolean _run;
	private Bitmap mBitmap;
	public boolean isDrawing = true;
	protected DrawThread thread;
	public Paddle mainPaddle;
	public Ball mainBall;
	public MainActivity parent;
	public boolean over = false;
	public List<Brick> bricks;
	
	
	public SurfacePanel(Context context){
		super(context);
		getHolder().addCallback(this);
		thread = new DrawThread(getHolder());
		mainPaddle = new Paddle(context);
		mainBall = new Ball(context);
		parent = (MainActivity)context;
		bricks = new ArrayList<Brick>();
		
		
		/*Create the bricks*/
		createBricks();
		
	}
	
	public void surfaceChanged(SurfaceHolder holder, int format, int width,  int height) {
        // Create a Bitmap with the dimensions of the View
        mBitmap =  Bitmap.createBitmap (width, height, Bitmap.Config.ARGB_8888);;
    }
 
    public void surfaceCreated(SurfaceHolder holder) {
        // Starts thread execution
        thread.setRunning(true);
        thread.start();
    }
 
    public void surfaceDestroyed(SurfaceHolder holder) {
        // Finish thread execution
        boolean retry = true;
        thread.setRunning(false);
        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
                // we will try it again and again...
            }
        }
    }
    
    public void reset(){
    	mainBall.setBallCharacteristics(parent.level);
    	mainBall.spawn();
    	parent.lives = 5;
    	parent.score = 0;
    	parent.level = 1;
    	over = false;
    	/*Remove and then re-add bricks*/
    	bricks.clear();
    	createBricks();
    }
    
    private void createBricks(){
    	int rows = 1;
		for(int i = 0; i < (rows * 6); i++ ){
			Brick tmpBrick = new Brick((i+1),mainPaddle.screenwidth, mainPaddle.screenheight/*, bricks*/);
			bricks.add(tmpBrick);
		}
    }
    
    private void nextLevel(){
    	parent.level = parent.level+1;
    	mainBall.setBallCharacteristics(parent.level);
    	mainBall.spawn();
    	createBricks();
    }
	
	 class DrawThread extends  Thread {
	        private SurfaceHolder mSurfaceHolder;
	 
	        public DrawThread (SurfaceHolder surfaceHolder){
	            mSurfaceHolder = surfaceHolder;
	        }
	 
	        public void setRunning(boolean run2) {
	            _run = run2;
	        }
	 
	        @Override
	        public void run() {
	            Canvas canvas = null;
	 
	            Paint paint = new Paint();
	            paint.setColor(Color.RED);
	            paint.setStrokeWidth(1);
	            paint.setTextSize(20);
	            
	            Paint brickPaint = new Paint();
	            brickPaint.setColor(Color.GREEN);
	            brickPaint.setStrokeWidth(1);
	            
	            
	            
	 
	            while (_run){
	                if(isDrawing == true){
	                    try{
	                        canvas = mSurfaceHolder.lockCanvas(null);
	                        if(mBitmap == null){
	                            mBitmap =  Bitmap.createBitmap (1, 1, Bitmap.Config.ARGB_8888);
	                        }
	                        final Canvas c = new Canvas (mBitmap);
	 
	                     
	                        c.drawColor(Color.BLACK);
	                        
	                        if(parent.lives > 0){
	                        
	                        
	                        	c.drawRect(mainPaddle.selfimage, mainPaddle.selfstyle);
	                        	c.drawRect(mainBall.image, mainBall.paint);
	                        	/*Draw the Bricks*/
	                        	if(bricks.size() != 0){
	                        		
	                        	
	                        		for(Brick tmpBrick : bricks){
	                        				c.drawRect(tmpBrick.image, brickPaint);
	                        		}
	                        	}
	                        	else{
	                        		nextLevel();
	                        	}
	                        	c.drawText("Score:"+parent.score + "  Lives: " + parent.lives + " Level : " + parent.level, 0, 100, paint);
	                        	c.drawText("X: " + (mainBall.image.right - mainBall.image.left), 0, 200, paint);
	                        	c.drawText("Y: " + (mainBall.image.top - mainBall.image.bottom), 0, 300, paint);
	                        	mainBall.tick(); //Tell the ball it needs to move again
	                        	parent.addScore(1);
	                        }
	                        else{
	                        	c.drawText("Game Over",mainPaddle.screenwidth/2 , mainPaddle.screenheight/2, paint);
	                        	if(!over){
	                        		parent.gameOver(parent);
	                        		over = true;
	                        	
	                        	}
	                        }
	                        
	                        
	 
	                        canvas.drawBitmap (mBitmap, 0,  0,null);
	                    } finally {
	                        mSurfaceHolder.unlockCanvasAndPost(canvas);
	                    }
	                }
	            }
	        }
	    }
	
}
