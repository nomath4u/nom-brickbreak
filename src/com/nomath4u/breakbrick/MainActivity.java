package com.nomath4u.breakbrick;




import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.Window;
import android.widget.Toast;
import com.google.android.gms.common.api.*;
import com.google.example.games.basegameutils.BaseGameActivity;
import com.nomath4u.breakbrick.R;


public class MainActivity extends BaseGameActivity {
	public int lives;
	public int score;
	public int level;
	public SensorManager mSensorManager;
	public float adcval;
	public float maxval;
	private SensorEventListener adcListener;
	private Sensor adcsensor;
	public SurfacePanel panel;
	public boolean over = false;
	public SoundPool pool;
	public int soundID;
	public int soundIDa;
	boolean loaded = false;
    private float vals[] = new float[] {0,0,0,0,0,0,0,0,0,0};
    public static final int AVGS = 10;
    private int orientation;
    private boolean isSignedIn = false;

    public interface Listener {
        public void onStartGameRequested(boolean hardMode);
        public void onShowAchievementsRequested();
        public void onShowLeaderboardsRequested();
        public void onSignInButtonClicked();
        public void onSignOutButtonClicked();
    }
    class AccomplishmentsOutbox {
        boolean mPrimeAchievement = false;
        boolean mHumbleAchievement = false;
        boolean mLeetAchievement = false;
        boolean mArrogantAchievement = false;
        int mBoredSteps = 0;
        int mScore = -1;


