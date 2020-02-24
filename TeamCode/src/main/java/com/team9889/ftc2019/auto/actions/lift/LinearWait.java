package com.team9889.ftc2019.auto.actions.lift;

import com.qualcomm.robotcore.util.ElapsedTime;
import com.team9889.ftc2019.auto.actions.Action;
import com.team9889.ftc2019.subsystems.Robot;

/**
 * Created by Eric on 2/24/2020.
 */
public class LinearWait extends Action {
    int time;
    boolean linearBarOut, done = false;
    ElapsedTime timer = new ElapsedTime();

    public LinearWait(int time, boolean linearBarOut){
        this.time = time;
        this.linearBarOut = linearBarOut;
    }

    @Override
    public void setup(String args) {

    }

    @Override
    public void start() {

    }

    @Override
    public void update() {
        if (timer.milliseconds() >= time && linearBarOut){
            Robot.getInstance().getLift().LinearBarOut();
            done = true;
        }else if (timer.milliseconds() >= time && !linearBarOut){
            Robot.getInstance().getLift().LinearBarIn();
            done = true;
        }
    }

    @Override
    public boolean isAtPose() {
        return done;
    }

    @Override
    public void done() {

    }
}
