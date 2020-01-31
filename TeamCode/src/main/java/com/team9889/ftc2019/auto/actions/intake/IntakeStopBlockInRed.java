package com.team9889.ftc2019.auto.actions.intake;

import com.qualcomm.robotcore.util.ElapsedTime;
import com.team9889.ftc2019.auto.actions.Action;
import com.team9889.ftc2019.subsystems.Robot;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

/**
 * Created by Eric on 11/22/2019.
 */
public class IntakeStopBlockInRed extends Action {
    private boolean finished = false;
    private ElapsedTime timer = new ElapsedTime();

    @Override
    public void setup(String args) {

    }

    @Override
    public void start() {
        timer.reset();

        Robot.getInstance().getIntake().SetIntakePower(.5);
        Robot.getInstance().getIntake().SetRollerPower(.5);
    }

    @Override
    public void update() {

    }

    @Override
    public boolean isFinished() {
            return Robot.getInstance().blockDetector.getDistance(DistanceUnit.INCH) < 4;
    }

    @Override
    public void done() {
        Robot.getInstance().getIntake().SetIntakePower(-.15);
        Robot.getInstance().getIntake().SetRollerPower(0);
        Robot.getInstance().getLift().GrabberClose();
    }
}
