package com.team9889.ftc2019.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.team9889.ftc2019.Constants;
import com.team9889.lib.CruiseLib;
import com.team9889.lib.control.math.cartesian.Pose;
import com.team9889.lib.control.math.cartesian.Rotation2d;
import com.team9889.lib.hardware.RevIMU;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

import static com.team9889.ftc2019.Constants.DriveConstants.ENCODER_TO_DISTANCE_RATIO;

/**
 * Created by Eric on 7/26/2019.
 */

public class Drive extends Subsystem{

    Robot robot = Robot.getInstance();

    @Override
    public void init(boolean auto) {

    }

    @Override
    public void outputToTelemetry(Telemetry telemetry) {

    }

    @Override
    public void update() {

    }

    private void setLeftRightPower(double left, double right){
        robot.fLDrive.setPower(left);
        robot.bLDrive.setPower(left);
        robot.fRDrive.setPower(right);
        robot.bRDrive.setPower(right);
    }

    public void setThrottleSteerPower(double throttle, double steer){
        double left = throttle + steer;
        double right = throttle - steer;
        setLeftRightPower(left, right);
    }
}
