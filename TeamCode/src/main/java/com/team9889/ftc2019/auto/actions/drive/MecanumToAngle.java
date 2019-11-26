package com.team9889.ftc2019.auto.actions.drive;

import com.qualcomm.robotcore.util.ElapsedTime;
import com.team9889.ftc2019.auto.actions.Action;
import com.team9889.ftc2019.subsystems.Robot;
import com.team9889.lib.CruiseLib;
import com.team9889.lib.control.controllers.cruiseController;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

/**
 * Created by Eric on 11/18/2019.
 */
public class MecanumToAngle extends Action {
    private int angle, timeOutInt;
    private double gyro, lastGyro;
    private cruiseController cc;
    private ElapsedTime timer = new ElapsedTime();
    private ElapsedTime timeOut = new ElapsedTime();

    public MecanumToAngle(int angle, int timeOut){
        this.angle = angle;
        timeOutInt = timeOut;
    }

    @Override
    public void setup(String args) {

    }

    @Override
    public void start() {
        cc = new cruiseController(.07, .12);
        timer.reset();
        gyro = Robot.getInstance().getMecanumDrive().getAngle().getTheda(AngleUnit.DEGREES);
        lastGyro = gyro;
        timeOut.reset();
    }

    @Override
    public void update() {
        gyro = Robot.getInstance().getMecanumDrive().getAngle().getTheda(AngleUnit.DEGREES);

        double tuner = cc.update(gyro - lastGyro, angle);

        Robot.getInstance().getMecanumDrive().setPosition(0, 0, tuner);

        Robot.getInstance().fLDrive.setPower(Robot.getInstance().getMecanumDrive().frontLeft);
        Robot.getInstance().fRDrive.setPower(Robot.getInstance().getMecanumDrive().frontRight);
        Robot.getInstance().bLDrive.setPower(Robot.getInstance().getMecanumDrive().backLeft);
        Robot.getInstance().bRDrive.setPower(Robot.getInstance().getMecanumDrive().backRight);

        if (!CruiseLib.isBetween(gyro - lastGyro, angle - 1, angle + 1)){
            timer.reset();
        }else {
            Robot.getInstance().fLDrive.setPower(0);
            Robot.getInstance().fRDrive.setPower(0);
            Robot.getInstance().bLDrive.setPower(0);
            Robot.getInstance().bRDrive.setPower(0);
        }
    }

    @Override
    public boolean isFinished() {
        return CruiseLib.isBetween(gyro - lastGyro, angle - 1, angle + 1) && timer.milliseconds() > 500 || timeOut.milliseconds() > timeOutInt;
    }

    @Override
    public void done() {
        Robot.getInstance().fLDrive.setPower(0);
        Robot.getInstance().fRDrive.setPower(0);
        Robot.getInstance().bLDrive.setPower(0);
        Robot.getInstance().bRDrive.setPower(0);
    }
}
