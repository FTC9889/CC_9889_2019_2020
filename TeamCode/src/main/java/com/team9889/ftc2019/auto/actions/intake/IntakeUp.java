package com.team9889.ftc2019.auto.actions.intake;

import com.qualcomm.robotcore.util.ElapsedTime;
import com.team9889.ftc2019.auto.actions.Action;
import com.team9889.ftc2019.subsystems.Robot;

/**
 * Created by Eric on 11/22/2019.
 */
public class IntakeUp extends Action {
    private ElapsedTime timer = new ElapsedTime();

    @Override
    public void setup(String args) {

    }

    @Override
    public void start() {
        Robot.getInstance().getIntake().IntakeUp();
        timer.reset();
    }

    @Override
    public void update() {
        Robot.getInstance().update();
    }

    @Override
    public boolean isFinished() {
        return timer.milliseconds() > 1000;
    }

    @Override
    public void done() {

    }
}
