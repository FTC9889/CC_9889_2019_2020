package com.team9889.ftc2019;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.team9889.ftc2019.subsystems.Robot;

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

    private boolean timeOverride = false;

    private boolean autoCapFirst = true, done = false;
    private ElapsedTime autoCapTime = new ElapsedTime();

    @Override
    public void runOpMode() {
        DriverStation driverStation = new DriverStation(gamepad1, gamepad2);
        waitForStart(false);

        Robot.odometryLifter.setPosition(0.5);
        Robot.teamMarkerDeployServo.setPosition(1);

        Robot.getLift().GrabberOpen();
        Robot.getMecanumDrive().OpenFoundationHook();

        Robot.getLift().LinearBarIn();

        while (opModeIsActive()) {
            // dt timer
            loopTimer.reset();

            // If not resetting imu, normal operation
            if(!driverStation.resetIMU()) {
                // Drive

                if(Math.abs(driverStation.getX()) < 0.01 && Math.abs(driverStation.getY()) < 0.01 &&
                        Math.abs(driverStation.getSteer()) < 0.01 && gamepad1.start) {
                    // if y<20 -> slower foward
                    // if y>35 -> half speed
                    // if y>50 -> full

                    // if x > 20 -> left
                    // if x near 20 -> center
                    // if x < 20 -> right

                    double forward = 0;
                    double turn = 0;
                    if (pipeline.getMinPoint().x == 40 && pipeline.getMinPoint().y == 60){
                        forward = .5;
                    } else if(pipeline.getMinPoint().y > 25)
                        forward = 1;
                    else if(pipeline.getMinPoint().y < 25)
                        forward = 0.5;

                    double center = 38.5;
                    if (pipeline.getMinPoint().x == 40 && pipeline.getMinPoint().y == 60){

                    } else if (pipeline.getMinPoint().x > center + 5 && pipeline.getMinPoint().x < center + 20)
                        turn = -0.2;
                    else if (pipeline.getMinPoint().x < center - 5 && pipeline.getMinPoint().x > center - 20)
                        turn = 0.2;
                    else if (pipeline.getMinPoint().x <= center - 20)
                        turn = .4;
                    else if (pipeline.getMinPoint().x >= center + 20){
                        turn = -.4;
                    }
                    else
                        turn = 0;

                    telemetry.addData("turn", turn);

                    Robot.getMecanumDrive().setPower(0, forward, turn);
                } else {
                    double slowDownFactor = driverStation.getSlowDownFactor();
                    Robot.getMecanumDrive().setFieldCentricPower(
                            driverStation.getX() / slowDownFactor,
                            driverStation.getY() / slowDownFactor,
                            driverStation.getSteer() / slowDownFactor);
                }

                // CapStone
                if ((driverStation.capStoneAuto(true, done) || done) && (matchTime.seconds() > 120 - 30 || timeOverride)){
                    if (autoCapFirst){
                        autoCapFirst = false;
                        autoCapTime.reset();
                    }else if (autoCapTime.milliseconds() < 300){
                        Robot.getLift().GrabberOpen();
                    }else if (autoCapTime.milliseconds() >= 300 && autoCapTime.milliseconds() < 500){
                        Robot.getLift().SetLiftPower(-1);
                    }else if (autoCapTime.milliseconds() >= 500 && autoCapTime.milliseconds() < 600){
                        Robot.getLift().SetLiftPower(0);
                        Robot.getLift().LinearBarIn();
                        Robot.teamMarkerDeployServo.setPosition(-1);
                        done = true;
                    }else if (autoCapTime.milliseconds() >= 600){
                        done = false;
                        autoCapFirst = true;
                    }
                }else if (!automateScoring && !automateCapStone) {
                    Robot.getLift().SetLiftPower(driverStation.getLiftPower(Robot.downLimit.isPressed()));

                    if (driverStation.getLinearBarIn(first))
                        Robot.getLift().LinearBarIn();
                    else
                        Robot.getLift().LinearBarOut();

                    if (Robot.downLimit.isPressed() && driverStation.getGrabberOpen(first)){
                        Robot.grabber.setPosition(1);
                    }else if (driverStation.getGrabberOpen(first)) {
                        Robot.getLift().GrabberOpen();
                    } else {
                        Robot.getLift().GrabberClose();
                    }

                    automateScoring = driverStation.scoreStone() && !driverStation.getLinearBarIn(true);
                    automatedScoringTimer.reset();

                    first = true;
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

                if (gamepad2.right_stick_button && gamepad2.left_stick_button){
                    timeOverride = true;
                }

                if ((matchTime.seconds() > 120 - 30 || timeOverride) && driverStation.releaseTapeMeasure())
                    Robot.tapeMeasureDeploy.setPosition(0.5);
                else if (matchTime.seconds() > 120 - 30 || timeOverride) {
                    if (driverStation.capStone(true)) {
                        Robot.teamMarkerDeployServo.setPosition(-1);
                    } else {
                        Robot.teamMarkerDeployServo.setPosition(1);
                    }
                }

                // Intake Servos

                if (driverStation.getIntakeFlip())
                    Robot.getIntake().IntakeFlip();
                else if (driverStation.getIntake())
                    Robot.getIntake().IntakeDown();
                else
                    Robot.getIntake().IntakeUp();

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
            telemetry.addData("Slow Drive", driveSlow);
            telemetry.addData("Gyro After Auto", Robot.getMecanumDrive().angleFromAuton);
            telemetry.addData("left lift height", -Robot.leftLift.getPosition());
            telemetry.addData("right lift height", -Robot.rightLift.getPosition());
            telemetry.addData("left intake", -Robot.intakeLeft.getPosition());
            telemetry.addData("right intake", -Robot.intakeRight.getPosition());

            telemetry.addData("Magnet", Robot.downLimit.isPressed());

            Robot.outputToTelemetry(telemetry);

            telemetry.update();

            Robot.update();
        }

        Runnable ShutDownCameraThread = new Runnable() {
            @Override
            public void run() {
                phoneCam.stopStreaming();
            }
        };

        new Thread(ShutDownCameraThread).start();
    }
}
