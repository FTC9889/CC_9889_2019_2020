package com.team9889.ftc2019.test.drive;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.team9889.ftc2019.subsystems.Robot;

/**
 * Created by Eric on 9/15/2019.
 */

@TeleOp
@Deprecated
@Disabled
public class MecanumDriveTrainTest extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        telemetry.addData("Started Init", "");
        telemetry.update();

        Robot robot = Robot.getInstance();
        robot.init(hardwareMap, false);

        telemetry.addData("Inited", "");
        telemetry.update();
        waitForStart();
        while (opModeIsActive()){
            robot.getMecanumDrive().setPower(-gamepad1.left_stick_x, gamepad1.left_stick_y, -gamepad1.right_stick_x);

            if (gamepad1.b){
                //robot.getMecanumDrive().setPosition(20, 1, 0, 100);
            }else if (gamepad1.y) {
                //robot.getMecanumDrive().setPosition(40, 1, 0, 100);
            }
            else if (gamepad1.x){
                robot.getMecanumDrive().x = 0;
                robot.getMecanumDrive().y = 0;
//                robot.getMecanumDrive().travelTotal = 0;
            }

            telemetry.addData("xSpeed", robot.getMecanumDrive().xSpeed);
            telemetry.addData("ySpeed", robot.getMecanumDrive().ySpeed);
//            telemetry.addData("travel", robot.getMecanumDrive().travelTotal);

            telemetry.update();
            robot.update();
        }
    }
}
