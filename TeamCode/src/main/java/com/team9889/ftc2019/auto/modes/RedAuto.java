package com.team9889.ftc2019.auto.modes;

import android.util.Log;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.team9889.ftc2019.auto.AutoModeBase;
import com.team9889.ftc2019.auto.actions.Wait;
import com.team9889.ftc2019.auto.actions.drive.Drive3DimensionalPID;
import com.team9889.ftc2019.auto.actions.drive.DriveFollowPath;
import com.team9889.ftc2019.auto.actions.drive.DriveToFoundation;
import com.team9889.ftc2019.auto.actions.drive.Foundation;
import com.team9889.ftc2019.auto.actions.foundation.FoundationHookClose;
import com.team9889.ftc2019.auto.actions.foundation.FoundationHookOpen;
import com.team9889.ftc2019.auto.actions.intake.Intake;
import com.team9889.ftc2019.auto.actions.intake.IntakeDown;
import com.team9889.ftc2019.auto.actions.intake.IntakeRollerOn;
import com.team9889.ftc2019.auto.actions.intake.IntakeRollerStop;
import com.team9889.ftc2019.auto.actions.intake.IntakeStopBlockIn;
import com.team9889.ftc2019.auto.actions.lift.CloseGrabber;
import com.team9889.ftc2019.auto.actions.lift.Lift;
import com.team9889.ftc2019.auto.actions.lift.LiftIn;
import com.team9889.ftc2019.auto.actions.lift.LiftLinearBar;
import com.team9889.ftc2019.auto.actions.lift.LiftOut;
import com.team9889.ftc2019.auto.actions.lift.LiftUp;
import com.team9889.ftc2019.auto.actions.lift.LiftWait;
import com.team9889.ftc2019.auto.actions.lift.LinearWait;
import com.team9889.ftc2019.auto.actions.lift.OpenGrabber;
import com.team9889.ftc2019.auto.actions.lift.OpenGrabberWait;
import com.team9889.lib.FollowPath;

import org.opencv.core.Mat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eric on 11/26/2019.
 */

