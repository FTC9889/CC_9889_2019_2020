package com.team9889.ftc2019.auto.actions.drive;

import com.qualcomm.robotcore.util.ElapsedTime;
import com.team9889.ftc2019.auto.actions.Action;
import com.team9889.ftc2019.subsystems.Robot;

/**
 * Created by Eric on 2/24/2020.
 */
public class FoundationWait extends Action {
    int time;
    boolean FoundationDown, done = false;
    ElapsedTime timer = new ElapsedTime();

    public FoundationWait(int time, boolean foundationDown){
        this.time = time;
        this.FoundationDown = foundationDown;
    }

    @Override
    public void setup(String args) {

    }

    @Override
    public void start() {

    }

    @Override
    public void update() {
        if (timer.milliseconds() >= time && FoundationDown){
            Robot.getInstance().getMecanumDrive().CloseFoundationHook();
            done = true;
        }else if (timer.milliseconds() >= time && !FoundationDown){
            Robot.getInstance().getMecanumDrive().OpenFoundationHook();
            done = true;
        }
    }

    @Override
    public boolean isFinished() {
        return done;
    }

    @Override
    public void done() {

    }
}
