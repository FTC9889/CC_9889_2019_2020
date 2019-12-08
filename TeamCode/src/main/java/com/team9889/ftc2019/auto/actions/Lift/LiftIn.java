package com.team9889.ftc2019.auto.actions.Lift;

import com.qualcomm.robotcore.util.ElapsedTime;
import com.team9889.ftc2019.auto.actions.Action;
import com.team9889.ftc2019.subsystems.Robot;

/**
 * Created by Eric on 11/22/2019.
 */
public class LiftIn extends Action {

    @Override
    public void setup(String args) {

    }

    @Override
    public void start() {
        Robot.getInstance().getLift().LinearBarIn();
    }

    @Override
    public void update() {

    }

    @Override
    public boolean isFinished()
    {
        return true;
    }

    @Override
    public void done() {
    }
}
