package com.team9889.ftc2019.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import com.team9889.ftc2019.Constants;
import com.team9889.lib.CruiseLib;
import com.team9889.lib.control.controllers.PID;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.internal.usb.exception.RobotUsbTimeoutException;

/**
 * Created by Eric on 8/19/2019.
 */

public class Intake extends Subsystem {

    @Override
    public void init(boolean auto) {

    }

    @Override
    public void outputToTelemetry(Telemetry telemetry) {

    }

    @Override
    public void update() {

    }

    public void setIntakePower(double power){
        Robot.getInstance().intakeLeft.setPower(power);
        Robot.getInstance().intakeRight.setPower(power);
    }
    public void Intake(){
        setIntakePower(0.75);
    }
    public void Outtake(){
        setIntakePower(-0.3);
    }
    public void Stop(){
        setIntakePower(0);
    }

    public void capUp(){
        Robot.getInstance().capServo.setPosition(0.8);
    }
    public void capDump(){
        Robot.getInstance().capServo.setPosition(0.2);
    }
}