        boolean isEmpty() {
            return  mScore < 0;
        }
    }
    Listener mListener = null;
    AccomplishmentsOutbox mOutbox = new AccomplishmentsOutbox();
    final int RC_RESOLVE = 5000, RC_UNUSED = 5001;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, LayoutParams.FLAG_FULLSCREEN);
		createSensorManager();
		//WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
		getWindow().addFlags(LayoutParams.FLAG_KEEP_SCREEN_ON); //Get active wakelock
		this.panel = new SurfacePanel(this);
		//setContentView(this.panel);
		setContentView(R.layout.main_menu);
        lives = 5;
		score = 0;
		level = 1;

		/*Setup sounds */
		pool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        pool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId,
                    int status) {
                loaded = true;
            }
        });
        soundID = pool.load(this, R.raw.blip1, 1);
        soundIDa= pool.load(this,R.raw.blip2, 0);

        /*Default orientation*/
        this.orientation = getDeviceDefaultOrientation();




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
    	mSensorManager.registerListener(adcListener, adcsensor, SensorManager.SENSOR_DELAY_GAME);
        panel._run = true;
    }

    @Override
    protected void onRestart(){
    	super.onRestart();
    	mSensorManager.registerListener(adcListener, adcsensor, SensorManager.SENSOR_DELAY_GAME);
        panel._run = true;
    }

    private void createSensorManager(){
    	mSensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
		adcsensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		adcListener = new SensorEventListener(){
			@Override
			public void onSensorChanged(SensorEvent event){
                /*Most devices are default portrait*/
                if(orientation == Configuration.ORIENTATION_PORTRAIT){
				    avgAdc(event.values[0]);
                }
                else{ //But not all of them
                    avgAdc(event.values[1]);
                }

			}
			@Override
			public void onAccuracyChanged(Sensor sensor, int accuracy){

			}
		};
		mSensorManager.registerListener(adcListener,adcsensor, SensorManager.SENSOR_DELAY_GAME);
		maxval = adcsensor.getMaximumRange();
    }

    @Override
    protected void onPause(){
    	super.onPause();
    	mSensorManager.unregisterListener(adcListener);
        panel._run = false;
    }

    @Override
    protected void onDestroy(){
    	super.onDestroy();
    	mSensorManager.unregisterListener(adcListener);
        panel._run= false;
    }

	public void addScore(int add){
		score = score + add;

	}

	public void addLife(int add){
		lives = lives + add;
	}

	public void gameOver(final Context context){
		final int highscore = getHighScore();
		if(score > highscore){
			SharedPreferences manager = getPreferences(MODE_PRIVATE);
			SharedPreferences.Editor edit = manager.edit();
			edit.putInt("highScore", score);
			edit.commit();
		}
		runOnUiThread(new Runnable(){
			public void run(){
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
		        builder.setCancelable(true);
		        builder.setInverseBackgroundForced(true);
		        builder.setTitle(R.string.game_over_title);

		        if(score > highscore){
		        builder.setMessage("New High Score! Your score was: " + Integer.toString(score));
		        }
		        else
		        	 builder.setMessage("Your score was: " + Integer.toString(score) + "\n Current High Score: " + Integer.toString(highscore));
		        builder.setPositiveButton("Main Menu",
		                new DialogInterface.OnClickListener() {
		                    @Override
		                    public void onClick(DialogInterface dialog,
		                            int which) {

		                    	panel.reset();
                                returnMenu();
		                		dialog.dismiss();

		                    }
		                });
		        builder.setNegativeButton("Exit",
		                new DialogInterface.OnClickListener() {
		                    @Override
		                    public void onClick(DialogInterface dialog,
		                            int which) {

		                		dialog.dismiss();
		                		finish();

		                    }
		                });
		        AlertDialog alert = builder.create();
		        alert.show();

                if(isSignedIn){
                    pushToLeader(score);
                }
                if(!mOutbox.isEmpty()){
                    pushAccomplishments();
                }
			}
		});
	}


	 private int getHighScore(){
		 SharedPreferences manager = getPreferences(MODE_PRIVATE);
		 int highscore = manager.getInt("highScore", 0);
		 return highscore;
	 }

	 public void backToGame(View view){
		 panel.unpause();
	 }

    public void startGame(View view){
        if(this.panel == null){
            this.panel = new SurfacePanel(this); // make a new one if there isn't one already
        }
        setContentView(this.panel);
    }

    public void avgAdc(float mValues){
        float total = 0;


        /*This loop handles entire reassignment except for the newly incoming one handled immediately after*/
        for( int i = 0; i < (AVGS-1); i++){
            this.vals[i] = this.vals[i+1];
        }
        //this.vals[AVGS] = mValues;


        /*this.vals[0]= this.vals[1];
        this.vals[1]= this.vals[2];
        this.vals[2]= this.vals[3];
        this.vals[3]= this.vals[4];*/
        this.vals[AVGS-1] = mValues;
       // this.adcval = ((this.vals[0] + this.vals[1] + this.vals[2] + this.vals [3] + this.vals [4])/5);
        for(int j = 0; j < AVGS; j++){
            total += this.vals[j];
        }
        this.adcval = (total/AVGS);
    }

    public void selfDestruct(View view){
        /*panel._run = false;
        panel = null;
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);*/
        //moveTaskToBack(true);
        finish();
    }
    public void viewHighScore(View view){
       int highscore = getHighScore();
       int duration = Toast.LENGTH_LONG;
       Toast toast = Toast.makeText(getApplicationContext(), String.valueOf(highscore), duration);
       toast.show();
    }

    /*To be able to access reset as a button*/
    public void reset(View view){
        panel.reset();
    }

    public int getDeviceDefaultOrientation() {

        WindowManager windowManager =  (WindowManager) getSystemService(WINDOW_SERVICE);

        Configuration config = getResources().getConfiguration();

        int rotation = windowManager.getDefaultDisplay().getRotation();

        if ( ((rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180) &&
                config.orientation == Configuration.ORIENTATION_LANDSCAPE)
                || ((rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270) &&
                config.orientation == Configuration.ORIENTATION_PORTRAIT)) {
            return Configuration.ORIENTATION_LANDSCAPE;
        }
            else
            return Configuration.ORIENTATION_PORTRAIT;
        }

    public void onClick(View view) {
        switch (view.getId()) {
            /*case R.id.easy_mode_button:
                mListener.onStartGameRequested(false);
                break;
            case R.id.hard_mode_button:
                mListener.onStartGameRequested(true);
                break;
            case R.id.show_achievements_button:
                mListener.onShowAchievementsRequested();
                break;
            case R.id.show_leaderboards_button:
                mListener.onShowLeaderboardsRequested();
                break;
            case R.id.sign_in_button:
                mListener.onSignInButtonClicked();
                break;
            case R.id.sign_out_button:
                mListener.onSignOutButtonClicked();
                break;*/
            case R.id.sign_in_button:
                mListener.onSignInButtonClicked();
                break;
        }
    }
    //@Override
    public void onSignInButtonClicked() {
        // check if developer read the documentation!
        // (Note: in a production application, this code should NOT exist)
        /*if (!verifyPlaceholderIdsReplaced()) {
            showAlert("Sample not set up correctly. See README.");
            return;
        }*/

        // start the sign-in flow
        //beginUserInitiatedSignIn();
        Toast toast = Toast.makeText(getApplicationContext(), "yay", Toast.LENGTH_SHORT);
        toast.show();
    }

    public void Test(View view){
        beginUserInitiatedSignIn();
    }



    @Override
    public void onSignInFailed() {

    }

    @Override
    public void onSignInSucceeded() {
        this.isSignedIn = true;
    }

    public void onShowLeaderboardsRequested(View view) {
        if (isSignedIn()) {
            startActivityForResult(getGamesClient().getAllLeaderboardsIntent(), RC_UNUSED);
        } else {
            showAlert(getString(R.string.leaderboards_not_available));
        }
    }

    public void pushToLeader(int score){
        mOutbox.mScore = score;
    }

    void pushAccomplishments() {

        /*if (mOutbox.mPrimeAchievement) {
            getGamesClient().unlockAchievement(getString(R.string.achievement_prime));
            mOutbox.mPrimeAchievement = false;
        }
        if (mOutbox.mArrogantAchievement) {
            getGamesClient().unlockAchievement(getString(R.string.achievement_arrogant));
            mOutbox.mArrogantAchievement = false;
        }
        if (mOutbox.mHumbleAchievement) {
            getGamesClient().unlockAchievement(getString(R.string.achievement_humble));
            mOutbox.mHumbleAchievement = false;
        }
        if (mOutbox.mLeetAchievement) {
            getGamesClient().unlockAchievement(getString(R.string.achievement_leet));
            mOutbox.mLeetAchievement = false;
        }
        if (mOutbox.mBoredSteps > 0) {
            getGamesClient().incrementAchievement(getString(R.string.achievement_really_bored),
                    mOutbox.mBoredSteps);
            getGamesClient().incrementAchievement(getString(R.string.achievement_bored),
                    mOutbox.mBoredSteps);
        }
        if (mOutbox.mEasyModeScore >= 0) {
            getGamesClient().submitScore(getString(R.string.leaderboard_easy),
                    mOutbox.mEasyModeScore);
            mOutbox.mEasyModeScore = -1;
        }*/
        if (mOutbox.mScore >= 0) {
            getGamesClient().submitScore(getString(R.string.leaderboard),
                    mOutbox.mScore);
            mOutbox.mScore = -1;
        }

    }
    public void clickreturnMenu(View view){
        returnMenu();
    }

    public void returnMenu(){
        panel.reset();
        panel._run = false; //Have to turn it off so the canvas doesn't get mad
        setContentView(R.layout.main_menu);
        panel = null; //Setting to null so we can make a new one if we need to later
    }
}