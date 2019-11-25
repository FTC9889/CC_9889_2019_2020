package com.team9889.ftc2019.auto.actions.drive;

import com.team9889.ftc2019.Constants;
import com.team9889.ftc2019.auto.actions.Action;
import com.team9889.ftc2019.subsystems.Robot;
import com.team9889.lib.control.controllers.cruiseController;

import org.firstinspires.ftc.robotcore.internal.usb.exception.RobotUsbTimeoutException;
import org.opencv.core.Mat;

/**
 * Created by Eric on 11/1/2019.
 */
public class MecanumToPosition extends Action {

    private double x, y, negative;
    private int angle;

    private cruiseController cc;

    public MecanumToPosition(double x, double y, int angle){
        this.x = x * Constants.DriveConstants.InchToTick;
        this.y = y * Constants.DriveConstants.InchToTick;
        this.angle = angle;
    }

    @Override
    public void setup(String args) {

    }

    @Override
    public void start() {
        Robot.getInstance().getMecanumDrive().frontLeftInt = 1;
        Robot.getInstance().getMecanumDrive().backLeftInt = 1;
        Robot.getInstance().getMecanumDrive().frontRightInt = 1;
        Robot.getInstance().getMecanumDrive().backRightInt = 1;

        cc = new cruiseController(.03, .25);

        Robot.getInstance().getMecanumDrive().setPosition(x, y, angle);

        Robot.getInstance().fLDrive.resetEncoder();
        Robot.getInstance().fRDrive.resetEncoder();
        Robot.getInstance().bLDrive.resetEncoder();
        Robot.getInstance().bRDrive.resetEncoder();

        if (x < y && x != 0){
            negative = x / Math.abs(x);
        }
        else if (x > y && y != 0){
            negative = y / Math.abs(y);
        }
        else{
            negative = 1;
        }
    }

    @Override
    public void update() {
        double controllerData = cc.update((Math.abs(Robot.getInstance().fLDrive.getPosition()) +
                        Math.abs(Robot.getInstance().bLDrive.getPosition()) + Math.abs(Robot.getInstance().fRDrive.getPosition()) +
                        Math.abs(Robot.getInstance().bRDrive.getPosition())) / 4, (Math.abs(Robot.getInstance().getMecanumDrive().frontLeft) +
                        Math.abs(Robot.getInstance().getMecanumDrive().backLeft) + Math.abs(Robot.getInstance().getMecanumDrive().frontRight) +
                        Math.abs(Robot.getInstance().getMecanumDrive().backRight)) / 4);

        Robot.getInstance().update();

        //Robot.getInstance().getMecanumDrive().setAutoPower(Robot.getInstance().getMecanumDrive().xAutoPower, Robot.getInstance().getMecanumDrive().yAutoPower, 0, controllerData, negative);

        if (Math.abs(Robot.getInstance().fLDrive.getPosition()) >= Math.abs(Robot.getInstance().getMecanumDrive().frontLeft)){
            Robot.getInstance().getMecanumDrive().frontLeftInt = 0;
        }
        if (Math.abs(Robot.getInstance().fRDrive.getPosition()) >= Math.abs(Robot.getInstance().getMecanumDrive().frontRight)){
            Robot.getInstance().getMecanumDrive().frontRightInt = 0;
        }
        if (Math.abs(Robot.getInstance().bLDrive.getPosition()) >= Math.abs(Robot.getInstance().getMecanumDrive().backLeft)){
            Robot.getInstance().getMecanumDrive().backLeftInt = 0;
        }
        if (Robot.getInstance().bRDrive.getPosition() >= Math.abs(Robot.getInstance().getMecanumDrive().backRight)){
            Robot.getInstance().getMecanumDrive().backRightInt = 0;
        }
    }

    @Override
    public boolean isFinished() {
        return Math.abs(Robot.getInstance().fLDrive.getPosition()) >= Math.abs(Robot.getInstance().getMecanumDrive().frontLeft) &&
                Math.abs(Robot.getInstance().fRDrive.getPosition()) >= Math.abs(Robot.getInstance().getMecanumDrive().frontRight) &&
                Math.abs(Robot.getInstance().bLDrive.getPosition()) >= Math.abs(Robot.getInstance().getMecanumDrive().backLeft) &&
                Math.abs(Robot.getInstance().bRDrive.getPosition()) >= Math.abs(Robot.getInstance().getMecanumDrive().backRight);
    }

    @Override
    public void done() {
        Robot.getInstance().getMecanumDrive().setPower(0,0,0);
    }
}
