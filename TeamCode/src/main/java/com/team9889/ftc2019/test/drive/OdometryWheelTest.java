package com.team9889.ftc2019.test.drive;

import android.util.Log;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.team9889.ftc2019.Team9889Linear;
import com.team9889.ftc2019.auto.AutoModeBase;
import com.team9889.ftc2019.auto.actions.RobotUpdate;
import com.team9889.ftc2019.auto.actions.drive.Drive3DimensionalPID;
import com.team9889.ftc2019.auto.actions.intake.Intake;
import com.team9889.ftc2019.auto.actions.intake.IntakeDown;
import com.team9889.lib.android.FileWriter;
import com.team9889.lib.control.math.cartesian.Pose;
import com.team9889.lib.control.math.cartesian.Rotation2d;
import com.team9889.lib.control.math.cartesian.Vector2d;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

/**
 * Created by joshua9889 on 12/24/2019.
 */
@Autonomous(group = "Test")
//@Disabled
public class OdometryWheelTest extends AutoModeBase {
    @Override
    public void run(Side side, SkyStonePosition stonePosition) {
//        Robot.getMecanumDrive().setCurrentPose(new Pose2d());
//
//        runAction(new Drive3DimensionalPID(new Pose2d(30, 20, Math.toRadians(45))));
//        runAction(new Drive3DimensionalPID(new Pose2d(60, 0, Math.toRadians(0))));
//        runAction(new Drive3DimensionalPID(new Pose2d(30, 0, Math.toRadians(-90))));
//        runAction(new Drive3DimensionalPID(new Pose2d(0, 0, 0)));
//        runAction(new Drive3DimensionalPID(new Pose2d(0, 0, 0), new Pose2d(1, 1, 2)));

//        while (opModeIsActive()) {
//            telemetry.addData("Pose: ", Robot.getMecanumDrive().getCurrentPose().toString());
//            telemetry.addData("X", Robot.getMecanumDrive().X_OdometryPosition());
//            telemetry.addData("Y", Robot.getMecanumDrive().Y_OdometryPosition());
//            telemetry.update();
//        }

        ThreadAction(new IntakeDown());
        ThreadAction(new Intake());

        runAction(new Drive3DimensionalPID(new Pose2d(36, 17, Math.toRadians(35)), new Pose2d(1, 1, Math.toRadians(1)),.35));
        runAction(new Drive3DimensionalPID(new Pose2d(25, 17, Math.toRadians(90)), new Pose2d(1, 1, Math.toRadians(1))));
        runAction(new Intake(false));
        runAction(new Drive3DimensionalPID(new Pose2d(0, 0, 0), new Pose2d(1, 1, Math.toRadians(1))));
    }
}
