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
    private ElapsedTime xButtonTimer = new ElapsedTime();
    private ElapsedTime linearBarTimer = new ElapsedTime();
    private ElapsedTime liftTimer = new ElapsedTime();
    private boolean driveSlow = false;
    private boolean first = true;
    private boolean linearBar = false;
    private boolean lift = false;
    private boolean grabberOpen = true;
    private boolean liftDownLimit;
    private boolean liftGoingDown;

    private boolean intaking = false;
    private boolean liftMoving = false;
    private boolean blockDetectorFirst = true;
    private boolean liftFirst = true;
    private boolean linearBarIn = false;

//  gyro file
    private double readAngle;
    String line;
    String fileName = "gyro.txt";
    java.io.FileReader angleFileReader;
    BufferedReader angleBufferedReader;

    //  lift automation
    private int numSkyStones = 0;

    boolean automateScoring = false;
    ElapsedTime automatedScoringTimer = new ElapsedTime();


    @Override
    public void runOpMode() {
        DriverStation driverStation = new DriverStation(gamepad1, gamepad2);
        waitForStart(false);

        Robot.odometryLifter.setPosition(0.5);

        Robot.getLift().GrabberOpen();
        Robot.getMecanumDrive().OpenFoundationHook();

        Robot.getLift().LinearBarIn();

        try {
            angleFileReader = new FileReader(fileName);
            angleBufferedReader = new BufferedReader(angleFileReader);

            while((line = angleBufferedReader.readLine()) != null){
                readAngle = Double.parseDouble(line);
            }

            angleBufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Robot.getMecanumDrive().angleOffset = Robot.getMecanumDrive().getAngle().getTheda(AngleUnit.RADIANS) - readAngle;

        while (opModeIsActive()){
            loopTimer.reset();

            // Drive
            double slowDownFactor = driverStation.getSlowDownFactor();
            Robot.getMecanumDrive().setFieldCentricPower(
                    driverStation.getX() / slowDownFactor,
                    driverStation.getY() / slowDownFactor,
                    driverStation.getSteer() / slowDownFactor);

            if(!automateScoring) {
                Robot.getLift().SetLiftPower(driverStation.getLiftPower(liftDownLimit));

                if (driverStation.getLinearBarIn(first))
                    Robot.getLift().LinearBarIn();
                else
                    Robot.getLift().LinearBarOut();

                if (driverStation.getStartIntaking()){
                    Robot.getIntake().Intake();
                    Robot.getLift().GrabberOpen();
                    intaking = true;
                }else if (driverStation.getStopIntaking()){
                    Robot.getIntake().Stop();
                    intaking = false;
                }else if (driverStation.getStartOuttaking()){
                    Robot.getIntake().Outtake();
                    intaking = false;
                }

                if (driverStation.getGrabberOpen(first)){
                    Robot.getLift().GrabberOpen();
                    grabberOpen = true;
                }else {
                    Robot.getLift().GrabberClose();
                    grabberOpen = false;
                }

                automateScoring = driverStation.scoreStone() && !driverStation.getLinearBarIn(true);
                automatedScoringTimer.reset();
                first = true;
            } else {
                if(automatedScoringTimer.milliseconds() < 500) {
                    Robot.getLift().GrabberOpen();
                } else if(automatedScoringTimer.milliseconds() < 750 && automatedScoringTimer.milliseconds() > 250) {
                    Robot.getLift().SetLiftPower(-0.7);
                } else if(automatedScoringTimer.milliseconds() < 1250+200 && automatedScoringTimer.milliseconds() > 750) {
                    Robot.getLift().SetLiftPower(0);
                    Robot.getLift().LinearBarIn();
                } else if(!Robot.getLift().isDown() && (automatedScoringTimer.milliseconds() > 1250+200 && automatedScoringTimer.milliseconds() < 2000+200)) {
                    Robot.getLift().SetLiftPower(1);
                } else {
                    Robot.getLift().SetLiftPower(0);
                    automateScoring = false;
                }
                first = false;
            }

            if(matchTime.seconds() > 120-30 && driverStation.releaseTapeMeasure())
                Robot.tapeMeasureDeploy.setPosition(0.5);

            // Intake Servos
            if(driverStation.getIntake())
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
                    grabberOpen = false;
                    blockDetectorFirst = false;
                }
            }else if (!blockDetectorFirst)
                if (Robot.blockDetector.getDistance(DistanceUnit.INCH) > 4){
                    blockDetectorFirst = true;
                }

            if(gamepad2.a)
                Robot.getIntake().DeployCapStone();
            else
                Robot.getIntake().StopCapStone();



            telemetry.addData("Loop Time", loopTimer.milliseconds());
            telemetry.addData("angle", Robot.getMecanumDrive().getAngle().getTheda(AngleUnit.DEGREES));
            telemetry.addData("Slow Drive", driveSlow);

            telemetry.addData("Gyro After Auto", Robot.gyroAfterAuto);
            telemetry.addData("Gyro", Robot.gyro - Robot.gyroAfterAuto);

            telemetry.addData("Number of SkyStones", numSkyStones);

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
