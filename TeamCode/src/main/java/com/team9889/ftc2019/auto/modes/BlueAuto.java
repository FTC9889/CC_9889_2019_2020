package com.team9889.ftc2019.auto.modes;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.team9889.ftc2019.auto.AutoModeBase;
import com.team9889.ftc2019.auto.actions.Wait;
import com.team9889.ftc2019.auto.actions.drive.Drive3DimensionalPID;
import com.team9889.ftc2019.auto.actions.drive.DriveFollowPath;
import com.team9889.ftc2019.auto.actions.drive.DriveToFoundation;
import com.team9889.ftc2019.auto.actions.foundation.FoundationHookClose;
import com.team9889.ftc2019.auto.actions.foundation.FoundationHookOpen;
import com.team9889.ftc2019.auto.actions.intake.Intake;
import com.team9889.ftc2019.auto.actions.intake.IntakeDown;
import com.team9889.ftc2019.auto.actions.intake.IntakeRollerOn;
import com.team9889.ftc2019.auto.actions.intake.IntakeRollerStop;
import com.team9889.ftc2019.auto.actions.intake.IntakeStopBlockIn;
import com.team9889.ftc2019.auto.actions.lift.CloseGrabber;
import com.team9889.ftc2019.auto.actions.lift.LiftIn;
import com.team9889.ftc2019.auto.actions.lift.LiftOut;
import com.team9889.ftc2019.auto.actions.lift.LiftUp;
import com.team9889.ftc2019.auto.actions.lift.OpenGrabber;
import com.team9889.lib.FollowPath;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eric on 11/26/2019.
 */

@Autonomous
public class BlueAuto extends AutoModeBase {

