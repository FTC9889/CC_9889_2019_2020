package com.team9889.ftc2019.auto.actions.intake;

import com.qualcomm.robotcore.util.ElapsedTime;
import com.team9889.ftc2019.auto.actions.Action;
import com.team9889.ftc2019.subsystems.Robot;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

/**
 * Created by Eric on 11/22/2019.
 */
public class IntakeStopBlockInWaitBlue extends Action {
    private boolean finished = false, first = true;
    private int time;
    private ElapsedTime timer = new ElapsedTime(), waitTimer = new ElapsedTime();

    public IntakeStopBlockInWaitBlue(int time){
        this.time = time;
    }

    @Override
    public void setup(String args) {

    }

    @Override
    public void start() {
        timer.reset();
        waitTimer.reset();
    }

    @Override
    public void update() {
        if (waitTimer.milliseconds() > time){
            if (first) {
                Robot.getInstance().grabber.setPosition(1);
                first = false;
            }

            Robot.getInstance().intakeLeft.setPower(.85);
            Robot.getInstance().intakeRight.setPower(.7);
            Robot.getInstance().getIntake().SetRollerPower(.5);
        }
    }

    @Override
    public boolean isFinished() {
        if (waitTimer.milliseconds() > time) {
            if (timer.milliseconds() > 130) {
                timer.reset();
                return Robot.getInstance().blockDetector.getDistance(DistanceUnit.INCH) < 4;
            } else
                return false;
        }
        else
            return false;
    }

    @Override
    public void done() {
        Robot.getInstance().getIntake().SetIntakePower(-.15);
        Robot.getInstance().getIntake().SetRollerPower(0);
        Robot.getInstance().getLift().GrabberClose();
    }
}
