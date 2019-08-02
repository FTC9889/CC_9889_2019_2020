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

    @Override
    public void init(boolean auto) {

    }

    @Override
    public void outputToTelemetry(Telemetry telemetry) {

    }

    @Override
    public void update() {

    }

    public static void setLeftRightPower(double left, double right) {
        Robot.getInstance().fLDrive.setPower(CruiseLib.limitValue(left, 1, -1));
        Robot.getInstance().bLDrive.setPower(CruiseLib.limitValue(left, 1, -1));
        Robot.getInstance().fRDrive.setPower(CruiseLib.limitValue(right, 1, -1));
        Robot.getInstance().bRDrive.setPower(CruiseLib.limitValue(right, 1, -1));
    }

    public static void setThrottleSteerPower(double throttle, double turn){
        double left = throttle + turn;
        double right = throttle - turn;
        setLeftRightPower(left, right);
    }
}
