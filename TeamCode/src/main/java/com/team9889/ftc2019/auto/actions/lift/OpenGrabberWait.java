package com.team9889.ftc2019.auto.actions.lift;

import com.qualcomm.robotcore.util.ElapsedTime;
import com.team9889.ftc2019.auto.actions.Action;
import com.team9889.ftc2019.subsystems.Robot;

/**
 * Created by Eric on 2/24/2020.
 */
public class OpenGrabberWait extends Action {
    int time;
    boolean grabberOpen, done = false;
    ElapsedTime timer = new ElapsedTime();

    public OpenGrabberWait(int time, boolean grabberOpen){
        this.time = time;
        this.grabberOpen = grabberOpen;
    }

    @Override
    public void setup(String args) {

    }

    @Override
    public void start() {

    }

    @Override
    public void update() {
        if (timer.milliseconds() >= time && grabberOpen){
            Robot.getInstance().getLift().GrabberOpen();
            done = true;
        }else if (timer.milliseconds() >= time && !grabberOpen){
            Robot.getInstance().getLift().GrabberClose();
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
