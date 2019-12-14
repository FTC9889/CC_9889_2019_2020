package com.team9889.ftc2019.auto.actions;

import com.team9889.ftc2019.subsystems.Robot;

/**
 * Created by Eric on 12/13/2019.
 */
public class RobotUpdate extends Action{
    @Override
    public void setup(String args) {

    }

    @Override
    public void start() {

    }

    @Override
    public void update() {
        Robot.getInstance().update();
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void done() {

    }
}
