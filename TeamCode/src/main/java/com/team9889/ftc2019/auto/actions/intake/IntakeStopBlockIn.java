package com.team9889.ftc2019.auto.actions.intake;

import com.team9889.ftc2019.auto.actions.Action;
import com.team9889.ftc2019.subsystems.Robot;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

/**
 * Created by Eric on 11/22/2019.
 */
public class IntakeStopBlockIn extends Action {
    @Override
    public void setup(String args) {

    }

    @Override
    public void start() {

    }

    @Override
    public void update()
    {
        Robot.getInstance().update();
        if (Robot.getInstance().blockDetector.getDistance(DistanceUnit.INCH) < 4){
            Robot.getInstance().getIntake().SetRollerPower(0);
        }
    }

    @Override
    public boolean isFinished() {
        return Robot.getInstance().intakeLeft.getVelocity() == 0;
    }

    @Override
    public void done() {

    }
}
