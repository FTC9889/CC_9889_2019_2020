package com.team9889.ftc2019.auto.actions.Lift;

import com.qualcomm.robotcore.util.ElapsedTime;
import com.team9889.ftc2019.auto.actions.Action;
import com.team9889.ftc2019.subsystems.Robot;

/**
 * Created by Eric on 11/22/2019.
 */
public class LiftOut extends Action {
    private ElapsedTime timer = new ElapsedTime();

    @Override
    public void setup(String args) {

    }

    @Override
    public void start() {
        timer.reset();
    }

    @Override
    public void update() {
        Robot.getInstance().linearBar.setPower(-1);
    }

    @Override
    public boolean isFinished() {
        return timer.milliseconds() > 2700;
    }

    @Override
    public void done() {
        Robot.getInstance().linearBar.setPower(0);
    }
}