@Autonomous
public class RedAuto extends AutoModeBase {
    @Override
    public void run(Side side, AutoModeBase.SkyStonePosition stonePosition) {
        Side Side_ = Side.RED;
        List<FollowPath> pose = new ArrayList<>();
        Robot.redAuto = true;

        Robot.grabber.setPosition(1);
        ThreadAction(new IntakeDown());
        ThreadAction(new IntakeStopBlockIn());

        switch (stonePosition){
            case RIGHT:
                break;

            case MIDDLE:
                break;

            case LEFT:
                pose.add(new FollowPath(new Pose2d(4, 0 * Side.getNum(Side_), 0 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 3, 1));
                pose.add(new FollowPath(new Pose2d(38, -15 * Side.getNum(Side_), -30 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 4, 1));
                pose.add(new FollowPath(new Pose2d(30, -15 * Side.getNum(Side_), -30 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 4, 1));
                pose.add(new FollowPath(new Pose2d(24, -10 * Side.getNum(Side_), -90 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 8.5, 1));
                break;
        }
        pose.add(new FollowPath(new Pose2d(24, 39 * Side.getNum(Side_), -90 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 6, 1));
        pose.add(new FollowPath(new Pose2d(32, 72 * Side.getNum(Side_), -90 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 4, 1));
        pose.add(new FollowPath(new Pose2d(36, 82 * Side.getNum(Side_), -180 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 4, 1));

        runAction(new DriveFollowPath(pose));
        pose.clear();

        runAction(new DriveToFoundation(3000, -180));

        ThreadAction(new LiftWait(0, -.5, 200));
        ThreadAction(new LiftWait(1500, .5, 300));
        ThreadAction(new LinearWait(150, true));
        ThreadAction(new OpenGrabberWait(1000, true));
        ThreadAction(new LinearWait(1500, false));
        ThreadAction(new Foundation(false, 55, false));
        runAction(new FoundationHookClose());

        ThreadAction(new IntakeStopBlockIn());
        pose.add(new FollowPath(new Pose2d(24, 50 * Side.getNum(Side_), -90 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 4, 1));
        pose.add(new FollowPath(new Pose2d(24, 20 * Side.getNum(Side_), -90 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 8.5, 1));
        pose.add(new FollowPath(new Pose2d(27, 10 * Side.getNum(Side_), -45 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 8.5, 1));
        pose.add(new FollowPath(new Pose2d(36, 4 * Side.getNum(Side_), -45 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 4, 1));
        runAction(new DriveFollowPath(pose));
        pose.clear();

        ThreadAction(new Lift(-.5, 200, 50, true));
        pose.add(new FollowPath(new Pose2d(24, 10 * Side.getNum(Side_), -45 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 4, 1));
        pose.add(new FollowPath(new Pose2d(24, 50 * Side.getNum(Side_), -90 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 4, 1));


        runAction(new DriveFollowPath(pose));

        pose.add(new FollowPath(new Pose2d(0, 0 * Side.getNum(Side_), 0 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 4, 1));

        //pick up first skystone
//        switch (stonePosition){
//            case RIGHT:
//                pose.add(new FollowPath(new Pose2d(7, 0, Math.toRadians(0)), new Pose2d(2, 2, 3), 3, .5));
//                pose.add(new FollowPath(new Pose2d(36, -2, Math.toRadians(35)), new Pose2d(2, 2, 3), 3, .7));
//                pose.add(new FollowPath(new Pose2d(30, 0, Math.toRadians(35)), new Pose2d(2, 2, 3), 3, .5));
//                runAction(new DriveFollowPath(pose));
//                pose.clear();
//                break;
//
//            case MIDDLE:
//                pose.add(new FollowPath(new Pose2d(34, -3, Math.toRadians(15)), new Pose2d(2, 2, 3), 3, .7));
//                pose.add(new FollowPath(new Pose2d(34, -3, Math.toRadians(15)), new Pose2d(2, 2, 3), 3, .7));
//                pose.add(new FollowPath(new Pose2d(30, 0, Math.toRadians(25)), new Pose2d(2, 2, 3), 3, .5));
//                runAction(new DriveFollowPath(pose));
//                pose.clear();
//                break;
//
//            case LEFT:
//                pose.add(new FollowPath(new Pose2d(34, -3, Math.toRadians(0)), new Pose2d(2, 2, 3), 3, .7));
//                pose.add(new FollowPath(new Pose2d(34, -3, Math.toRadians(-35)), new Pose2d(2, 2, 3), 3, .7));
//                pose.add(new FollowPath(new Pose2d(30, 0, Math.toRadians(-35)), new Pose2d(2, 2, 3), 3, .5));
//                runAction(new DriveFollowPath(pose));
//                pose.clear();
//                break;
//        }
//
//        pose.add(new FollowPath(new Pose2d(30, 60, Math.toRadians(90)), new Pose2d(2, 2, 3), 4, 1));
//        pose.add(new FollowPath(new Pose2d(34, 76, Math.toRadians(90)), new Pose2d(2, 2, 3), 7.5, .8));
//        pose.add(new FollowPath(new Pose2d(34, 76, Math.toRadians(180)), new Pose2d(2, 2, 3), 7.5, .5));
//        runAction(new DriveFollowPath(pose));
//        pose.clear();
//
//        Robot.getIntake().SetIntakePower(-.3);
//        runAction(new DriveToFoundation(2000, 180));
//        runAction(new FoundationHookClose());
//        Robot.getIntake().SetIntakePower(0);
//
//        ThreadAction(new LiftUp(250, -.4));
//        ThreadAction(new LiftOut());
//
//        pose.add(new FollowPath(new Pose2d(30, 50, Math.toRadians(-90)), new Pose2d(2, 2, 1), 7.5, .8));
//        pose.add(new FollowPath(new Pose2d(30, 50, Math.toRadians(-90)), new Pose2d(2, 2, 1), 3, .5));
//        runAction(new DriveFollowPath(pose));
//        pose.clear();
//
//        Robot.grabber.setPosition(1);
//        runAction(new FoundationHookOpen());
//        ThreadAction(new IntakeStopBlockIn());
//
//        ThreadAction(new LiftIn());
//        ThreadAction(new LiftUp(250, 1));
//
//        //grab second skystone
//        switch (stonePosition){
//            case RIGHT:
//                pose.add(new FollowPath(new Pose2d(30, 5, Math.toRadians(-90)), new Pose2d(2, 2, 3), 7.5, .8));
//                pose.add(new FollowPath(new Pose2d(45, -7, Math.toRadians(-45)), new Pose2d(2, 2, 3), 3, .8));
//                pose.add(new FollowPath(new Pose2d(30, -10, Math.toRadians(-90)), new Pose2d(2, 2, 3), 3, .5));
//                runAction(new DriveFollowPath(pose));
//                pose.clear();
//                break;
//
//            case MIDDLE:
//                pose.add(new FollowPath(new Pose2d(30, -3, Math.toRadians(-90)), new Pose2d(2, 2, 3), 3, .8));
//                pose.add(new FollowPath(new Pose2d(45, -11, Math.toRadians(-65)), new Pose2d(2, 2, 3), 3, .5));
//                pose.add(new FollowPath(new Pose2d(30, -11, Math.toRadians(-90)), new Pose2d(2, 2, 3), 3, .5));
//                runAction(new DriveFollowPath(pose));
//                pose.clear();
//                break;
//
//            case LEFT:
//                pose.add(new FollowPath(new Pose2d(30, -15, Math.toRadians(-90)), new Pose2d(2, 2, 3), 7.5, .8));
//                pose.add(new FollowPath(new Pose2d(30, -15, Math.toRadians(-65)), new Pose2d(2, 2, 3), 7.5, .8));
//                pose.add(new FollowPath(new Pose2d(45, -25, Math.toRadians(-65)), new Pose2d(2, 2, 3), 3, .8));
//                pose.add(new FollowPath(new Pose2d(30, -22, Math.toRadians(-90)), new Pose2d(2, 2, 3), 3, .5));
//                runAction(new DriveFollowPath(pose));
//                pose.clear();
//                break;
//        }
//
//        pose.add(new FollowPath(new Pose2d(29, 45, Math.toRadians(-90)), new Pose2d(2, 2, 3), 7, .7));
//        pose.add(new FollowPath(new Pose2d(29, 45, Math.toRadians(-90)), new Pose2d(2, 2, 3), 4, .5));
//        runAction(new DriveFollowPath(pose));
//        pose.clear();
//
//        Robot.getIntake().SetIntakePower(0);
//        ThreadAction(new LiftUp(250, -.8));
//        ThreadAction(new LiftOut());
//        runAction(new DriveToFoundation(2000, -90));
//
//        runAction(new FoundationHookOpen());
//
//        runAction(new Wait(500));
//
//        Robot.grabber.setPosition(1);
//        runAction(new Wait(250));
//
//        runAction(new LiftIn());
//        runAction(new Wait(250));
//        Robot.getLift().SetLiftPower(.6);
//        runAction(new Wait(250));
//        Robot.getLift().SetLiftPower(0);
//
//        ThreadAction(new IntakeStopBlockIn());
//        //grab third skystone
//        switch (stonePosition){
//            case RIGHT:
//            case MIDDLE:
//                pose.add(new FollowPath(new Pose2d(30, -5, Math.toRadians(-90)), new Pose2d(2, 2, 3), 7.5, .8));
//                pose.add(new FollowPath(new Pose2d(30, -5, Math.toRadians(-65)), new Pose2d(2, 2, 3), 7.5, .8));
//                pose.add(new FollowPath(new Pose2d(45, -25, Math.toRadians(-65)), new Pose2d(2, 2, 3), 3, .8));
//                pose.add(new FollowPath(new Pose2d(30, -22, Math.toRadians(-90)), new Pose2d(4, 4, 4), 3, .5));
//                runAction(new DriveFollowPath(pose));
//                pose.clear();
//                break;
//
//            case LEFT:
//                pose.add(new FollowPath(new Pose2d(30, 15, Math.toRadians(-90)), new Pose2d(2, 2, 3), 7.5, .8));
//                pose.add(new FollowPath(new Pose2d(30, 15, Math.toRadians(0)), new Pose2d(2, 2, 3), 7.5, .8));
//                pose.add(new FollowPath(new Pose2d(45, 15, Math.toRadians(0)), new Pose2d(2, 2, 3), 3, .8));
//                pose.add(new FollowPath(new Pose2d(30, 15, Math.toRadians(-90)), new Pose2d(2, 2, 3), 3, .5));
//                runAction(new DriveFollowPath(pose));
//                pose.clear();
//                break;
//        }
//
//        pose.add(new FollowPath(new Pose2d(32, 45, Math.toRadians(-90)), new Pose2d(2, 2, 3), 4, .7));
//        pose.add(new FollowPath(new Pose2d(32, 45, Math.toRadians(-90)), new Pose2d(2, 2, 3), 4, .5));
//        runAction(new DriveFollowPath(pose));
//        pose.clear();
//
//        Robot.getIntake().SetIntakePower(0);
//        runAction(new LiftUp(250, -1));
//        ThreadAction(new LiftOut());
//
//        pose.add(new FollowPath(new Pose2d(32, 75, Math.toRadians(-90)), new Pose2d(2, 2, 3), 8, 1));
//        pose.add(new FollowPath(new Pose2d(32, 75, Math.toRadians(-90)), new Pose2d(4, 4, 3), 8, 1));
//        runAction(new DriveFollowPath(pose));
//        pose.clear();
//
//        Robot.grabber.setPosition(1);
//        runAction(new Wait(250));
//
//        runAction(new LiftIn());
//        ThreadAction(new LiftUp(2000, .3));
//
//        pose.add(new FollowPath(new Pose2d(34, 36, Math.toRadians(-90)), new Pose2d(2, 2, 3), 8, .7));
//        pose.add(new FollowPath(new Pose2d(34, 36, Math.toRadians(-90)), new Pose2d(2, 2, 3), 8, .7));
//        runAction(new DriveFollowPath(pose));
//        pose.clear();

    }
}
