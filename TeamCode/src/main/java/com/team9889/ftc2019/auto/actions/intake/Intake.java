package com.team9889.ftc2019.auto.actions.intake;

import com.team9889.ftc2019.auto.actions.Action;
import com.team9889.ftc2019.subsystems.Robot;

/**
 * Created by Eric on 11/22/2019.
 */
public class Intake extends Action {

    private boolean intake = true;
    private boolean fullSpeed = false;

    public Intake() {
        this.intake = true;
        this.fullSpeed = false;
    }

    public Intake(boolean intake) {
        this.intake = intake;
        this.fullSpeed = false;
    }

    public Intake(boolean intake, boolean fullSpeed) {
        this.intake = intake;
        this.fullSpeed = fullSpeed;
    }

    @Override
    public void setup(String args) {

    }

    @Override
    public void start() {

        if (intake) {
            Robot.getInstance().getIntake().SetIntakePower(fullSpeed ? 1.0 : 0.5);
            Robot.getInstance().getIntake().SetRollerPower(1);
        } else {
            Robot.getInstance().getIntake().SetIntakePower(0);
            Robot.getInstance().getIntake().SetRollerPower(0);
        }

    }

    @Override
    public void update() {
        Robot.getInstance().update();
    }

    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    public void done() {}
}
