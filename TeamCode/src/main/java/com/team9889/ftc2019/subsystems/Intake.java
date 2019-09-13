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
    // PID for extending the intake
    private PID extenderPID = new PID(0.5, 0.0, 2);

    // Tracker
    private double maximumPosition = 35; // Inches
    private double offset = 0; // Ticks

    public boolean zeroingFirst = true;

    private double zeroingTimerOffset;
    private double transitionTimerOffset;

    private boolean intakeOperatorControl = true;
    public IntakeStates currentIntakeState = IntakeStates.NULL;
    private IntakeStates wantedIntakeState = IntakeStates.ZEROING;
    private double intakeRotatorPosition = 0;

    public double autoIntakeOut;
    private boolean auto;

    private RotatorStates currentIntakeRotatorState;

    public boolean isIntakeOperatorControl() {
        return intakeOperatorControl;
    }

    @Override
    public void init(boolean auto) {
        offset = 0;
        this.auto = auto;
        if (auto){
            zeroSensors();
            setMarkerDumperState(MarkerDumperStates.HOLDING);
            setIntakeRotatorState(RotatorStates.DUMPING);
            setMarkerDumperState(MarkerDumperStates.HOLDING);
            wantedIntakeState = IntakeStates.ZEROING;
            currentIntakeState = IntakeStates.NULL;
        }else {
            wantedIntakeState = IntakeStates.NULL;
            zeroingFirst = true;
        }

    }

    public void zeroSensors() {
        offset = getIntakeExtenderPositionTicks();
    }

    @Override
    public void outputToTelemetry(Telemetry telemetry) {
        telemetry.addData("PID Output", extenderPID.getOutput());

        telemetry.addData("IntakePower", Robot.getInstance().intakeMotor.getVelocity());
        telemetry.addData("Intake Extender Real Position", getIntakeExtenderPosition());
        telemetry.addData("Offset", offset);
        telemetry.addData("Offset is true", !CruiseLib.isBetween(offset, -0.1, 0.1));

        telemetry.addData("Angle of Intake", Robot.getInstance().intakeRotator.getPosition());
        telemetry.addData("Fully In Intake Switch", intakeInSwitchValue());

        telemetry.addData("Wanted State", wantedIntakeState);
        telemetry.addData("Current State", currentIntakeState);

        telemetry.addData("Intake Cruise Control", isIntakeOperatorControl());


        telemetry.addData("Current Rotator State", currentIntakeRotatorState);
    }

    @Override
    public void update() {
        switch (wantedIntakeState) {
            case INTAKING:
                intakeOperatorControl = true;
                setIntakeGateState(IntakeGateStates.DOWN);
                intake();

                if(currentIntakeState != IntakeStates.INTAKING)
                    setIntakeRotatorState(Intake.RotatorStates.DOWN);

                currentIntakeState = IntakeStates.INTAKING;
                break;
            case GRABBING:
                if (zeroingFirst){
                    zeroingTimerOffset = Robot.timer.milliseconds();
                    zeroingFirst = false;
                }

                if (intakeInSwitchValue() || Robot.getInstance().timer.milliseconds() > (2000 + zeroingTimerOffset)){
                    setIntakeExtenderPower(0);
                    intakeOperatorControl = true;
                    transitionTimerOffset = Robot.timer.milliseconds();
                    zeroingFirst = true;
                    setIntakeRotatorState(RotatorStates.DUMPING);
                    currentIntakeState = IntakeStates.GRABBING;
                    wantedIntakeState = IntakeStates.TRANSITION;
                }else{
                    intakeOperatorControl = false;
                    setIntakeRotatorState(RotatorStates.UP);
                    if (auto)
                        setIntakePower(-.25);
                    else
                        setIntakePower(-.5);

                    setIntakeExtenderPower(-1);
                }
                break;

            case ZEROING:
                if (currentIntakeState != wantedIntakeState) {
                    if (zeroingFirst){
                        zeroingTimerOffset = Robot.timer.milliseconds();
                        zeroingFirst = false;
                    }

                    if (intakeInSwitchValue() || Robot.timer.milliseconds() > 2000 + zeroingTimerOffset) {
                        setIntakeExtenderPower(0);
                        intakeOperatorControl = true;
                        zeroingFirst = true;
                        currentIntakeState = IntakeStates.ZEROING;
                    } else {
                        intakeOperatorControl = false;
                        setIntakeRotatorState(RotatorStates.UP);
                        if (auto)
                            setIntakeExtenderPower(-0.5);
                        else
                            setIntakeExtenderPower(-1);
                    }
                }
                break;

            case AUTONOMOUS:
                if (getIntakeExtenderPosition() >= autoIntakeOut){
                    setIntakeExtenderPower(0);
                    currentIntakeState = IntakeStates.AUTONOMOUS;
                }else {
                    setIntakeExtenderPower(1);
                }
                break;

            case NULL:
                currentIntakeState = IntakeStates.NULL;
                intakeOperatorControl = true;
                break;

            case TRANSITION:
                setIntakeGateState(IntakeGateStates.UP);

                if(Robot.timer.milliseconds() > 800 + transitionTimerOffset && Robot.timer.milliseconds() < 1000 + transitionTimerOffset) {
                    intakeOperatorControl = false;
                    setIntakeRotatorState(RotatorStates.UP);
                    setIntakeExtenderPower(1);
                } else if(Robot.timer.milliseconds() > 1000 + transitionTimerOffset && Robot.timer.milliseconds() < 1200 + transitionTimerOffset) {
                    setIntakeExtenderPower(0);
                    setIntakePower(0);
                    setIntakeRotatorState(RotatorStates.UP);
                    currentIntakeState = IntakeStates.TRANSITION;
                    intakeOperatorControl = true;
                }
                break;

            case DRIVER:
                break;
        }

        setIntakeGatePosition();
    }

    /**
     * @param power Power that the Core Hex Motor should go
     */
    public void setIntakePower(double power) {
        Robot.getInstance().intakeMotor.setPower(power);
    }

    /**
     * Turn On Intake
     */
    public void intake() {
        setIntakePower(-1);
        if (auto)
            setIntakeRotatorState(RotatorStates.DOWN);
    }

    /**
     * Outtake
     */
    public void outtake() {
        setIntakePower(1);
    }

    /**
     * @param power Power the extender should go at
     */
    public void setIntakeExtenderPower(double power) {
        if (offset != 0 && (getIntakeExtenderPosition() > maximumPosition && power > 0)
                || (intakeInSwitchValue() && power < 0))
            Robot.getInstance().intakeExtender.setPower(0);
        else
            Robot.getInstance().intakeExtender.setPower(power);
    }

    public double getIntakeExtenderPosition() {
        return (getIntakeExtenderPositionTicks() - offset) * Constants.IntakeConstants.kIntakeTicksToInchRatio;
    }

    private double getIntakeExtenderPositionTicks() {
        return Robot.getInstance().intakeExtender.getPosition();
    }

    /**
     * @param position Position that the intake should go to. In Inches
     */
    public void setIntakeExtenderPosition(double position) {
        position = Range.clip(position, 0, maximumPosition);

        double currentPosition = getIntakeExtenderPosition();
        double power = extenderPID.update(currentPosition, position);

        if (offset == 0.0 && (getIntakeExtenderPosition() > maximumPosition && power > 0) || (intakeInSwitchValue() && power < 0))
            Robot.getInstance().intakeExtender.setPower(0);
        else
            setIntakeExtenderPower(power);
    }

    /**
     * @param wantedState Set the wanted state of the Intake
     */
    public void setWantedIntakeState(IntakeStates wantedState) {
        this.wantedIntakeState = wantedState;
    }

    /**
     * @return If the current state of the intake is equal to the wanted state
     */
    public boolean isCurrentStateWantedState() {
        return (currentIntakeState == wantedIntakeState);
    }

    public IntakeStates getCurrentIntakeState() {
        return currentIntakeState;
    }

    /**
     * @param position Set the position of the Intake
     */
    private void setIntakeRotatorPosition(double position) {
        intakeRotatorPosition = position;
        setIntakeRotatorPosition();
    }

    private void setIntakeRotatorPosition() {
        if (!CruiseLib.isBetween(intakeRotatorPosition, -1, 0.1))
            Robot.getInstance().intakeRotator.setPosition(intakeRotatorPosition);
    }

    public void setIntakeRotatorState(RotatorStates state) {
        switch (state) {
            case UP:
                if (auto)
                    setIntakeRotatorPosition(0.7);
                else
                    setIntakeRotatorPosition(0.7);
                currentIntakeRotatorState = RotatorStates.UP;
                break;

            case DOWN:
                setIntakeRotatorPosition(1);
                currentIntakeRotatorState = RotatorStates.DOWN;
                break;

            case DUMPING:
                setIntakeRotatorPosition(0.5);
                setIntakeGateState(IntakeGateStates.UP);
                currentIntakeRotatorState = RotatorStates.DUMPING;
                break;
        }
    }

    private void setMarkerDumperPosition(double position){
        Robot.getInstance().markerDumper.setPosition(position);
    }

    public void setMarkerDumperState(MarkerDumperStates state){
        switch (state){
            case HOLDING:
                setMarkerDumperPosition(1);
                break;

            case DUMPING:
                setMarkerDumperPosition(.4);
                break;
        }
    }

    private double intakeGatePosition = 0;

    private void setIntakeGatePosition(double position) {
        intakeGatePosition = position;
    }

    private void setIntakeGatePosition() {
        Robot.getInstance().intakeGate.setPosition(intakeGatePosition);
    }

    public void setIntakeGateState(IntakeGateStates state){
        switch (state){
            case UP:
                setIntakeGatePosition(0.4);
                break;

            case DOWN:
                setIntakeGatePosition(0.1);
                break;

            case HOLDINGMARKER:
                setIntakeGatePosition(0.05);
                break;
        }
    }

    /**
     * @return If the ScoringLift is pressing the Lower Limit Switch
     */
    private boolean intakeInSwitchValue() {
        boolean intakeInSwitch = !Robot.getInstance().intakeInSwitch.getState();
        if (intakeInSwitch)
            zeroSensors();
        return intakeInSwitch;
    }

    @Override
    public String toString() {
        return "Intake";
    }

    public enum IntakeStates {
        INTAKING, GRABBING, ZEROING, AUTONOMOUS, NULL, EXTENDED, DRIVER, TRANSITION
    }

    public enum RotatorStates {
        UP, DOWN, DUMPING
    }

    public enum IntakeGateStates {
        UP, DOWN, HOLDINGMARKER
    }

    public enum MarkerDumperStates {
        HOLDING, DUMPING
    }
}