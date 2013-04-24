package com.nomath4u.breakbrick;

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
	public Brick testBrick;
	
	
	public SurfacePanel(Context context){
		super(context);
		getHolder().addCallback(this);
		thread = new DrawThread(getHolder());
		mainPaddle = new Paddle(context);
		mainBall = new Ball(context);
		parent = (MainActivity)context;
		
		
		/*Create the bricks*/
		testBrick = new Brick(1);
		
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
    	mainBall.setBallCharacteristics();
    	mainBall.spawn();
    	parent.lives = 5;
    	parent.score = 0;
    	over = false;
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
	 
	                        /*c.drawColor(Color.WHITE);
	                        c.drawCircle(80,80, 30, paint);
	                        c.drawLine(80, 80, 80, 200, paint);
	                        c.drawText(""+canvas.getWidth()+", "+canvas.getHeight(), 0, 200,paint);*/
	                        
	                        /*Clear Canvas*/
	                        c.drawColor(Color.BLACK);
	                        
	                        if(parent.lives > 0){
	                        
	                        
	                        	c.drawRect(mainPaddle.selfimage, mainPaddle.selfstyle);
	                        	c.drawRect(mainBall.image, mainBall.paint);
	                        	/*Draw the Bricks*/
	                        	c.drawRect(testBrick.image,brickPaint);
	                        	
	                        	c.drawText("Score:"+parent.score + "  Lives: " + parent.lives, 0, 100, paint);
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
