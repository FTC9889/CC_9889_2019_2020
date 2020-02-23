package com.team9889.ftc2019.test.drive;

import android.util.Log;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.team9889.ftc2019.Constants;
import com.team9889.ftc2019.Team9889Linear;
import com.team9889.ftc2019.auto.AutoModeBase;
import com.team9889.ftc2019.auto.actions.RobotUpdate;
import com.team9889.ftc2019.auto.actions.drive.Drive3DimensionalPID;
import com.team9889.ftc2019.auto.actions.drive.DriveFollowPath;
import com.team9889.ftc2019.auto.actions.intake.Intake;
import com.team9889.ftc2019.auto.actions.intake.IntakeDown;
import com.team9889.lib.FollowPath;
import com.team9889.lib.android.FileWriter;
import com.team9889.lib.control.math.cartesian.Pose;
import com.team9889.lib.control.math.cartesian.Rotation2d;
import com.team9889.lib.control.math.cartesian.Vector2d;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joshua9889 on 12/24/2019.
 */
@Autonomous(group = "Test")
//@Disabled
public class OdometryWheelTest extends AutoModeBase {
    @Override
    public void run(Side side, SkyStonePosition stonePosition) {
        Side Side_ = Side.BLUE;
        List<FollowPath> pose = new ArrayList<>();
        Robot.redAuto = false;
        Robot.getMecanumDrive().setCurrentPose(new Pose2d());

//        while (opModeIsActive()){
//            Robot.update();
//
//            telemetry.addData("Left Odometry", -Robot.leftLift.getPosition());
//            telemetry.addData("Right Odometry", -Robot.intakeLeft.getPosition());
//            telemetry.addData("Side Odometry", Robot.intakeRight.getPosition());
//
//            telemetry.addData("Left Odometry", Robot.getMecanumDrive().Left_OdometryPosition());
//            telemetry.addData("Right Odometry", Robot.getMecanumDrive().Right_OdometryPosition());
//            telemetry.addData("Side Odometry", Robot.getMecanumDrive().Y_OdometryPosition());
//
//            telemetry.update();
//        }

        pose.add(new FollowPath(new Pose2d(20, 0, 0), new Pose2d(2, 2, 3), 4, 1));
        pose.add(new FollowPath(new Pose2d(20, 0, 0), new Pose2d(2, 2, 3), 4, 1));
        runAction(new DriveFollowPath(pose));
    }
}
