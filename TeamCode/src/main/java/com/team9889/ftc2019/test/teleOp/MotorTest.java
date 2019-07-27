package com.team9889.ftc2019.test.teleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.team9889.ftc2019.Constants;
import com.team9889.ftc2019.subsystems.Robot;
import com.team9889.lib.hardware.Motor;

/**
 * Created by Eric on 7/26/2019.
 */
@TeleOp
public class MotorTest extends LinearOpMode {
    private ElapsedTime timer = new ElapsedTime();

    @Override
    public void runOpMode() throws InterruptedException {
        Robot robot = Robot.getInstance();
        robot.init(hardwareMap, false);
        waitForStart();

        while (opModeIsActive()){
            robot.getDrive().setThrottleSteerPower(gamepad1.left_stick_y, gamepad1.right_stick_x);

            if(gamepad1.a){
                robot.intake.setPower(.5);
            }
            else{
                robot.intake.setPower(0);
            }

            telemetry.addData("loop time", timer.milliseconds());
            telemetry.addData("Hardware", Motor.numHardwareUsesThisUpdate);
            telemetry.addData("front left position", robot.fLDrive.getPosition() * Constants.DriveConstants.ENCODER_TO_DISTANCE_RATIO * Constants.DriveConstants.AngleToInchRatio);
            telemetry.addData("front right position", robot.fRDrive.getPosition() * Constants.DriveConstants.ENCODER_TO_DISTANCE_RATIO * Constants.DriveConstants.AngleToInchRatio);
            telemetry.addData("x", robot.pose[0]);
            telemetry.addData("y", robot.pose[1]);
            telemetry.addData("angle", robot.pose[3]);
            telemetry.update();
            timer.reset();
            Motor.numHardwareUsesThisUpdate = 0;
        }
    }
}