package com.team9889.ftc2019.auto.actions.intake;

import com.team9889.ftc2019.auto.actions.Action;
import com.team9889.ftc2019.subsystems.Robot;

/**
 * Created by Eric on 11/22/2019.
 */
public class IntakeRollerOn extends Action {
    @Override
    public void setup(String args) {

    }

    @Override
    public void start() {
        Robot.getInstance().getIntake().RollerIn();
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
