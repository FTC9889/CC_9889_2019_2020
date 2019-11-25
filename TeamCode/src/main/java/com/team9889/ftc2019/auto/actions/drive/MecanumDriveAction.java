package com.team9889.ftc2019.auto.actions.drive;

import com.qualcomm.robotcore.util.ElapsedTime;
import com.team9889.ftc2019.Constants;
import com.team9889.ftc2019.auto.actions.Action;
import com.team9889.ftc2019.subsystems.MecanumDrive;
import com.team9889.ftc2019.subsystems.Robot;
import com.team9889.lib.CruiseLib;
import com.team9889.lib.control.controllers.cruiseController;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

/**
 * Created by Eric on 9/9/2019.
 */
public class MecanumDriveAction extends Action {

    private double xPosition, yPosition, gyro;
    private int angle, counter, time;
    private cruiseController cc;
    private boolean ignoreAngle;
    double angleSpeed = 0;
    private double minSpeed = 0.1;

    private ElapsedTime timer = new ElapsedTime();

    public MecanumDriveAction(double xPosition, double yPosition, int angle, int time){
        this.xPosition = xPosition * Constants.DriveConstants.InchToTick;
        this.yPosition = yPosition * Constants.DriveConstants.InchToTick;
        this.angle = angle;
        this.time = time;
    }
    public MecanumDriveAction(double xPosition, double yPosition, int angle, int time, boolean ignoreAngle){
        this.xPosition = xPosition * Constants.DriveConstants.InchToTick;
        this.yPosition = yPosition * Constants.DriveConstants.InchToTick;
        this.angle = angle;
        this.time = time;
        this.ignoreAngle = ignoreAngle;
    }
    public MecanumDriveAction(double xPosition, double yPosition, int angle, int time, boolean ignoreAngle, double minSpeed){
        this.xPosition = xPosition * Constants.DriveConstants.InchToTick;
        this.yPosition = yPosition * Constants.DriveConstants.InchToTick;
        this.angle = angle;
        this.time = time;
        this.ignoreAngle = ignoreAngle;
        this.minSpeed = minSpeed;
    }

    @Override
    public void setup(String args) {

    }

    @Override
    public void start() {
        Robot.getInstance().update();

        if (yPosition == 0){
            cc = new cruiseController(.08, .2);

        }else if(xPosition != 0 && yPosition != 0) {
            cc = new cruiseController(.08, .2);
        }else {
            cc = new cruiseController(.05, minSpeed);
        }

        Robot.getInstance().getMecanumDrive().setPosition(xPosition, yPosition, 0);

        Robot.getInstance().fLDrive.resetEncoder();
        Robot.getInstance().fRDrive.resetEncoder();
        Robot.getInstance().bLDrive.resetEncoder();
        Robot.getInstance().bRDrive.resetEncoder();

        gyro = Robot.getInstance().getMecanumDrive().getAngle().getTheda(AngleUnit.DEGREES);

        timer.reset();
    }

    @Override
    public void update() {
        double fLPower = cc.update(Robot.getInstance().fLDrive.getPosition(), Robot.getInstance().getMecanumDrive().frontLeft);
        double fRPower = cc.update(Robot.getInstance().fRDrive.getPosition(), Robot.getInstance().getMecanumDrive().frontRight);
        double bLPower = cc.update(Robot.getInstance().bLDrive.getPosition(), Robot.getInstance().getMecanumDrive().backLeft);
        double bRPower = cc.update(Robot.getInstance().bRDrive.getPosition(), Robot.getInstance().getMecanumDrive().backRight);

        if (counter > 5) {
            gyro = Robot.getInstance().getMecanumDrive().getAngle().getTheda(AngleUnit.DEGREES);
            counter = 0;
        }else {
            counter++;
        }

        angleSpeed = Robot.getInstance().getMecanumDrive().getSpeed(0, 0, (gyro - angle) / 100);

        Robot.getInstance().update();

        if (!ignoreAngle) {
            Robot.getInstance().fLDrive.setPower(fLPower - angleSpeed);
            Robot.getInstance().fRDrive.setPower(fRPower + angleSpeed);
            Robot.getInstance().bLDrive.setPower(bLPower - angleSpeed);
            Robot.getInstance().bRDrive.setPower(bRPower + angleSpeed);
        }else {
            Robot.getInstance().fLDrive.setPower(fLPower);
            Robot.getInstance().fRDrive.setPower(fRPower);
            Robot.getInstance().bLDrive.setPower(bLPower);
            Robot.getInstance().bRDrive.setPower(bRPower);
        }
    }

    @Override
    public boolean isFinished() {
        return CruiseLib.isBetween(Robot.getInstance().fLDrive.getPosition(),
                Robot.getInstance().getMecanumDrive().frontLeft - 10, Robot.getInstance().getMecanumDrive().frontLeft + 10)
                && CruiseLib.isBetween(Robot.getInstance().fRDrive.getPosition(),
                Robot.getInstance().getMecanumDrive().frontRight - 10, Robot.getInstance().getMecanumDrive().frontRight + 10)
                && CruiseLib.isBetween(Robot.getInstance().bLDrive.getPosition(),
                Robot.getInstance().getMecanumDrive().backLeft - 10, Robot.getInstance().getMecanumDrive().backLeft + 10)
                && CruiseLib.isBetween(Robot.getInstance().bRDrive.getPosition(),
                Robot.getInstance().getMecanumDrive().backRight - 10, Robot.getInstance().getMecanumDrive().backRight + 10) || timer.milliseconds() > time;
    }

    @Override
    public void done() {
        Robot.getInstance().fLDrive.setPower(0);
        Robot.getInstance().fRDrive.setPower(0);
        Robot.getInstance().bLDrive.setPower(0);
        Robot.getInstance().bRDrive.setPower(0);
    }
}
