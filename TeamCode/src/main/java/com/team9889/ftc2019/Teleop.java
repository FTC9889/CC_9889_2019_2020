package com.team9889.ftc2019;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by MannoMation on 1/14/2019.
 */

@TeleOp(name = "Teleop")
public class Teleop extends Team9889Linear {

    private ElapsedTime loopTimer = new ElapsedTime();
    private boolean driveSlow = false;
    private boolean first = true;
    private boolean liftDownLimit = false;

    private boolean intaking = false;
    private boolean blockDetectorFirst = true;

    //  lift automation
    private boolean automateScoring = false;
    private ElapsedTime automatedScoringTimer = new ElapsedTime();

    private boolean automateCapStone = false;
    private ElapsedTime automatedCapStoneTimer = new ElapsedTime();
    private boolean stoneWasThere = false;

    @Override
    public void runOpMode() {
        DriverStation driverStation = new DriverStation(gamepad1, gamepad2);
        waitForStart(false);

        Robot.odometryLifter.setPosition(0.5);

        Robot.getLift().GrabberOpen();
        Robot.getMecanumDrive().OpenFoundationHook();

        Robot.getLift().LinearBarIn();

        while (opModeIsActive()) {
            // dt timer
            loopTimer.reset();

            // If not resetting imu, normal operation
            if(!driverStation.resetIMU()) {
                // Drive
                double slowDownFactor = driverStation.getSlowDownFactor();
                Robot.getMecanumDrive().setFieldCentricPower(
                        driverStation.getX() / slowDownFactor,
                        driverStation.getY() / slowDownFactor,
                        driverStation.getSteer() / slowDownFactor);

                if (!automateScoring && !automateCapStone) {
                    Robot.getLift().SetLiftPower(driverStation.getLiftPower(liftDownLimit));

                    if (driverStation.getLinearBarIn(first))
                        Robot.getLift().LinearBarIn();
                    else
                        Robot.getLift().LinearBarOut();

                    if (driverStation.getStartIntaking()) {
                        Robot.getIntake().Intake();
                        Robot.getLift().GrabberOpen();
                        intaking = true;
                    } else if (driverStation.getStopIntaking()) {
                        Robot.getIntake().Stop();
                        intaking = false;
                    } else if (driverStation.getStartOuttaking()) {
                        Robot.getIntake().Outtake();
                        intaking = false;
                    }

                    if (driverStation.getGrabberOpen(first)) {
                        Robot.getLift().GrabberOpen();
                    } else {
                        Robot.getLift().GrabberClose();
                    }

                    automateScoring = driverStation.scoreStone() && !driverStation.getLinearBarIn(true);
                    automatedScoringTimer.reset();

                    automateCapStone = driverStation.capStone();
                    automatedCapStoneTimer.reset();
                    first = true;
                } else if (automateCapStone) {
                    if (Robot.blockDetector.getDistance(DistanceUnit.INCH) < 4) {
                        if (automatedCapStoneTimer.milliseconds() < 200) {
                            stoneWasThere = true;
                            Robot.getLift().GrabberOpen();
                            Robot.getIntake().Stop();
                            Robot.getLift().SetLiftPower(-1);
                        } else if (automatedCapStoneTimer.milliseconds() > 199 && automatedCapStoneTimer.milliseconds() < 3500) {
                            Robot.getLift().SetLiftPower(0);
                            Robot.getIntake().DeployCapStone();
                        } else if (automatedCapStoneTimer.milliseconds() > 3499 && automatedCapStoneTimer.milliseconds() < 3700) {
                            Robot.getLift().SetLiftPower(1);
                            Robot.getIntake().StopCapStone();
                        } else if (automatedCapStoneTimer.milliseconds() > 3699 && automatedCapStoneTimer.milliseconds() < 4000
                                || automatedCapStoneTimer.milliseconds() > 3499 && Robot.downLimit.isPressed()) {
                            Robot.getLift().GrabberClose();
                            Robot.getLift().SetLiftPower(0);
                            automateCapStone = false;
                            stoneWasThere = false;
                        }

                    }
                } else {
                    if (automatedScoringTimer.milliseconds() < 500) {
                        Robot.getLift().GrabberOpen();
                    } else if (automatedScoringTimer.milliseconds() < 750 && automatedScoringTimer.milliseconds() > 250) {
                        Robot.getLift().SetLiftPower(-0.7);
                    } else if (automatedScoringTimer.milliseconds() < 1250 + 200 && automatedScoringTimer.milliseconds() > 750) {
                        Robot.getLift().SetLiftPower(0);
                        Robot.getLift().LinearBarIn();
                    } else if (!Robot.getLift().isDown() && (automatedScoringTimer.milliseconds() > 1250 + 200 && automatedScoringTimer.milliseconds() < 2000 + 700)) {
                        Robot.getLift().GrabberClose();
                        Robot.getLift().SetLiftPower(1);
                    } else {
                        Robot.getLift().SetLiftPower(0);
                        automateScoring = false;
                    }
                    first = false;
                }

                if (matchTime.seconds() > 120 - 30 && driverStation.releaseTapeMeasure())
                    Robot.tapeMeasureDeploy.setPosition(0.5);

                // Intake Servos
                if (driverStation.getIntake())
                    Robot.getIntake().IntakeDown();
                else
                    Robot.getIntake().IntakeUp();

                // Foundation Hook
                if (driverStation.getFoundationClose())
                    Robot.getMecanumDrive().CloseFoundationHook();
                else
                    Robot.getMecanumDrive().OpenFoundationHook();

                if (intaking && blockDetectorFirst) {
                    if (Robot.blockDetector.getDistance(DistanceUnit.INCH) < 4) {
                        Robot.getIntake().Stop();
                        Robot.getLift().GrabberClose();
                        blockDetectorFirst = false;
                    }
                } else if (!blockDetectorFirst) {
                    if (Robot.blockDetector.getDistance(DistanceUnit.INCH) > 4) {
                        blockDetectorFirst = true;
                    }
                }
            } else {
                Robot.getMecanumDrive().setPower(0,0,0);
                Robot.getMecanumDrive().writeAngleToFile();
                Robot.getMecanumDrive().readAngleFromFile();
            }

            telemetry.addData("Loop Time", loopTimer.milliseconds());
            telemetry.addData("angle", Robot.getMecanumDrive().getAngle().getTheda(AngleUnit.DEGREES));
            telemetry.addData("Slow Drive", driveSlow);

            telemetry.addData("Gyro After Auto", Robot.getMecanumDrive().angleFromAuton);

            telemetry.addData("left lift height", -Robot.leftLift.getPosition());
            telemetry.addData("right lift height", -Robot.rightLift.getPosition());
            telemetry.addData("left intake", -Robot.intakeLeft.getPosition());
            telemetry.addData("right intake", -Robot.intakeRight.getPosition());

            telemetry.addData("Magnet", Robot.downLimit.isPressed());

            telemetry.update();

            Robot.update();
        }
    }
}
