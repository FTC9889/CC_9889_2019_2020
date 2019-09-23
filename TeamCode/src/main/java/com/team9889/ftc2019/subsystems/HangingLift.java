//package com.team9889.ftc2019.subsystems;
//
//import com.qualcomm.robotcore.hardware.CRServo;
//import com.qualcomm.robotcore.hardware.DcMotor;
//import com.qualcomm.robotcore.hardware.DcMotorEx;
//import com.qualcomm.robotcore.hardware.DcMotorSimple;
//import com.qualcomm.robotcore.hardware.DigitalChannel;
//import com.qualcomm.robotcore.hardware.HardwareMap;
//import com.qualcomm.robotcore.util.ElapsedTime;
//import com.team9889.ftc2019.Constants;
//import com.team9889.ftc2019.states.LiftStates;
//import com.team9889.lib.control.controllers.PID;
//
//import org.firstinspires.ftc.robotcore.external.Telemetry;
//
///**
// * Created by Eric on 8/19/2019.
// */
//
//public class HangingLift extends Subsystem {
//
//    private PID pid = new PID(.6, 0, 0.3);
//    public boolean liftOperatorControl = false;
//
//    private double offset = 0;
//
////    private ElapsedTime hookTimer = new ElapsedTime();
//    private double timerOffset;
//
//    private LiftStates currentState = LiftStates.NULL;
//    private LiftStates wantedState = LiftStates.NULL;
//
//    @Override
//    public void init(boolean auto) {
//        offset = 0;
//
//        currentState = LiftStates.NULL;
//        wantedState = LiftStates.NULL;
//
//        if (auto) {
//            timerOffset = Robot.timer.milliseconds();
//            setLiftState(LiftStates.HANGING);
//        }
//    }
//
//    public void zeroSensors() {
//        offset = getHeightTicks();
//    }
//
//    @Override
//    public void outputToTelemetry(Telemetry telemetry) {
//        telemetry.addData("Height of lift", getHeight());
//        telemetry.addData("Height of lift in ticks", getHeightTicks());
//        telemetry.addData("Lift PID Output", pid.getOutput());
//
//        telemetry.addData("Lower limit pressed", getLowerLimitPressed());
//
//        telemetry.addData("Is Lift in position", isCurrentWantedState());
//
//        telemetry.addData("Wanted State", wantedState);
//        telemetry.addData("Current State", currentState);
//    }
//
//    @Override
//    public void update() {
//        getLowerLimitPressed();
//
//        if (currentState != wantedState) {
//            liftOperatorControl = false;
//
//            switch (wantedState) {
//                case DOWN:
//                    if (getLowerLimitPressed()) {
//                        setLiftPower(0);
//                        currentState = LiftStates.DOWN;
//                    } else {
//                        setLiftPower(-0.7);
//                    }
//                    break;
//
//                case HOOKHEIGHT:
//                    setLiftPosition(3200);
//
//                    if (inPosition() || getHeight() > 3200) {
//                        setLiftPower(0);
//                        currentState = LiftStates.HOOKHEIGHT;
//                    }
//                    break;
//
//                case HANGING:
//                    if (getLowerLimitPressed()) {
//                        setLiftPower(-0.2);
//                        zeroSensors();
//                    } else {
//                        setLiftPower(-.7);
//                    }
//                    if (Robot.timer.milliseconds() < (1000 + timerOffset)){
//                        setHookPower(-.5);
//                    }
//
//                    // Never Set the currentState to Hanging in order to make it still run during init
//                    break;
//
//                case NULL:
//                    setLiftPower(0);
//                    currentState = LiftStates.NULL;
//                    break;
//            }
//        } else {
//            liftOperatorControl = true;
//        }
//    }
//
//    /**
//     * @param power Power to the motors
//     */
//    public void setLiftPower(double power) {
//        double mPower = power;
//        if((getLowerLimitPressed() && mPower<0))
//            mPower = 0;
//
//        Robot.getInstance().hangingLiftMotor.setPower(mPower);
//    }
//
//    /**
//     * @param wantedHeight In inches
//     */
//    private void setLiftPosition(double wantedHeight) {
//        setLiftPower(pid.update(getHeight(), wantedHeight));
//    }
//
//    public void setHookPower(double power){
//        Robot.getInstance().hangingHook.setPower(power);
//    }
//
//    private double getHeightTicks() {
//        return Robot.getInstance().hangingLiftMotor.getPosition();
//    }
//
//    public double getHeight() {
//        return (getHeightTicks() - offset);
//    }
//
//    private boolean inPosition() {
//        return Math.abs(pid.getError()) < 0.5;
//    }
//
//    public boolean getLowerLimitPressed() {
//        boolean lowerLimitPressed = !Robot.getInstance().hangingLowerLimit.getState();
//        if (lowerLimitPressed)
//            zeroSensors();
//
//        return lowerLimitPressed;
//    }
//
//    public void setLiftState(LiftStates state) {
//        this.wantedState = state;
//    }
//
//    public boolean isCurrentWantedState() {
//        return currentState == wantedState;
//    }
//
//    public LiftStates getWantedState() {
//        return wantedState;
//    }
//
//    public LiftStates getCurrentState() {
//        return currentState;
//    }
//
//    @Override
//    public String toString() {
//        return "Lift";
//    }
//}