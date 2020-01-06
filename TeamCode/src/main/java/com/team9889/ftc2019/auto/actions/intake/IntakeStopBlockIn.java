package com.team9889.ftc2019.auto.actions.intake;

import com.qualcomm.robotcore.util.ElapsedTime;
import com.team9889.ftc2019.auto.actions.Action;
import com.team9889.ftc2019.subsystems.Robot;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

/**
 * Created by Eric on 11/22/2019.
 */
public class IntakeStopBlockIn extends Action {
    private ElapsedTime timer = new ElapsedTime();
    private boolean finished = false;

    @Override
    public void setup(String args) {

    }

    @Override
    public void start() {
        timer.reset();
    }

    @Override
    public void update() {
        if(timer.milliseconds() > 250 && !finished) {
            finished = Robot.getInstance().blockDetector.getDistance(DistanceUnit.INCH) < 4;
            timer.reset();
        } else {
            if(timer.milliseconds() > 250 + 100 && timer.milliseconds() < 250 + 200) {
                Robot.getInstance().getIntake().SetIntakePower(0);
            } else if(timer.milliseconds() > 250 + 200 && timer.milliseconds() < 250 + 300) {
                Robot.getInstance().getIntake().SetRollerPower(0);
            } else if(timer.milliseconds() > 250 + 300 && timer.milliseconds() < 250 + 350) {
                Robot.getInstance().getLift().GrabberClose();
            } else if (timer.milliseconds() > 250 + 350){
                Robot.getInstance().getIntake().SetIntakePower(-.4);
            }
        }
    }

    @Override
    public boolean isFinished() {
        return timer.milliseconds() > 600 + 3000;
    }

    @Override
    public void done() {
        Robot.getInstance().getIntake().SetIntakePower(0);
    }
}
