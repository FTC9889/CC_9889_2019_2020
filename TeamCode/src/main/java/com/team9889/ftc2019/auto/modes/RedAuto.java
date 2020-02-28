package com.team9889.ftc2019.auto.modes;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.team9889.ftc2019.auto.AutoModeBase;
import com.team9889.ftc2019.auto.actions.Wait;
import com.team9889.ftc2019.auto.actions.drive.DriveFollowPath;
import com.team9889.ftc2019.auto.actions.drive.DriveToFoundation;
import com.team9889.ftc2019.auto.actions.drive.Foundation;
import com.team9889.ftc2019.auto.actions.foundation.FoundationHookClose;
import com.team9889.ftc2019.auto.actions.intake.Intake;
import com.team9889.ftc2019.auto.actions.intake.IntakeDown;
import com.team9889.ftc2019.auto.actions.intake.IntakeStopBlockIn;
import com.team9889.ftc2019.auto.actions.intake.IntakeStopBlockInWait;
import com.team9889.ftc2019.auto.actions.lift.Grabber;
import com.team9889.ftc2019.auto.actions.lift.Lift;
import com.team9889.ftc2019.auto.actions.lift.LiftLinearBar;
import com.team9889.ftc2019.auto.actions.lift.LiftWait;
import com.team9889.ftc2019.auto.actions.lift.LinearWait;
import com.team9889.ftc2019.auto.actions.lift.OpenGrabberWait;
import com.team9889.lib.FollowPath;

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
                pose.add(new FollowPath(new Pose2d(4, 0 * Side.getNum(Side_), 0 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 3, 1));
                pose.add(new FollowPath(new Pose2d(26, -2 * Side.getNum(Side_), -30 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 4, 1));
                pose.add(new FollowPath(new Pose2d(44, -2 * Side.getNum(Side_), -30 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 4, .8));
                pose.add(new FollowPath(new Pose2d(30, -2 * Side.getNum(Side_), -30 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 4, 1));
                break;

            case MIDDLE:
                pose.add(new FollowPath(new Pose2d(4, 0 * Side.getNum(Side_), 0 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 3, 1));
                pose.add(new FollowPath(new Pose2d(26, -10 * Side.getNum(Side_), -20 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 4, 1));
                pose.add(new FollowPath(new Pose2d(38, -10 * Side.getNum(Side_), -20 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 4, .8));
                pose.add(new FollowPath(new Pose2d(30, -10 * Side.getNum(Side_), -20 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 4, 1));
                pose.add(new FollowPath(new Pose2d(24, 0 * Side.getNum(Side_), -90 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 8.5, 1));
                break;

            case LEFT:
                pose.add(new FollowPath(new Pose2d(4, 0 * Side.getNum(Side_), 0 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 3, 1));
                pose.add(new FollowPath(new Pose2d(26, -15 * Side.getNum(Side_), -30 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 4, 1));
                pose.add(new FollowPath(new Pose2d(38, -15 * Side.getNum(Side_), -30 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 4, .8));
                pose.add(new FollowPath(new Pose2d(30, -15 * Side.getNum(Side_), -30 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 4, 1));
                pose.add(new FollowPath(new Pose2d(24, -10 * Side.getNum(Side_), -90 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 8.5, 1));
                break;
        }
        pose.add(new FollowPath(new Pose2d(24, 39 * Side.getNum(Side_), -90 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 6, 1));
        pose.add(new FollowPath(new Pose2d(25, 72 * Side.getNum(Side_), -90 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 4, 1));
        pose.add(new FollowPath(new Pose2d(25, 82 * Side.getNum(Side_), -180 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 4, 1));

        runAction(new DriveFollowPath(pose));
        pose.clear();

        runAction(new Intake(false));
        runAction(new DriveToFoundation(3000, -180));

        ThreadAction(new LiftWait(0, -1, 200));
        ThreadAction(new LiftWait(1900, 1, 300));
        ThreadAction(new LinearWait(0, true));
        ThreadAction(new OpenGrabberWait(1500, true));
        ThreadAction(new LinearWait(1700, false));
        ThreadAction(new Foundation(false, 55, false));
        runAction(new FoundationHookClose());

        ThreadAction(new IntakeStopBlockInWait(1000));
        pose.add(new FollowPath(new Pose2d(24, 50 * Side.getNum(Side_), -110 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 4, 1));

        switch (stonePosition){
            case RIGHT:
                pose.add(new FollowPath(new Pose2d(22, 24 * Side.getNum(Side_), -90 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 4, 1, 4000));
                pose.add(new FollowPath(new Pose2d(22, 24 * Side.getNum(Side_), -30 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 4, .8, 2000));
                pose.add(new FollowPath(new Pose2d(38, 24 * Side.getNum(Side_), -30 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 2, .8, 1000));
                pose.add(new FollowPath(new Pose2d(38, 18 * Side.getNum(Side_), -30 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 2, .8, 2000));
                break;

            case MIDDLE:
                pose.add(new FollowPath(new Pose2d(22, 24 * Side.getNum(Side_), -90 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 4, 1, 4000));
                pose.add(new FollowPath(new Pose2d(22, 24 * Side.getNum(Side_), -75 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 4, .8, 2000));
                pose.add(new FollowPath(new Pose2d(34, 24 * Side.getNum(Side_), -75 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 2, .8, 1000));
                pose.add(new FollowPath(new Pose2d(34, 17 * Side.getNum(Side_), -75 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 2, .8, 2000));
                break;

            case LEFT:
                pose.add(new FollowPath(new Pose2d(22, 20 * Side.getNum(Side_), -90 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 4, 1));
                pose.add(new FollowPath(new Pose2d(22, 16 * Side.getNum(Side_), -75 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 4, .8));
                pose.add(new FollowPath(new Pose2d(34, 16 * Side.getNum(Side_), -75 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 2, .8));
                pose.add(new FollowPath(new Pose2d(34, 8 * Side.getNum(Side_), -75 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 2, .8));
                break;
        }
        runAction(new DriveFollowPath(pose));
        pose.clear();

        ThreadAction(new Lift(-1, 300, 50, true));
        ThreadAction(new LiftLinearBar(false, 50, true));
        ThreadAction(new Grabber(true, 600, 53, true));
        pose.add(new FollowPath(new Pose2d(18, 10 * Side.getNum(Side_), -45 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 4, 1));
        pose.add(new FollowPath(new Pose2d(20, 55 * Side.getNum(Side_), -90 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 4, 1));
        runAction(new DriveFollowPath(pose));
        pose.clear();

        runAction(new Wait(500));

        runAction(new Intake(false));
        ThreadAction(new LinearWait(800, false));
        runAction(new LiftWait(1000, 1, 400));

        ThreadAction(new IntakeStopBlockInWait(500));
        ThreadAction(new LiftLinearBar(false, 65, true));
        ThreadAction(new Lift(-1, 400, 60, true));
        ThreadAction(new Grabber(true, 1000, 70, true));
        switch (stonePosition){
            case RIGHT:
                pose.add(new FollowPath(new Pose2d(26, -15 * Side.getNum(Side_), -30 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 4, 1));
                pose.add(new FollowPath(new Pose2d(42, -15 * Side.getNum(Side_), -30 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 4, .8));
                pose.add(new FollowPath(new Pose2d(42, -15 * Side.getNum(Side_), -30 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 4, 1));
                pose.add(new FollowPath(new Pose2d(24, -10 * Side.getNum(Side_), -90 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 8.5, 1));
                break;

            case MIDDLE:
            case LEFT:
                pose.add(new FollowPath(new Pose2d(22, 20 * Side.getNum(Side_), -90 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 4, 1));
                pose.add(new FollowPath(new Pose2d(22, 20 * Side.getNum(Side_), 0 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 4, 1));
                pose.add(new FollowPath(new Pose2d(45, 20 * Side.getNum(Side_), 0 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 4, 1));
                pose.add(new FollowPath(new Pose2d(20, 20 * Side.getNum(Side_), -90 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 4, 1));
                break;
        }
        pose.add(new FollowPath(new Pose2d(24, 80 * Side.getNum(Side_), -90 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 4, 1));
        runAction(new DriveFollowPath(pose));
        pose.clear();

        runAction(new Wait(700));

        ThreadAction(new Lift(1, 500, 60, false));
        ThreadAction(new LiftLinearBar(true, 65, false));
        pose.add(new FollowPath(new Pose2d(26, 50 * Side.getNum(Side_), -90 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 4, 1));
        pose.add(new FollowPath(new Pose2d(26, 50 * Side.getNum(Side_), -90 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 4, 1));
        runAction(new DriveFollowPath(pose));
        pose.clear();

//        pose.add(new FollowPath(new Pose2d(0, 0 * Side.getNum(Side_), 0 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 4, 1));
    }
}
