package com.team9889.lib.hardware;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Hardware;
import com.team9889.ftc2019.subsystems.Robot;

import org.openftc.revextensions2.ExpansionHubMotor;
import org.openftc.revextensions2.RevBulkData;

/**
 * Created by Eric on 7/26/2019.
 */

public class Motor {
    public ExpansionHubMotor motor;
    private int position;
    private double currentPower = 0;
    private double velocity;
    private double ratio;
    public static int numHardwareUsesThisUpdate = 0;

    public Motor(HardwareMap hardwareMap, String id, double ratio){
        this.motor = (ExpansionHubMotor) hardwareMap.dcMotor.get(id);
        this.ratio = ratio;
        numHardwareUsesThisUpdate ++;
    }

    public Motor(HardwareMap hardwareMap, String id) {
        this.motor = (ExpansionHubMotor) hardwareMap.dcMotor.get(id);
        this.ratio = 1;
        numHardwareUsesThisUpdate ++;
    }

    public void setPower(double power){
        if (Math.abs(power - currentPower) > .005) {
            motor.setPower(power);
            currentPower = power;
            numHardwareUsesThisUpdate ++;
        }
    }

    public int getPosition(){
        return position;
    }

    public double getVelocity(){
        return velocity;
    }

    public void update(RevBulkData bulkData){
        position = bulkData.getMotorCurrentPosition(motor);
        velocity = bulkData.getMotorVelocity(motor);
    }
}