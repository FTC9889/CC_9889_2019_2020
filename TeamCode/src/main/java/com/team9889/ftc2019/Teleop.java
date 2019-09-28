package com.team9889.ftc2019;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.internal.usb.exception.RobotUsbTimeoutException;

/**
 * Created by MannoMation on 1/14/2019.
 */

@TeleOp(name = "Teleop")
public class Teleop extends Team9889Linear {

    private ElapsedTime loopTimer = new ElapsedTime();

    @Override
    public void runOpMode() {
        telemetry.addData("Started Init", "");
        telemetry.update();

        com.team9889.ftc2019.subsystems.Robot robot = com.team9889.ftc2019.subsystems.Robot.getInstance();
        robot.init(hardwareMap, false);

        telemetry.addData("Inited", "");
        telemetry.update();
        waitForStart();

        while (opModeIsActive()){
            loopTimer.reset();

            robot.getMecanumDrive().setPower(-gamepad1.left_stick_x, gamepad1.left_stick_y, -gamepad1.right_stick_x);

            if (gamepad1.a){
                robot.getIntake().Intake();
            }else if (gamepad1.b){
                robot.getIntake().Stop();
            }else if (gamepad1.y){
                robot.getIntake().Outtake();
            }

            if (gamepad1.left_bumper){
                robot.getIntake().capUp();
            }else if (gamepad1.right_bumper){
                robot.getIntake().capDump();
            }

            telemetry.addData("Loop Time", loopTimer.milliseconds());
            telemetry.addData("angle", robot.getMecanumDrive().getAngle().getTheda(AngleUnit.DEGREES));

            telemetry.addData("x", gamepad1.left_stick_x);
            telemetry.addData("y", gamepad1.left_stick_y);

            telemetry.update();
            robot.update();
        }
    }
}
