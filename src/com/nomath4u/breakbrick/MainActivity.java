package com.nomath4u.breakbrick;




import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;


public class MainActivity extends Activity {
	public int lives;
	public int score;
	public SensorManager mSensorManager;
	public float adcval;
	public float maxval;
	private SensorEventListener adcListener;
	private Sensor adcsensor;
	public SurfacePanel panel;
	public boolean over = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		createSensorManager();
		//WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
		getWindow().addFlags(LayoutParams.FLAG_KEEP_SCREEN_ON); //Get active wakelock
		panel = new SurfacePanel(this);
		setContentView(panel);
		lives = 5;
		score = 0;
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
    @Override
    protected void onResume(){
    	super.onResume();
    	mSensorManager.registerListener(adcListener, adcsensor, SensorManager.SENSOR_DELAY_NORMAL);
    }
    
    @Override
    protected void onRestart(){
    	super.onRestart();
    	mSensorManager.registerListener(adcListener, adcsensor, SensorManager.SENSOR_DELAY_NORMAL);

    }
    
    private void createSensorManager(){
    	mSensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
		adcsensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		adcListener = new SensorEventListener(){
			@Override
			public void onSensorChanged(SensorEvent event){
				adcval = event.values[0];
				
			}
			@Override
			public void onAccuracyChanged(Sensor sensor, int accuracy){
			
			}
		};
		mSensorManager.registerListener(adcListener,adcsensor, SensorManager.SENSOR_DELAY_NORMAL);
		maxval = adcsensor.getMaximumRange();
    }
    
    @Override
    protected void onPause(){
    	super.onPause();
    	mSensorManager.unregisterListener(adcListener);
    }
    
    @Override
    protected void onDestroy(){
    	super.onDestroy();
    	mSensorManager.unregisterListener(adcListener);
    }
	
	public void addScore(int add){
		score = score + add;
		
	}
	
	public void addLife(int add){
		lives = lives + add;
	}
	
	public void gameOver(final Context context){
		runOnUiThread(new Runnable(){
			public void run(){
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
		        //builder.setView(checkBoxView);
		        builder.setCancelable(true);
		        builder.setInverseBackgroundForced(true);
		        builder.setTitle(R.string.game_over_title);
		        builder.setMessage(R.string.game_over_message);
		        builder.setPositiveButton("OK",
		                new DialogInterface.OnClickListener() {
		                    @Override
		                    public void onClick(DialogInterface dialog,
		                            int which) {
		                    	panel.reset();
		                		dialog.dismiss();
		                        
		                    }
		                });
		        builder.setNegativeButton("Exit",
		                new DialogInterface.OnClickListener() {
		                    @Override
		                    public void onClick(DialogInterface dialog,
		                            int which) {

		                		dialog.dismiss();
		                		panel._run = false; /*Stop the thread*/
		                		Intent intent = new Intent(Intent.ACTION_MAIN);
		                		intent.addCategory(Intent.CATEGORY_HOME);
		                		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		                		startActivity(intent);
		                        
		                    }
		                });
		        AlertDialog alert = builder.create();
		        alert.show();
			}
		});   
	}		
		
	     
	        
		
	
}