    @Override
    public void run(AutoModeBase.Side side, AutoModeBase.SkyStonePosition stonePosition) {
        if (positionOfSkyStone < 110)
            currentSkyStonePosition = SkyStonePosition.RIGHT;
        else if (positionOfSkyStone > 109 && positionOfSkyStone < 200)
            currentSkyStonePosition = SkyStonePosition.MIDDLE;
        else if (positionOfSkyStone > 200)
            currentSkyStonePosition = SkyStonePosition.LEFT;

        stonePosition = currentSkyStonePosition;

            Side Side_ = Side.BLUE;
            List<FollowPath> pose = new ArrayList<>();
            Robot.redAuto = false;


            Robot.grabber.setPosition(1);
            ThreadAction(new IntakeDown());
            ThreadAction(new IntakeStopBlockIn());

            //pick up first skystone
            switch (stonePosition){
                case RIGHT:
                    pose.add(new FollowPath(new Pose2d(7, 0 * Side.getNum(Side_), Math.toRadians(0 * Side.getNum(Side_))), new Pose2d(2, 2, 3), 3, .5));
                    pose.add(new FollowPath(new Pose2d(36, -2 * Side.getNum(Side_), Math.toRadians(35 * Side.getNum(Side_))), new Pose2d(2, 2, 3), 3, .7));
                    pose.add(new FollowPath(new Pose2d(30, 0 * Side.getNum(Side_), Math.toRadians(35 * Side.getNum(Side_))), new Pose2d(2, 2, 3), 3, .5));
                    runAction(new DriveFollowPath(pose));
                    pose.clear();
                    break;

                case MIDDLE:
                    pose.add(new FollowPath(new Pose2d(34, 0 * Side.getNum(Side_), Math.toRadians(10 * Side.getNum(Side_))), new Pose2d(2, 2, 3), 3, .7));
                    pose.add(new FollowPath(new Pose2d(34, 0 * Side.getNum(Side_), Math.toRadians(25 * Side.getNum(Side_))), new Pose2d(2, 2, 3), 3, .7));
                    pose.add(new FollowPath(new Pose2d(30, 0 * Side.getNum(Side_), Math.toRadians(25 * Side.getNum(Side_))), new Pose2d(2, 2, 3), 3, .5));
                    runAction(new DriveFollowPath(pose));
                    pose.clear();
                    break;

                case LEFT:
                    pose.add(new FollowPath(new Pose2d(34, -3 * Side.getNum(Side_), Math.toRadians(0 * Side.getNum(Side_))), new Pose2d(2, 2, 3), 3, .7));
                    pose.add(new FollowPath(new Pose2d(34, -3 * Side.getNum(Side_), Math.toRadians(-35 * Side.getNum(Side_))), new Pose2d(2, 2, 3), 3, .7));
                    pose.add(new FollowPath(new Pose2d(30, 0 * Side.getNum(Side_), Math.toRadians(-35 * Side.getNum(Side_))), new Pose2d(2, 2, 3), 3, .5));
                    runAction(new DriveFollowPath(pose));
                    pose.clear();
                    break;
            }

            pose.add(new FollowPath(new Pose2d(26, 60 * Side.getNum(Side_), Math.toRadians(90 * Side.getNum(Side_))), new Pose2d(2, 2, 3), 4, 1));
            pose.add(new FollowPath(new Pose2d(26, 80 * Side.getNum(Side_), Math.toRadians(90 * Side.getNum(Side_))), new Pose2d(2, 2, 3), 7.5, .8));
            pose.add(new FollowPath(new Pose2d(26, 80 * Side.getNum(Side_), Math.toRadians(180 * Side.getNum(Side_))), new Pose2d(2, 2, 3), 7.5, .5));
            runAction(new DriveFollowPath(pose));
            pose.clear();

            Robot.getIntake().SetIntakePower(0);
            runAction(new DriveToFoundation(2000, 180 * Side.getNum(Side_)));
            runAction(new FoundationHookClose());

            ThreadAction(new LiftUp(250, -.4));
            ThreadAction(new LiftOut());

            pose.add(new FollowPath(new Pose2d(25, 50 * Side.getNum(Side_), Math.toRadians(-90 * Side.getNum(Side_))), new Pose2d(2, 2, 1), 7.5, .8));
            pose.add(new FollowPath(new Pose2d(25, 50 * Side.getNum(Side_), Math.toRadians(-90 * Side.getNum(Side_))), new Pose2d(2, 2, 1), 3, .5));
            runAction(new DriveFollowPath(pose));
            pose.clear();

            Robot.grabber.setPosition(1);
            runAction(new FoundationHookOpen());
            ThreadAction(new IntakeStopBlockIn());

            ThreadAction(new LiftIn());
            ThreadAction(new LiftUp(250, 1));

            //grab second skystone
            switch (stonePosition){
                case RIGHT:
                    pose.add(new FollowPath(new Pose2d(30, 5 * Side.getNum(Side_), Math.toRadians(-90 * Side.getNum(Side_))), new Pose2d(2, 2, 3), 7.5, .8));
                    pose.add(new FollowPath(new Pose2d(45, 0 * Side.getNum(Side_), Math.toRadians(-45 * Side.getNum(Side_))), new Pose2d(2, 2, 3), 3, .8));
                    pose.add(new FollowPath(new Pose2d(30, 0 * Side.getNum(Side_), Math.toRadians(-90 * Side.getNum(Side_))), new Pose2d(2, 2, 3), 3, .5));
                    runAction(new DriveFollowPath(pose));
                    pose.clear();
                    break;

                case MIDDLE:
                    pose.add(new FollowPath(new Pose2d(30, 0 * Side.getNum(Side_), Math.toRadians(-90 * Side.getNum(Side_))), new Pose2d(2, 2, 3), 3, .8));
                    pose.add(new FollowPath(new Pose2d(45, -6 * Side.getNum(Side_), Math.toRadians(-35 * Side.getNum(Side_))), new Pose2d(2, 2, 3), 3, .8));
                    pose.add(new FollowPath(new Pose2d(30, -6 * Side.getNum(Side_), Math.toRadians(-90 * Side.getNum(Side_))), new Pose2d(2, 2, 3), 3, .5));
                    runAction(new DriveFollowPath(pose));
                    pose.clear();
                    break;

                case LEFT:
                    pose.add(new FollowPath(new Pose2d(30, -5 * Side.getNum(Side_), Math.toRadians(-90 * Side.getNum(Side_))), new Pose2d(2, 2, 3), 7.5, .8));
                    pose.add(new FollowPath(new Pose2d(30, -5 * Side.getNum(Side_), Math.toRadians(-65 * Side.getNum(Side_))), new Pose2d(2, 2, 3), 7.5, .8));
                    pose.add(new FollowPath(new Pose2d(45, -15 * Side.getNum(Side_), Math.toRadians(-65 * Side.getNum(Side_))), new Pose2d(2, 2, 3), 3, .8));
                    pose.add(new FollowPath(new Pose2d(30, -15 * Side.getNum(Side_), Math.toRadians(-90 * Side.getNum(Side_))), new Pose2d(2, 2, 3), 3, .5));
                    runAction(new DriveFollowPath(pose));
                    pose.clear();
                    break;
            }

            pose.add(new FollowPath(new Pose2d(24, 45 * Side.getNum(Side_), Math.toRadians(-90 * Side.getNum(Side_))), new Pose2d(2, 2, 3), 7, .7));
            pose.add(new FollowPath(new Pose2d(24, 45 * Side.getNum(Side_), Math.toRadians(-90 * Side.getNum(Side_))), new Pose2d(2, 2, 3), 4, .5));
            runAction(new DriveFollowPath(pose));
            pose.clear();

            Robot.getIntake().SetIntakePower(0);
            ThreadAction(new LiftUp(500, -.8));
            ThreadAction(new LiftOut());
            runAction(new DriveToFoundation(2000, -90 * Side.getNum(Side_)));

            runAction(new FoundationHookOpen());

            runAction(new Wait(250));

            Robot.grabber.setPosition(1);
            runAction(new Wait(250));

            runAction(new LiftIn());
            Robot.getLift().SetLiftPower(1);
            runAction(new Wait(250));
            Robot.getLift().SetLiftPower(0);

            ThreadAction(new IntakeStopBlockIn());
            //grab third skystone
            switch (stonePosition){
                case RIGHT:
                case MIDDLE:
                    pose.add(new FollowPath(new Pose2d(30, -5 * Side.getNum(Side_), Math.toRadians(-90 * Side.getNum(Side_))), new Pose2d(2, 2, 3), 7.5, .8));
                    pose.add(new FollowPath(new Pose2d(30, -5 * Side.getNum(Side_), Math.toRadians(-75 * Side.getNum(Side_))), new Pose2d(2, 2, 3), 7.5, .8));
                    pose.add(new FollowPath(new Pose2d(45, -15 * Side.getNum(Side_), Math.toRadians(-75 * Side.getNum(Side_))), new Pose2d(2, 2, 3), 4, .8));
                    pose.add(new FollowPath(new Pose2d(30, -15 * Side.getNum(Side_), Math.toRadians(-90 * Side.getNum(Side_))), new Pose2d(2, 2, 3), 3, .5));
                    runAction(new DriveFollowPath(pose));
                    pose.clear();
                    break;

                case LEFT:
                    pose.add(new FollowPath(new Pose2d(30, 30 * Side.getNum(Side_), Math.toRadians(-90 * Side.getNum(Side_))), new Pose2d(2, 2, 3), 7.5, .8));
                    pose.add(new FollowPath(new Pose2d(30, 20 * Side.getNum(Side_), Math.toRadians(0 * Side.getNum(Side_))), new Pose2d(2, 2, 3), 7.5, .8));
                    pose.add(new FollowPath(new Pose2d(40, 20 * Side.getNum(Side_), Math.toRadians(0 * Side.getNum(Side_))), new Pose2d(2, 2, 3), 3, .8));
                    pose.add(new FollowPath(new Pose2d(30, 17 * Side.getNum(Side_), Math.toRadians(-90 * Side.getNum(Side_))), new Pose2d(2, 2, 3), 3, .5));
                    runAction(new DriveFollowPath(pose));
                    pose.clear();
                    break;
            }

            pose.add(new FollowPath(new Pose2d(22, 45 * Side.getNum(Side_), Math.toRadians(-90 * Side.getNum(Side_))), new Pose2d(2, 2, 3), 4, .7));
            pose.add(new FollowPath(new Pose2d(22, 45 * Side.getNum(Side_), Math.toRadians(-90 * Side.getNum(Side_))), new Pose2d(2, 2, 3), 4, .5));
            runAction(new DriveFollowPath(pose));
            pose.clear();

            Robot.getIntake().SetIntakePower(0);
            runAction(new LiftUp(250, -1));
            ThreadAction(new LiftOut());

            pose.add(new FollowPath(new Pose2d(22, 75 * Side.getNum(Side_), Math.toRadians(-90 * Side.getNum(Side_))), new Pose2d(2, 2, 3), 8, 1));
            pose.add(new FollowPath(new Pose2d(22, 75 * Side.getNum(Side_), Math.toRadians(-90 * Side.getNum(Side_))), new Pose2d(4, 4, 3), 8, 1));
            runAction(new DriveFollowPath(pose));
            pose.clear();

            Robot.grabber.setPosition(1);
            runAction(new Wait(250));

            runAction(new LiftIn());
            ThreadAction(new LiftUp(2000, .3));

            pose.add(new FollowPath(new Pose2d(28, 36 * Side.getNum(Side_), Math.toRadians(-90 * Side.getNum(Side_))), new Pose2d(2, 2, 3), 8, .7));
            pose.add(new FollowPath(new Pose2d(28, 36 * Side.getNum(Side_), Math.toRadians(-90 * Side.getNum(Side_))), new Pose2d(2, 2, 3), 8, .7));
            runAction(new DriveFollowPath(pose));
            pose.clear();

        /*
        List<FollowPath> pose = new ArrayList<>();


        Robot.grabber.setPosition(1);
        ThreadAction(new IntakeDown());
        ThreadAction(new IntakeStopBlockIn());

        //pick up first skystone
        switch (stonePosition){
            case RIGHT:
                pose.add(new FollowPath(new Pose2d(7, 0 * Side.getNum(Side_), Math.toRadians(0) * Side.getNum(Side_)), new Pose2d(2, 2, 3), 3, .5));
                pose.add(new FollowPath(new Pose2d(36, -2 * Side.getNum(Side_), Math.toRadians(35) * Side.getNum(Side_)), new Pose2d(2, 2, 3), 3, .7));
                pose.add(new FollowPath(new Pose2d(30, 0 * Side.getNum(Side_), Math.toRadians(35) * Side.getNum(Side_)), new Pose2d(2, 2, 3), 3, .5));
                runAction(new DriveFollowPath(pose));
                pose.clear();
                break;

            case MIDDLE:
                pose.add(new FollowPath(new Pose2d(34, 1 * Side.getNum(Side_), Math.toRadians(0) * Side.getNum(Side_)), new Pose2d(2, 2, 3), 3, .7));
                pose.add(new FollowPath(new Pose2d(34, 1 * Side.getNum(Side_), Math.toRadians(25) * Side.getNum(Side_)), new Pose2d(2, 2, 3), 3, .7));
                pose.add(new FollowPath(new Pose2d(30, 0 * Side.getNum(Side_), Math.toRadians(25) * Side.getNum(Side_)), new Pose2d(2, 2, 3), 3, .5));
                runAction(new DriveFollowPath(pose));
                pose.clear();
                break;

            case LEFT:
                pose.add(new FollowPath(new Pose2d(34, -3 * Side.getNum(Side_), Math.toRadians(0) * Side.getNum(Side_)), new Pose2d(2, 2, 3), 3, .7));
                pose.add(new FollowPath(new Pose2d(34, -3 * Side.getNum(Side_), Math.toRadians(-35) * Side.getNum(Side_)), new Pose2d(2, 2, 3), 3, .7));
                pose.add(new FollowPath(new Pose2d(30, 0 * Side.getNum(Side_), Math.toRadians(-35) * Side.getNum(Side_)), new Pose2d(2, 2, 3), 3, .5));
                runAction(new DriveFollowPath(pose));
                pose.clear();
                break;
        }

        pose.add(new FollowPath(new Pose2d(30, 60 * Side.getNum(Side_), Math.toRadians(90) * Side.getNum(Side_)), new Pose2d(2, 2, 3), 4, 1));
        pose.add(new FollowPath(new Pose2d(34, 76 * Side.getNum(Side_), Math.toRadians(90) * Side.getNum(Side_)), new Pose2d(2, 2, 3), 7.5, .8));
        pose.add(new FollowPath(new Pose2d(34, 76 * Side.getNum(Side_), Math.toRadians(180) * Side.getNum(Side_)), new Pose2d(2, 2, 3), 7.5, .5));
        runAction(new DriveFollowPath(pose));
        pose.clear();

        runAction(new DriveToFoundation(2000, 180 * Side.getNum(Side_)));
        runAction(new FoundationHookClose());

        ThreadAction(new LiftUp(250, -.4));
        ThreadAction(new LiftOut());

        pose.add(new FollowPath(new Pose2d(30, 50 * Side.getNum(Side_), Math.toRadians(-90) * Side.getNum(Side_)), new Pose2d(2, 2, 1), 7.5, .8));
        pose.add(new FollowPath(new Pose2d(30, 50 * Side.getNum(Side_), Math.toRadians(-90) * Side.getNum(Side_)), new Pose2d(2, 2, 1), 3, .5));
        runAction(new DriveFollowPath(pose));
        pose.clear();

        Robot.grabber.setPosition(1);
        runAction(new FoundationHookOpen());
        ThreadAction(new IntakeStopBlockIn());

        ThreadAction(new LiftIn());
        ThreadAction(new LiftUp(250, 1));

        //grab second skystone
        switch (stonePosition){
            case RIGHT:
                pose.add(new FollowPath(new Pose2d(30, 5 * Side.getNum(Side_), Math.toRadians(-90) * Side.getNum(Side_)), new Pose2d(2, 2, 3), 7.5, .8));
                pose.add(new FollowPath(new Pose2d(45, -7 * Side.getNum(Side_), Math.toRadians(-45) * Side.getNum(Side_)), new Pose2d(2, 2, 3), 3, .8));
                pose.add(new FollowPath(new Pose2d(30, -10 * Side.getNum(Side_), Math.toRadians(-90) * Side.getNum(Side_)), new Pose2d(2, 2, 3), 3, .5));
                runAction(new DriveFollowPath(pose));
                pose.clear();
                break;

            case MIDDLE:
                pose.add(new FollowPath(new Pose2d(30, 0 * Side.getNum(Side_), Math.toRadians(-90) * Side.getNum(Side_)), new Pose2d(2, 2, 3), 3, .8));
                pose.add(new FollowPath(new Pose2d(45, -7 * Side.getNum(Side_), Math.toRadians(-35) * Side.getNum(Side_)), new Pose2d(2, 2, 3), 3, .8));
                pose.add(new FollowPath(new Pose2d(30, -7 * Side.getNum(Side_), Math.toRadians(-35) * Side.getNum(Side_)), new Pose2d(2, 2, 3), 3, .5));
                runAction(new DriveFollowPath(pose));
                pose.clear();
                break;

            case LEFT:
                pose.add(new FollowPath(new Pose2d(30, -5 * Side.getNum(Side_), Math.toRadians(-90) * Side.getNum(Side_)), new Pose2d(2, 2, 3), 7.5, .8));
                pose.add(new FollowPath(new Pose2d(30, -5 * Side.getNum(Side_), Math.toRadians(-65) * Side.getNum(Side_)), new Pose2d(2, 2, 3), 7.5, .8));
                pose.add(new FollowPath(new Pose2d(45, -25 * Side.getNum(Side_), Math.toRadians(-65) * Side.getNum(Side_)), new Pose2d(2, 2, 3), 3, .8));
                pose.add(new FollowPath(new Pose2d(30, -22 * Side.getNum(Side_), Math.toRadians(-90) * Side.getNum(Side_)), new Pose2d(2, 2, 3), 3, .5));
                runAction(new DriveFollowPath(pose));
                pose.clear();
                break;
        }

        pose.add(new FollowPath(new Pose2d(29, 45 * Side.getNum(Side_), Math.toRadians(-90) * Side.getNum(Side_)), new Pose2d(2, 2, 3), 7, .7));
        pose.add(new FollowPath(new Pose2d(29, 45 * Side.getNum(Side_), Math.toRadians(-90) * Side.getNum(Side_)), new Pose2d(2, 2, 3), 4, .5));
        runAction(new DriveFollowPath(pose));
        pose.clear();

        ThreadAction(new LiftUp(250, -.8));
        ThreadAction(new LiftOut());
        runAction(new DriveToFoundation(2000, -90 * Side.getNum(Side_)));

        runAction(new FoundationHookOpen());

        runAction(new Wait(500));

        Robot.grabber.setPosition(1);
        runAction(new Wait(250));

        runAction(new LiftIn());
        Robot.getLift().SetLiftPower(.6);
        runAction(new Wait(250));
        Robot.getLift().SetLiftPower(0);

        ThreadAction(new IntakeStopBlockIn());
        //grab third skystone
        switch (stonePosition){
            case RIGHT:
            case MIDDLE:
                pose.add(new FollowPath(new Pose2d(30, -5 * Side.getNum(Side_), Math.toRadians(-90) * Side.getNum(Side_)), new Pose2d(2, 2, 3), 7.5, .8));
                pose.add(new FollowPath(new Pose2d(30, -5 * Side.getNum(Side_), Math.toRadians(-65) * Side.getNum(Side_)), new Pose2d(2, 2, 3), 7.5, .8));
                pose.add(new FollowPath(new Pose2d(45, -25 * Side.getNum(Side_), Math.toRadians(-65) * Side.getNum(Side_)), new Pose2d(2, 2, 3), 3, .8));
                pose.add(new FollowPath(new Pose2d(30, -22 * Side.getNum(Side_), Math.toRadians(-90) * Side.getNum(Side_)), new Pose2d(2, 2, 3), 3, .5));
                runAction(new DriveFollowPath(pose));
                pose.clear();
                break;

            case LEFT:
                pose.add(new FollowPath(new Pose2d(30, 15 * Side.getNum(Side_), Math.toRadians(-90) * Side.getNum(Side_)), new Pose2d(2, 2, 3), 7.5, .8));
                pose.add(new FollowPath(new Pose2d(30, 15 * Side.getNum(Side_), Math.toRadians(0) * Side.getNum(Side_)), new Pose2d(2, 2, 3), 7.5, .8));
                pose.add(new FollowPath(new Pose2d(45, 15 * Side.getNum(Side_), Math.toRadians(0) * Side.getNum(Side_)), new Pose2d(2, 2, 3), 3, .8));
                pose.add(new FollowPath(new Pose2d(30, 15 * Side.getNum(Side_), Math.toRadians(-90) * Side.getNum(Side_)), new Pose2d(2, 2, 3), 3, .5));
                runAction(new DriveFollowPath(pose));
                pose.clear();
                break;
        }

        pose.add(new FollowPath(new Pose2d(32, 45 * Side.getNum(Side_), Math.toRadians(-90) * Side.getNum(Side_)), new Pose2d(2, 2, 3), 4, .7));
        pose.add(new FollowPath(new Pose2d(32, 45 * Side.getNum(Side_), Math.toRadians(-90) * Side.getNum(Side_)), new Pose2d(2, 2, 3), 4, .5));
        runAction(new DriveFollowPath(pose));
        pose.clear();

        runAction(new LiftUp(250, -1));
        ThreadAction(new LiftOut());

        pose.add(new FollowPath(new Pose2d(32, 75 * Side.getNum(Side_), Math.toRadians(-90) * Side.getNum(Side_)), new Pose2d(2, 2, 3), 8, 1));
        pose.add(new FollowPath(new Pose2d(32, 75 * Side.getNum(Side_), Math.toRadians(-90) * Side.getNum(Side_)), new Pose2d(4, 4, 3), 8, 1));
        runAction(new DriveFollowPath(pose));
        pose.clear();

        Robot.grabber.setPosition(1);
        runAction(new Wait(250));

        runAction(new LiftIn());
        ThreadAction(new LiftUp(2000, .3));

        pose.add(new FollowPath(new Pose2d(34, 36 * Side.getNum(Side_), Math.toRadians(-90) * Side.getNum(Side_)), new Pose2d(2, 2, 3), 8, .7));
        pose.add(new FollowPath(new Pose2d(34, 36 * Side.getNum(Side_), Math.toRadians(-90) * Side.getNum(Side_)), new Pose2d(2, 2, 3), 8, .7));
        runAction(new DriveFollowPath(pose));
        pose.clear();
        */
        }
}
