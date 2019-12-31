package com.team9889.ftc2019;

import com.qualcomm.robotcore.hardware.Gamepad;

/**
 * Created by MannoMation on 12/14/2018.
 */
public class DriverStation {
    private Gamepad gamepad1;
    private Gamepad gamepad2;

    public DriverStation(Gamepad gamepad1, Gamepad gamepad2){
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;
    }

    double getX(){
        return gamepad1.left_stick_x;
    }

    double getY() {
        return -gamepad1.left_stick_y;
    }

    double getSteer(){
        return gamepad1.right_stick_x;
    }

    double getLiftPower(boolean isDown) {
        double g1UpPower = gamepad1.right_trigger;
        double g2UpPower = gamepad2.right_trigger;
        double g1DownPower = gamepad1.left_trigger;
        double g2DownPower = gamepad2.left_trigger;

        if(g1UpPower > 0.1)
            return -g1UpPower;
        else if(g1DownPower > 0.1 && !isDown)
            return g1DownPower;
        else if(g2UpPower > 0.1)
            return -g2UpPower;
        else if(g2DownPower > 0.1 && !isDown)
            return g2DownPower;
        else
            return -0.2;
    }

    boolean getReleaseStone(){
        return gamepad2.right_bumper || gamepad1.right_bumper;
    }

    boolean getStartIntaking(){
        return gamepad1.a;
    }

    boolean getStopIntaking() {
        return gamepad1.b;
    }

    boolean getStartOuttaking() {
        return gamepad1.y;
    }

    private boolean intakeToggle = true;
    private boolean intakeDown = true;
    boolean getIntake() {
        if(gamepad1.right_bumper && intakeToggle) {
            intakeDown = !intakeDown;
            intakeToggle = false;
        } else if(!gamepad1.right_bumper)
            intakeToggle = true;

        return intakeDown;
    }

    private boolean slowDownToggle = true;
    private boolean slowDown = false;
    double getSlowDownFactor() {
        if(gamepad1.x && slowDownToggle) {
            slowDown = !slowDown;
            slowDownToggle = false;
        } else if(!gamepad1.x)
            slowDownToggle = true;

        return slowDown ? 3 : 1;
    }

    private boolean foundationToggle = false;
    private boolean foundationClose = false;
    boolean getFoundationClose() {
        if(gamepad2.dpad_up && foundationToggle) {
            foundationClose = !foundationClose;
            foundationToggle = false;
        } else if(!gamepad2.dpad_up)
            foundationToggle = true;

        return foundationClose;
    }

    private boolean grabberToggle = true;
    private boolean grabberOpen = true;
    boolean getGrabberOpen(boolean override) {
        if(gamepad2.y && grabberToggle) {
            grabberOpen = !grabberOpen;
            grabberToggle = false;
        } else if(!gamepad2.y)
            grabberToggle = true;

        if(gamepad1.dpad_right)
            grabberOpen = false;

        if(!override)
            grabberOpen = true;

        return grabberOpen;
    }

    private boolean linearBarIn = true;
    boolean getLinearBarIn(boolean override) {
        if(gamepad2.right_bumper)
            linearBarIn = true;
        else if(gamepad2.left_bumper)
            linearBarIn = false;

        if(!override)
            linearBarIn = true;

        return linearBarIn;
    }

    boolean scoreStone() {
        return gamepad1.dpad_left;
    }

    boolean releaseTapeMeasure() {
        return gamepad2.dpad_down;
    }
}
