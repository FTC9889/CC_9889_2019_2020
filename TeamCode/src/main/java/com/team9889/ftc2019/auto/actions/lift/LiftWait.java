package com.team9889.ftc2019.auto.actions.lift;

import com.qualcomm.robotcore.util.ElapsedTime;
import com.team9889.ftc2019.auto.actions.Action;
import com.team9889.ftc2019.subsystems.Robot;

/**
 * Created by Eric on 2/24/2020.
 */
public class LiftWait extends Action {
    int time, liftTime;
    double LiftPower;
    ElapsedTime timer = new ElapsedTime(), liftTimer = new ElapsedTime();

    public LiftWait(int time, double liftPower, int liftTime){
        this.time = time;
        this.LiftPower = liftPower;
        this.liftTime = liftTime;
    }

    @Override
    public void setup(String args) {

    }

    @Override
    public void start() {

    }

    @Override
    public void update() {
        if (timer.milliseconds() >= time) {
            Robot.getInstance().getLift().SetLiftPower(LiftPower);
        } else {
            liftTimer.reset();
        }

        if (Robot.getInstance().downLimit.isPressed() && LiftPower > 0){
            Robot.getInstance().getLift().SetLiftPower(0);
        }
    }

    @Override
    public boolean isFinished() {
        return liftTimer.milliseconds() > liftTime;
    }

    @Override
    public void done() {
        Robot.getInstance().getLift().SetLiftPower(0);
    }
}
