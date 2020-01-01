package com.team9889.ftc2019.auto.actions.Lift;

import com.team9889.ftc2019.Constants;
import com.team9889.ftc2019.auto.actions.Action;
import com.team9889.ftc2019.subsystems.Robot;
import com.team9889.lib.CruiseLib;

/**
 * Created by Eric on 12/27/2019.
 */
public class LiftUp extends Action {
    private double height;

    public LiftUp(double height){
        this.height = height;
    }

    @Override
    public void setup(String args) {

    }

    @Override
    public void start() {
        Robot.getInstance().getLift().SetLiftPower(-.5 * Math.signum(height));
    }

    @Override
    public void update() {

    }

    @Override
    public boolean isFinished() {
        return Robot.getInstance().getLift().getLiftHeightInches() - 1 > height;
    }

    @Override
    public void done() {
        Robot.getInstance().getLift().SetLiftPower(0);
    }
}
