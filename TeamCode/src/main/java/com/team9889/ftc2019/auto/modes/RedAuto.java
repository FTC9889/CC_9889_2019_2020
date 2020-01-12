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

import org.opencv.core.Mat;

/**
 * Created by Eric on 11/26/2019.
 */

@Autonomous
public class RedAuto extends AutoModeBase {

    @Override
    public void run(Side side, AutoModeBase.SkyStonePosition stonePosition) {
        Side Side_ = Side.RED;

        ThreadAction(new IntakeDown());
//        runAction(new Drive3DimensionalPID(new Pose2d(4, 0, 0)));
        ThreadAction(new Intake());

        //pick up first skystone
        switch (stonePosition){
            case RIGHT:
                runAction(new Drive3DimensionalPID(new Pose2d(33, -2 * Side.getNum(Side_), Math.toRadians(-30 * Side.getNum(Side_)))));
                runAction(new Drive3DimensionalPID(new Pose2d(25, -5 * Side.getNum(Side_), Math.toRadians(-90 * Side.getNum(Side_)))));
                runAction(new Intake(false));

                runAction(new Wait(500));
                runAction(new Intake(true, true));
                ThreadAction(new IntakeStopBlockIn());
                break;

            case MIDDLE:
                runAction(new Drive3DimensionalPID(new Pose2d(36, -9 * Side.getNum(Side_), Math.toRadians(-35 * Side.getNum(Side_)))));
                runAction(new Drive3DimensionalPID(new Pose2d(25, -8 * Side.getNum(Side_), Math.toRadians(-90 * Side.getNum(Side_)))));
                runAction(new Intake(false));

                runAction(new Wait(500));
                runAction(new Intake(true, true));
                ThreadAction(new IntakeStopBlockIn());
                break;

            case LEFT:
                runAction(new DriveFollowPath(new Pose2d[]{
                        new Pose2d(4, 0, 0),
                        new Pose2d(33, -16, Math.toRadians(-35)),
                        new Pose2d(25, -17, Math.toRadians(-90))
                }, new double[]{
                        2,
                        2,
                        6
                }));

//                runAction(new Drive3DimensionalPID(new Pose2d(33, -16 * Side.getNum(Side_), Math.toRadians(-35 * Side.getNum(Side_)))));
//                runAction(new Drive3DimensionalPID(new Pose2d(25, -17 * Side.getNum(Side_), Math.toRadians(-90 * Side.getNum(Side_)))));
                runAction(new Intake(false));

                runAction(new Wait(500));
                runAction(new Intake(true, true));
                ThreadAction(new IntakeStopBlockIn());
                break;
        }

        //drive to foundation
//        runAction(new Drive3DimensionalPID(new Pose2d(25, 85 * Side.getNum(Side_), Math.toRadians(-90 * Side.getNum(Side_))), .8));
//        runAction(new Wait(200));
//        runAction(new Drive3DimensionalPID(new Pose2d(25, 85 * Side.getNum(Side_), Math.toRadians(180 * Side.getNum(Side_)))));

//        runAction(new DriveFollowPath(new Pose2d[]{
//                 new Pose2d(25, 85, Math.toRadians( -90)),
//                 new Pose2d(25, 85, Math.toRadians( 180))
//        }, .8));

        runAction(new CloseGrabber());
        runAction(new DriveToFoundation(3000));

        //score skystone and pull foundation to bridge
        runAction(new FoundationHookClose());

        // Reset Pose
        Robot.getMecanumDrive().setCurrentPose(
                new Pose2d(31, Robot.getMecanumDrive().currentPose.getY(),
                        Robot.getMecanumDrive().currentPose.getHeading()));

        runAction(new IntakeRollerOn());
        ThreadAction(new LiftOut());

        runAction(new Drive3DimensionalPID(new Pose2d(20, 56 * Side.getNum(Side_), Math.toRadians(-90 * Side.getNum(Side_))), .8));

        runAction(new Drive3DimensionalPID(new Pose2d(23, 56 * Side.getNum(Side_), Math.toRadians(-90 * Side.getNum(Side_))),new Pose2d(3, 2, 3), .8));
        runAction(new IntakeRollerStop());
        ThreadAction(new OpenGrabber());
        ThreadAction(new FoundationHookOpen());
        Pose2d rememberPoseScoringBlock = Robot.getMecanumDrive().currentPose;

        runAction(new Wait(500));

        ThreadAction(new LiftIn());

        //grab second skystone
        switch (stonePosition){
            case RIGHT:
                runAction(new Drive3DimensionalPID(new Pose2d(20, 35 * Side.getNum(Side_), Math.toRadians(-90 * Side.getNum(Side_))), .8));

                runAction(new Intake(true));

                runAction(new Drive3DimensionalPID(new Pose2d(20, 28 * Side.getNum(Side_), Math.toRadians(-40 * Side.getNum(Side_)))));
                runAction(new Drive3DimensionalPID(new Pose2d(29, 19 * Side.getNum(Side_), Math.toRadians(-40 * Side.getNum(Side_)))));
                runAction(new Drive3DimensionalPID(new Pose2d(23, 20 * Side.getNum(Side_), Math.toRadians(-90 * Side.getNum(Side_)))));

                runAction(new Intake(false));
                runAction(new Wait(500));
                runAction(new Intake(true, true));
                ThreadAction(new IntakeStopBlockIn());
                break;

            case MIDDLE:
                runAction(new Drive3DimensionalPID(new Pose2d(20, 16 * Side.getNum(Side_), Math.toRadians(-90 * Side.getNum(Side_))), .8));

                runAction(new Intake(true));

                runAction(new Drive3DimensionalPID(new Pose2d(20, 16 * Side.getNum(Side_), Math.toRadians(-40 * Side.getNum(Side_)))));
                runAction(new Drive3DimensionalPID(new Pose2d(30, 15 * Side.getNum(Side_), Math.toRadians(-40 * Side.getNum(Side_)))));
                runAction(new Drive3DimensionalPID(new Pose2d(23, 20 * Side.getNum(Side_), Math.toRadians(-90 * Side.getNum(Side_)))));

                runAction(new Intake(false));
                runAction(new Wait(500));
                runAction(new Intake(true, true));
                ThreadAction(new IntakeStopBlockIn());
                break;

            case LEFT:
                runAction(new Drive3DimensionalPID(new Pose2d(20, 15 * Side.getNum(Side_), Math.toRadians(-90 * Side.getNum(Side_))), .8));

                runAction(new Intake(true));

                runAction(new DriveFollowPath(new Pose2d[]{
                    new Pose2d(20, 10 * Side.getNum(Side_), Math.toRadians(-40 * Side.getNum(Side_))),
                    new Pose2d(33, 5 * Side.getNum(Side_), Math.toRadians(-40 * Side.getNum(Side_))),
                    new Pose2d(20, 20 * Side.getNum(Side_), Math.toRadians(-90 * Side.getNum(Side_)))
                }, new double[]{
                      5,
                      2,
                      5
                }));

                runAction(new Intake(false));
                runAction(new Wait(500));
                runAction(new Intake(true, true));
                ThreadAction(new IntakeStopBlockIn());
                break;
        }

        //score second skystone
        runAction(new Drive3DimensionalPID(
                new Pose2d(rememberPoseScoringBlock.getX(),
                        (rememberPoseScoringBlock.getY() + 2), Math.toRadians(-90 * Side.getNum(Side_))),
                new Pose2d(1.4, 1.4, Math.toRadians(2))));

        runAction(new Wait(500));
        runAction(new CloseGrabber());
        runAction(new Wait(250));

        runAction(new LiftUp(14));
        runAction(new LiftOut());
        runAction(new Wait(1500));

        Robot.getLift().SetLiftPower(.1);
        runAction(new Wait(500));
        Robot.getLift().SetLiftPower(0);

        runAction(new OpenGrabber());
        ThreadAction(new LiftUp(20));
        runAction(new LiftIn());
        runAction(new Drive3DimensionalPID(new Pose2d(rememberPoseScoringBlock.getX(), 80 * Side.getNum(Side_), Math.toRadians(-90 * Side.getNum(Side_))), 1));
        Robot.getLift().SetLiftPower(.5);

        if (autoTimer.milliseconds() > 29000)
            runAction(new Drive3DimensionalPID(new Pose2d(rememberPoseScoringBlock.getX(), 60 * Side.getNum(Side_), Math.toRadians(-90 * Side.getNum(Side_))), 1));
        else
            runAction(new Drive3DimensionalPID(new Pose2d(rememberPoseScoringBlock.getX(), 45 * Side.getNum(Side_), Math.toRadians(-90 * Side.getNum(Side_))), 1));
    }
}
