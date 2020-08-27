package com.team9889.ftc2019.test.drive;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.team9889.ftc2019.auto.AutoModeBase;
import com.team9889.ftc2019.auto.actions.drive.DriveFollowPath;
import com.team9889.lib.Path;
import com.team9889.lib.control.controllers.PID;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eric on 2/7/2020.
 */

@Autonomous
public class TuneFollowPathPIDAuto extends AutoModeBase {
    List<Path> path = new ArrayList<>();
    double p = -0.2, i = 0, d = -16;
    double maxVelocity = .5, add = 0.05;
    int number = 1;

    boolean lRToggle = true, upDownToggle = true, speedToggle = true, addToggle = true;

    @Override
    public void run(Side side, SkyStonePosition stonePosition) {
        while (opModeIsActive()) {
            Robot.update();

            PID pid = new PID(p, i, d);

            if (gamepad1.a) {
                path.add(new Path(new Pose2d(
                        Robot.getMecanumDrive().getCurrentPose().getX() + 24,
                        Robot.getMecanumDrive().getCurrentPose().getY(),
                        Robot.getMecanumDrive().getCurrentPose().getHeading()),
                        new Pose2d(1, 1, 2), 2, maxVelocity));
                path.add(new Path(new Pose2d(
                        Robot.getMecanumDrive().getCurrentPose().getX() + 24,
                        Robot.getMecanumDrive().getCurrentPose().getY(),
                        Robot.getMecanumDrive().getCurrentPose().getHeading()),
                        new Pose2d(1, 1, 2), 8.5, maxVelocity));

                runAction(new DriveFollowPath(path, pid, telemetry));
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
                    p += add;
                else if (number == 2)
                    i += add;
                else if (number > 2)
                    d += add;
            } else if (gamepad1.dpad_down && upDownToggle) {
                upDownToggle = false;
                if (number < 2)
                    p -= add;
                else if (number == 2)
                    i -= add;
                else if (number > 2)
                    d -= add;
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

            if (gamepad1.x && addToggle){
                add += 0.1;
                addToggle = false;
            }else if (gamepad1.y && addToggle){
                add -= 0.1;
                addToggle = false;
            }else if (!gamepad1.x && !gamepad1.y){
                addToggle = true;
            }


            if (number < 2)
                telemetry.addData("PID", "[" + p + "]" + ", " + i + ", " + d);
            else if (number == 2)
                telemetry.addData("PID", p + ", " + "[" + i + "]" + ", " + d);
            else if (number > 2)
                telemetry.addData("PID", p + ", " + i + ", " + "[" + d + "]");

            telemetry.addData("X", Robot.getMecanumDrive().getCurrentPose().getX());

            telemetry.addData("Max Velocity", maxVelocity);
            telemetry.addData("Add Amount", add);

            telemetry.update();
        }

    }
}
