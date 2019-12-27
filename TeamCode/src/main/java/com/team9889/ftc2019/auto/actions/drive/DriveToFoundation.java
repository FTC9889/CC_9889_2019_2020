package com.team9889.ftc2019.auto.actions.drive;

import android.util.Log;

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

    PID turnPID = new PID(0.02, 0, 0.3);

    boolean offsetAxis = false;

    MecanumDrive mDrive = Robot.getInstance().getMecanumDrive();
    private double distance, angle = 177, angleSpeed;
    private int timeOut = 30000;
    private ElapsedTime timer = new ElapsedTime();

    @Override
    public void setup(String args) {}

    @Override
    public void start() {
//        if(mDrive.gyroAngle.getTheda(AngleUnit.DEGREES) > 0)
//            angle = -18;
//        else
//            angle = 180;

        if (Math.abs(angle) > 175) {
            angle = Math.signum(angle) > 0 ? -(angle - 180) : -(angle + 180);
            offsetAxis = true;
        }

        timer.reset();

        Robot.getInstance().foundationHook.setPosition(.7);
    }

    @Override
    public void update() {
        double currentAngle = mDrive.gyroAngle.getTheda(AngleUnit.DEGREES);

        if (offsetAxis) currentAngle = Math.signum(currentAngle) > 0 ? currentAngle - 180 : currentAngle + 180;

        double rotation = turnPID.update(currentAngle, angle);

        mDrive.setPower(0, -.2, rotation);
    }

    int angleCounter = 0;
    @Override
    public boolean isFinished() {
        if (Math.abs(turnPID.getError()) < 3) angleCounter++;

        return (Robot.getInstance().foundationDetector.getDistance(DistanceUnit.INCH) <= 2.2 && angleCounter > 3) || timeOut < timer.milliseconds();
    }

    @Override
    public void done() {
        mDrive.setPower(0,0,0);
    }
}
