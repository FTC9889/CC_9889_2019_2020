package com.team9889.ftc2019.subsystems;

import android.util.Log;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.RobotLog;
import com.team9889.ftc2019.Constants;
import com.team9889.lib.CruiseLib;
import com.team9889.lib.RunningAverage;
import com.team9889.lib.control.controllers.PID;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.Set;

/**
 * Created by joshua9889 on 3/28/2018.
 */

public class ScoringLift extends Subsystem{

    PID liftPID = new PID(.3, 0, 0.01);

    private double wantedHeight = 0;
    private boolean goToHeight = false;
    public boolean isDown = false;

    @Override
    public void init(boolean auto) {
        if(auto) {
            GrabberOpen();
            LinearBarIn();
        }
    }

    @Override
    public void outputToTelemetry(Telemetry telemetry) {
        telemetry.addData("Down Limit", isDown);
    }

    @Override
    public void update() {

    }

    public double getLiftHeightInches(){
        double height = Robot.getInstance().rightLift.getPosition() / Constants.LiftConstants.LiftInchToTick;
        Log.e("Height", String.valueOf(height));
        return height;
    }

    public void SetLiftPower(double power){
            Robot.getInstance().leftLift.setPower(power);
            Robot.getInstance().rightLift.setPower(power);
    }

    public void SetLiftHeight(double height){
        wantedHeight = height * Constants.LiftConstants.LiftInchToTick;

        liftPID.update(Robot.getInstance().rightLift.getPosition(), height);
        SetLiftPower(liftPID.getOutput());

        if (CruiseLib.isBetween(getLiftHeightInches(), height - .2, height + .2)){
            SetLiftPower(0);
        }
    }

    public void SetLinearBarPosition(int position){
        Robot.getInstance().linearBar.setPosition(position);
    }

    public void LinearBarIn(){
        SetLinearBarPosition(-1);
    }
    public void LinearBarOut(){
        SetLinearBarPosition(1);
    }

    public void GrabberOpen(){
        Robot.getInstance().grabber.setPosition(1);
    }
    public void GrabberClose(){
        Robot.getInstance().grabber.setPosition(0);
    }

    public boolean isDown() {
        return Robot.getInstance().downLimit.isPressed();
    }
}
