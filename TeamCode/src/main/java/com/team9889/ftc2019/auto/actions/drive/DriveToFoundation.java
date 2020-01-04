package com.team9889.ftc2019.auto.actions.drive;

import com.qualcomm.robotcore.util.ElapsedTime;
import com.team9889.ftc2019.auto.actions.Action;
import com.team9889.ftc2019.subsystems.MecanumDrive;
import com.team9889.ftc2019.subsystems.Robot;
import com.team9889.lib.control.controllers.PID;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

/**
 * Created by Eric on 12/27/2019.
 */
public class DriveToFoundation extends Action {

    public DriveToFoundation(int timeOut){
        this.timeOut = timeOut;
    }

    private PID turnPID = new PID(0.02, 0, 0.3);

    private MecanumDrive mDrive = Robot.getInstance().getMecanumDrive();
    private double angle = 180;
    private int angleCounter = 0;
    private int timeOut = 30000;
    private ElapsedTime timer = new ElapsedTime();

    @Override
    public void setup(String args) {}

    @Override
    public void start() {
        Robot.getInstance().foundationHook.setPosition(.7);

        timer.reset();
    }

    @Override
    public void update() {
        double currentAngle = mDrive.gyroAngle.getTheda(AngleUnit.RADIANS);
        double dx = Math.cos(Math.toRadians(angle) - currentAngle);
        double dy = Math.sin(Math.toRadians(angle) - currentAngle);
        double turn = Math.toDegrees(Math.atan2(dy, dx));
        turn *= -1;

        double rotation = turnPID.update(turn, 0);

        mDrive.setPower(0, -.3, rotation);
    }

    @Override
    public boolean isFinished() {
        if (Math.abs(turnPID.getError()) < 3) angleCounter++;

        return (Robot.getInstance().foundationDetector.getDistance(DistanceUnit.INCH) <= 2.2 && angleCounter > 3) || timer.milliseconds() > timeOut;
    }

    @Override
    public void done() {
        mDrive.setPower(0,0,0);
    }
}
