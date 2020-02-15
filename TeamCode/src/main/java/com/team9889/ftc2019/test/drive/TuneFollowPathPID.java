package com.team9889.ftc2019.test.drive;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.team9889.ftc2019.Team9889Linear;
import com.team9889.ftc2019.auto.actions.Action;
import com.team9889.ftc2019.auto.actions.drive.DriveFollowPath;
import com.team9889.lib.FollowPath;
import com.team9889.lib.control.controllers.PID;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eric on 2/7/2020.
 */

@TeleOp
public class TuneFollowPathPID extends Team9889Linear {
    List<FollowPath> path = new ArrayList<>();
    double p = -0.1, i = 0, d = 0;
    double maxVelocity = .5;
    int number = 1;

    boolean lRToggle = true, upDownToggle = true, speedToggle = true;

    @Override
    public void runOpMode() throws InterruptedException {
        waitForStart(false);

        Robot.odometryLifter.setPosition(1);

        while (opModeIsActive()) {
            Robot.update();

            PID pid = new PID(p, i, d);

            if (gamepad1.a) {
                path.add(new FollowPath(new Pose2d(
                        24,
                        Robot.getMecanumDrive().getCurrentPose().getY(),
                        Robot.getMecanumDrive().getCurrentPose().getHeading()),
                        new Pose2d(2, 2, 3), 8.5, maxVelocity));
                path.add(new FollowPath(new Pose2d(
                        24,
                        Robot.getMecanumDrive().getCurrentPose().getY(),
                        Robot.getMecanumDrive().getCurrentPose().getHeading()),
                        new Pose2d(2, 2, 3), 8.5, maxVelocity));

                runAction(new DriveFollowPath(path, pid));
            }

            if (gamepad1.dpad_left && lRToggle) {
                number--;
                lRToggle = false;
            } else if (gamepad1.dpad_right && lRToggle) {
                number++;
                lRToggle = false;
            } else if (!gamepad1.dpad_left && !gamepad1.dpad_right) {
                lRToggle = true;
            }

            if (gamepad1.dpad_up && upDownToggle) {
                upDownToggle = false;
                if (number < 2)
                    p += 0.05;
                else if (number == 2)
                    i += 0.05;
                else if (number > 2)
                    d += 0.05;
            } else if (gamepad1.dpad_down && upDownToggle) {
                upDownToggle = false;
                if (number < 2)
                    p -= 0.05;
                else if (number == 2)
                    i -= 0.05;
                else if (number > 2)
                    d -= 0.05;
            } else if (!gamepad1.dpad_down && !gamepad1.dpad_up) {
                upDownToggle = true;
            }

            if (gamepad1.left_bumper && speedToggle) {
                speedToggle = false;
                maxVelocity -= .1;
            } else if (gamepad1.right_bumper && speedToggle) {
                speedToggle = false;
                maxVelocity += .1;
            } else if (!gamepad1.left_bumper && !gamepad1.right_bumper) {
                speedToggle = true;
            }


            if (number < 2)
                telemetry.addData("PID", "[" + p + "]" + ", " + i + ", " + d);
            else if (number == 2)
                telemetry.addData("PID", p + ", " + "[" + i + "]" + ", " + d);
            else if (number > 2)
                telemetry.addData("PID", p + ", " + i + ", " + "[" + d + "]");

            telemetry.addData("X", Robot.getMecanumDrive().getCurrentPose().getX());

            telemetry.addData("Max Velocity", maxVelocity);

            telemetry.update();
        }
    }
}
