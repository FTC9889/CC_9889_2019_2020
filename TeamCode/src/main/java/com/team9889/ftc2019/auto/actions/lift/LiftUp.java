package com.team9889.ftc2019.auto.actions.lift;

import com.qualcomm.robotcore.util.ElapsedTime;
import com.team9889.ftc2019.auto.actions.Action;
import com.team9889.ftc2019.subsystems.Robot;

/**
 * Created by Eric on 12/27/2019.
 */
public class LiftUp extends Action {
    private double height, power;
    private ElapsedTime timer = new ElapsedTime();

    public LiftUp(double time, double power){
        this.height = time;
        this.power = power;
    }

    @Override
    public void setup(String args) {

    }

    @Override
    public void start() {
        timer.reset();
    }

    @Override
    public void update() {
        Robot.getInstance().getLift().SetLiftPower(power);
    }

    @Override
    public boolean isFinished() {
        return timer.milliseconds() > height;
    }

    @Override
    public void done() {
        Robot.getInstance().getLift().SetLiftPower(0);
    }
}
