package com.team9889.ftc2019.auto.actions.lift;

import com.team9889.ftc2019.auto.actions.Action;
import com.team9889.ftc2019.subsystems.Robot;

/**
 * Created by Eric on 11/22/2019.
 */
public class LiftOut extends Action {

    @Override
    public void setup(String args) {

    }

    @Override
    public void start() {
        Robot.getInstance().getLift().LinearBarOut();
    }

    @Override
    public void update() {}

    @Override
    public boolean isAtPose() {
        return true;
    }

    @Override
    public void done() {}
}
