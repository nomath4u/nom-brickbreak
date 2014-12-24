package com.nomath4u.breakbrick;


import android.util.Log;

import java.util.TimerTask;

public class TickRunner extends TimerTask {
    private Ball ball;
    
    public void setBall(Ball _ball) {
        this.ball = _ball;
    }

    public void run(){
        ball.tick();
        Log.d("TAG", "TICK");

    }

}