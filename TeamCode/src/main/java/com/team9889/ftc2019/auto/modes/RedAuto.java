package com.team9889.ftc2019.auto.modes;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.team9889.ftc2019.auto.AutoModeBase;
import com.team9889.ftc2019.auto.actions.utl.Wait;
import com.team9889.ftc2019.auto.actions.drive.DriveFollowPath;
import com.team9889.ftc2019.auto.actions.drive.DriveToFoundation;
import com.team9889.ftc2019.auto.actions.drive.Foundation;
import com.team9889.ftc2019.auto.actions.foundation.FoundationHookClose;
import com.team9889.ftc2019.auto.actions.intake.Intake;
import com.team9889.ftc2019.auto.actions.intake.IntakeDown;
import com.team9889.ftc2019.auto.actions.intake.IntakeStopBlockIn;
import com.team9889.ftc2019.auto.actions.intake.IntakeStopBlockInWait;
import com.team9889.ftc2019.auto.actions.intake.Outtake;
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
                pose.add(new FollowPath(new Pose2d(4, 0 * Side.getNum(Side_), 0 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 3, .8));
                pose.add(new FollowPath(new Pose2d(26, 0 * Side.getNum(Side_), -40 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 4, .8));
                pose.add(new FollowPath(new Pose2d(42, 0 * Side.getNum(Side_), -40 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 4, .6));
                pose.add(new FollowPath(new Pose2d(42, -5 * Side.getNum(Side_), -40 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 4, .6));
                pose.add(new FollowPath(new Pose2d(26, -5 * Side.getNum(Side_), -40 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 4, .8));
                break;

            case MIDDLE:
                pose.add(new FollowPath(new Pose2d(4, 0 * Side.getNum(Side_), 0 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 3, .8));
                pose.add(new FollowPath(new Pose2d(26, -12 * Side.getNum(Side_), -30 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 4, .8, 1500));
                pose.add(new FollowPath(new Pose2d(40, -12 * Side.getNum(Side_), -30 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 4, .6, 1000));
                pose.add(new FollowPath(new Pose2d(40, -18 * Side.getNum(Side_), -30 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 4, .8, 1000));
                pose.add(new FollowPath(new Pose2d(26, -10 * Side.getNum(Side_), -90 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 3, .8, 1500));
                break;

            case LEFT:
                pose.add(new FollowPath(new Pose2d(4, 0 * Side.getNum(Side_), 0 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 3, .8));
                pose.add(new FollowPath(new Pose2d(26, -15 * Side.getNum(Side_), -45 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 4, .8));
                pose.add(new FollowPath(new Pose2d(42, -15 * Side.getNum(Side_), -45 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 4, .6));
                pose.add(new FollowPath(new Pose2d(42, -20 * Side.getNum(Side_), -45 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 4, .8, 1000));
                pose.add(new FollowPath(new Pose2d(24, -20 * Side.getNum(Side_), -45 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 8.5, .8));
                pose.add(new FollowPath(new Pose2d(24, -20 * Side.getNum(Side_), -90 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 8.5, .8));
                break;
        }
//        pose.add(new FollowPath(new Pose2d(24, 39 * Side.getNum(Side_), -90 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 4, 1));
        pose.add(new FollowPath(new Pose2d(24, 92 * Side.getNum(Side_), -90 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 2, .8));
        pose.add(new FollowPath(new Pose2d(24, 92 * Side.getNum(Side_), -180 * Side.getNum(Side_)), new Pose2d(2, 2, 5), 4, .8, 1000));

        runAction(new DriveFollowPath(pose));
        pose.clear();

        runAction(new Outtake(true));
        runAction(new DriveToFoundation(3000, 180));

        ThreadAction(new LiftWait(0, -1, 200));
        ThreadAction(new LiftWait(1900, 1, 300));
        ThreadAction(new LinearWait(0, true));
        ThreadAction(new OpenGrabberWait(1500, true));
        ThreadAction(new LinearWait(1700, false));
        ThreadAction(new Foundation(false, 65, false));
        runAction(new FoundationHookClose());

        Robot.getIntake().SetIntakePower(1);
        Robot.getIntake().SetRollerPower(.5);
        ThreadAction(new IntakeStopBlockInWait(3000));
        pose.add(new FollowPath(new Pose2d(24, 60 * Side.getNum(Side_), -100 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 4, 1));
        pose.add(new FollowPath(new Pose2d(24, 60 * Side.getNum(Side_), -100 * Side.getNum(Side_)), new Pose2d(1, 2, 3), 4, 1));

        switch (stonePosition){
            case RIGHT:
                pose.add(new FollowPath(new Pose2d(20, 25 * Side.getNum(Side_), -90 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 4, .8, 4000));
                pose.add(new FollowPath(new Pose2d(24, 25 * Side.getNum(Side_), -45 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 4, .6, 2000));
                pose.add(new FollowPath(new Pose2d(40, 25 * Side.getNum(Side_), -45 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 2, .6, 1000));
                pose.add(new FollowPath(new Pose2d(40, 16 * Side.getNum(Side_), -45 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 2, .6, 2000));
                break;

            case MIDDLE:
                pose.add(new FollowPath(new Pose2d(26, 22 * Side.getNum(Side_), -90 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 2, .8, 4000));
                pose.add(new FollowPath(new Pose2d(28, 22 * Side.getNum(Side_), -65 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 2, .8, 1000));
                pose.add(new FollowPath(new Pose2d(40, 22 * Side.getNum(Side_), -65 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 2, .8, 1500));
                pose.add(new FollowPath(new Pose2d(40, 12 * Side.getNum(Side_), -65 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 2, .8, 2000));
                pose.add(new FollowPath(new Pose2d(28, 20 * Side.getNum(Side_), -90 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 2, .8, 1000));
//                pose.add(new FollowPath(new Pose2d(30, 10 * Side.getNum(Side_), -45 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 2, 1.2, 2000));
                break;

            case LEFT:
                pose.add(new FollowPath(new Pose2d(26, 20 * Side.getNum(Side_), -90 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 4, .8));
                pose.add(new FollowPath(new Pose2d(28, 20 * Side.getNum(Side_), -75 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 4, .6));
                pose.add(new FollowPath(new Pose2d(42, 15 * Side.getNum(Side_), -75 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 2, .8));
                pose.add(new FollowPath(new Pose2d(42, 5 * Side.getNum(Side_), -75 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 2, .8, 1000));
                break;
        }
        runAction(new DriveFollowPath(pose));
        pose.clear();

        ThreadAction(new Lift(-1, 400, 60, true));
        ThreadAction(new LiftLinearBar(false, 60, true));
        ThreadAction(new Grabber(true, 600, 60, true));

//        pose.add(new FollowPath(new Pose2d(20, 0 * Side.getNum(Side_), -90 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 2, .8, 2000));

        pose.add(new FollowPath(new Pose2d(24, 45 * Side.getNum(Side_), -90 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 2, .8, 2000));
        pose.add(new FollowPath(new Pose2d(68, 45 * Side.getNum(Side_), -90 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 2, .8, 750));
        runAction(new DriveFollowPath(pose));
        pose.clear();

        //Robot.getMecanumDrive().setCurrentPose(new Pose2d(38.5, Robot.getMecanumDrive().currentPose.getY()));
//        Robot.getMecanumDrive().driftCalc = new Pose2d(0, Robot.getMecanumDrive().driftCalc.getY());

        pose.add(new FollowPath(new Pose2d(36, 65 * Side.getNum(Side_), -90 * Side.getNum(Side_)), new Pose2d(1, 2, 3), 4, .8, 3000));
        pose.add(new FollowPath(new Pose2d(36, 65 * Side.getNum(Side_), -90 * Side.getNum(Side_)), new Pose2d(1, 2, 3), 4, .8, 3000));
        runAction(new DriveFollowPath(pose));
        pose.clear();

        runAction(new Wait(500));

        runAction(new Intake(false));
        ThreadAction(new LinearWait(600, false));
        runAction(new LiftWait(1000, 1, 400));

        ThreadAction(new IntakeStopBlockInWait(500));
        switch (stonePosition){
            case RIGHT:
                pose.add(new FollowPath(new Pose2d(32, -20 * Side.getNum(Side_), -90 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 4, .8, 4000));
                pose.add(new FollowPath(new Pose2d(44, -20 * Side.getNum(Side_), -45 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 4, .8, 1000));
                pose.add(new FollowPath(new Pose2d(44, -35 * Side.getNum(Side_), -45 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 4, .8, 1000));
                pose.add(new FollowPath(new Pose2d(36, -15 * Side.getNum(Side_), -90 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 8.5, .8, 1000));
                break;

            case MIDDLE:
//                pose.add(new FollowPath(new Pose2d(36, 55 * Side.getNum(Side_), -90 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 2, 1));
                pose.add(new FollowPath(new Pose2d(36, 12 * Side.getNum(Side_), -90 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 3, .8));
                pose.add(new FollowPath(new Pose2d(34, 12 * Side.getNum(Side_), 0 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 2, .8));
                pose.add(new FollowPath(new Pose2d(60, 12 * Side.getNum(Side_), 0 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 2, .8, 2000));
                pose.add(new FollowPath(new Pose2d(36, 12 * Side.getNum(Side_), 0 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 4, .8));
                break;

            case LEFT:
//                pose.add(new FollowPath(new Pose2d(36, 55 * Side.getNum(Side_), -90 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 2, 1));
                pose.add(new FollowPath(new Pose2d(36, 15 * Side.getNum(Side_), -90 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 3, .8));
                pose.add(new FollowPath(new Pose2d(34, 15 * Side.getNum(Side_), 0 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 2, .8));
                pose.add(new FollowPath(new Pose2d(60, 15 * Side.getNum(Side_), 0 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 2, .8));
                pose.add(new FollowPath(new Pose2d(34, 15 * Side.getNum(Side_), 0 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 4, .8));
                break;
        }
        runAction(new DriveFollowPath(pose));
        pose.clear();

        ThreadAction(new LiftLinearBar(false, 75, true));
        ThreadAction(new Lift(-1, 600, 65, true));
        ThreadAction(new Grabber(true, 500, 75, true));
//        pose.add(new FollowPath(new Pose2d(30, 50 * Side.getNum(Side_), -90 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 2, .8, 3000));
//        pose.add(new FollowPath(new Pose2d(30, 60 * Side.getNum(Side_), -90 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 4, 1, 1000));
        pose.add(new FollowPath(new Pose2d(34, 100 * Side.getNum(Side_), -90 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 4, 1, 3000));
        pose.add(new FollowPath(new Pose2d(34, 100 * Side.getNum(Side_), -90 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 4, 1, 3000));
        runAction(new DriveFollowPath(pose));
        pose.clear();

        runAction(new Wait(700));

        ThreadAction(new LiftWait(100, 1, 700));
//        ThreadAction(new LiftLinearBar(true, -75, true));
        pose.add(new FollowPath(new Pose2d(36, 40 * Side.getNum(Side_), -90 * Side.getNum(Side_)), new Pose2d(1, 1, 3), 2, 1));
        pose.add(new FollowPath(new Pose2d(36, 40 * Side.getNum(Side_), -90 * Side.getNum(Side_)), new Pose2d(1, 1, 3), 4, 1));
        runAction(new DriveFollowPath(pose));
        pose.clear();

//        pose.add(new FollowPath(new Pose2d(0, 0 * Side.getNum(Side_), 0 * Side.getNum(Side_)), new Pose2d(2, 2, 3), 4, 1));
    }
}
