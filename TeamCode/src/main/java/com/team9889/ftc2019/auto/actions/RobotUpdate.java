package com.team9889.ftc2019.auto.actions;

import com.qualcomm.robotcore.util.ElapsedTime;
import com.team9889.ftc2019.subsystems.Robot;

/**
 * Created by Eric on 12/13/2019.
 */
public class RobotUpdate extends Action{
    private ElapsedTime time;

    public RobotUpdate(ElapsedTime autoTimer) {
        time = autoTimer;
    }

    @Override
    public void setup(String args) {

    }

    @Override
    public void start() {

    }

    @Override
    public void update() {
        Robot.getInstance().update();

        if(time.milliseconds() > 29 * 1000)
            Robot.getInstance().getMecanumDrive().writeAngleToFile();
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void done() {
        Robot.getInstance().getMecanumDrive().writeAngleToFile();
    }
}
