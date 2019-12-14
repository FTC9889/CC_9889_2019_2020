package com.team9889.ftc2019;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.internal.usb.exception.RobotUsbTimeoutException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
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

    @Override
    public void runOpMode() {
        telemetry.addData("Started Init", "");
        telemetry.update();

        com.team9889.ftc2019.subsystems.Robot robot = com.team9889.ftc2019.subsystems.Robot.getInstance();
        robot.init(hardwareMap, false);

        telemetry.addData("Inited", "");
        telemetry.update();
        waitForStart();

        Robot.getLift().GrabberOpen();
        Robot.getIntake().IntakeDown();
        Robot.getMecanumDrive().OpenFoundationHook();

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

//          Drive
            if (!driveSlow)
                robot.getMecanumDrive().setFieldCentricPower(gamepad1.left_stick_x, -gamepad1.left_stick_y, gamepad1.right_stick_x);
            else if (driveSlow)
                robot.getMecanumDrive().setFieldCentricPower(gamepad1.left_stick_x / 3, -gamepad1.left_stick_y / 3, gamepad1.right_stick_x / 3);

//          Lift
            if (gamepad1.right_trigger > 0.1) {
                Robot.getLift().SetLiftPower(-gamepad1.right_trigger);
            }
            else if (gamepad1.left_trigger > 0.1 && !liftDownLimit) {
                Robot.getLift().SetLiftPower(gamepad1.left_trigger);
            }
            else if (gamepad2.right_trigger > 0.1) {
                Robot.getLift().SetLiftPower(-gamepad2.right_trigger);
            }
            else if (gamepad2.left_trigger > 0.1 && !liftDownLimit) {
                Robot.getLift().SetLiftPower(gamepad2.left_trigger);
            }
            else if (!lift && !liftGoingDown){
                Robot.getLift().SetLiftPower(-0.2);
            }

//          Grabber
            if (gamepad1.dpad_right){
                Robot.getLift().GrabberClose();
                grabberOpen = false;
            }else if (gamepad1.dpad_left || linearBar){
                if (first) {
                    Robot.getLift().GrabberOpen();
                    grabberOpen = true;
                    first = false;
                    linearBar = true;
                    lift = true;
                    liftFirst = true;
                    linearBarTimer.reset();
                    liftTimer.reset();

                    if (Robot.linearBar.getPosition() > 0){
                        linearBarIn = false;
                    }
                    else {
                        linearBarIn = true;
                    }
                }else if (!linearBarIn) {
                    if (linearBarTimer.milliseconds() > 1000) {
                        first = true;
                        linearBar = false;
                        liftGoingDown = true;
                        linearBarIn = true;
                        Robot.getLift().SetLiftPower(1);
                    } else if (liftTimer.milliseconds() > 400 && lift) {
                        Robot.getLift().SetLiftPower(0);
                        lift = false;
                    } else if (liftTimer.milliseconds() > 200 && liftFirst) {
                        Robot.getLift().SetLiftPower(-.7);
                        Robot.getLift().LinearBarIn();
                        liftTimer.reset();
                        liftFirst = false;
                    } else {
                        lift = false;
                    }
                }else {
                    first = true;
                    lift = false;
                    linearBar = false;
                    Robot.getLift().LinearBarIn();
                }
            }

//          Intake
            if (gamepad1.a){
                robot.getIntake().Intake();
                intaking = true;
            }else if (gamepad1.b){
                robot.getIntake().Stop();
                intaking = false;
            }else if (gamepad1.y){
                robot.getIntake().Outtake();
                intaking = false;
            }

//          Intake Servos
            if (gamepad1.right_bumper)
                Robot.getIntake().IntakeDown();
            else if (gamepad1.left_bumper)
                Robot.getIntake().IntakeUp();

//          Foundation Hook
            if (gamepad2.dpad_down){
                Robot.getMecanumDrive().CloseFoundationHook();
            }else if (gamepad2.dpad_up){
                Robot.getMecanumDrive().OpenFoundationHook();
            }

//          Linear Bar
            if (gamepad2.left_bumper){
                Robot.getLift().LinearBarIn();
            }else if (gamepad2.right_bumper){
                Robot.getLift().LinearBarOut();
            }

//          Intake Roller
            if (gamepad2.a){
                Robot.getIntake().RollerIn();
            }else if (gamepad2.b){
                Robot.getIntake().RollerStop();
            }else if (gamepad2.y){
                Robot.getIntake().RollerOut();
            }

//          Slow Down Button
            if (gamepad2.x && xButtonTimer.milliseconds() > 500 || gamepad1.x && xButtonTimer.milliseconds() > 500){
                driveSlow = !driveSlow;
                xButtonTimer.reset();
            }

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

            if (liftGoingDown) {
                if (Robot.downLimit.green() > 350) {
                    liftDownLimit = true;
                    liftGoingDown = false;
                } else {
                    liftDownLimit = false;
                }
            }else {
                liftDownLimit = false;
            }

            if (gamepad2.right_stick_button){
                Robot.getLift().GrabberOpen();
                grabberOpen = true;
            }else if (gamepad2.left_stick_button){
                Robot.getLift().GrabberClose();
                grabberOpen = false;
            }

            telemetry.addData("Loop Time", loopTimer.milliseconds());
            telemetry.addData("angle", robot.getMecanumDrive().getAngle().getTheda(AngleUnit.DEGREES));
            telemetry.addData("Slow Drive", driveSlow);

            telemetry.addData("hi", Robot.downLimit.green());

            telemetry.update();
            robot.update();
        }
    }
}
