package com.team9889.ftc2019.auto.modes;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.team9889.ftc2019.auto.AutoModeBase;
import com.team9889.ftc2019.auto.actions.Wait;
import com.team9889.ftc2019.auto.actions.drive.Drive3DimensionalPID;
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

/**
 * Created by Eric on 11/26/2019.
 */

@Autonomous
public class BlueAuto extends AutoModeBase {

    @Override
    public void run(AutoModeBase.Side side, AutoModeBase.SkyStonePosition stonePosition) {
        if (positionOfSkyStone < 110)
            currentSkyStonePosition = SkyStonePosition.LEFT;
        else if (positionOfSkyStone > 109 && positionOfSkyStone < 200)
            currentSkyStonePosition = SkyStonePosition.MIDDLE;
        else if (positionOfSkyStone > 200)
            currentSkyStonePosition = SkyStonePosition.RIGHT;

        stonePosition = currentSkyStonePosition;

        Robot.getMecanumDrive().setCurrentPose(new Pose2d(0, 0, 0));

        AutoModeBase.Side Side_ = AutoModeBase.Side.BLUE;

        ThreadAction(new IntakeDown());
        runAction(new Drive3DimensionalPID(new Pose2d(4, 0, 0)));
        ThreadAction(new Intake());

        // Pick up first Skystone
        switch (stonePosition){
            case RIGHT:
                runAction(new Drive3DimensionalPID(new Pose2d(33, -16 * Side.getNum(Side_), Math.toRadians(-35 * Side.getNum(Side_)))));
                runAction(new Drive3DimensionalPID(new Pose2d(25, -17 * Side.getNum(Side_), Math.toRadians(-90 * Side.getNum(Side_)))));
                runAction(new Intake(false));

                runAction(new Wait(500));
                runAction(new Intake(true, true));
                ThreadAction(new IntakeStopBlockIn());
                break;

            case MIDDLE:
                runAction(new Drive3DimensionalPID(new Pose2d(36, -8 * Side.getNum(Side_), Math.toRadians(-35 * Side.getNum(Side_)))));
                runAction(new Drive3DimensionalPID(new Pose2d(25, -8 * Side.getNum(Side_), Math.toRadians(-90 * Side.getNum(Side_)))));
                runAction(new Intake(false));

                runAction(new Wait(500));
                runAction(new Intake(true, true));
                ThreadAction(new IntakeStopBlockIn());
                break;

            case LEFT:
                runAction(new Drive3DimensionalPID(new Pose2d(33, -2 * Side.getNum(Side_), Math.toRadians(-30 * Side.getNum(Side_)))));
                runAction(new Drive3DimensionalPID(new Pose2d(25, -5 * Side.getNum(Side_), Math.toRadians(-90 * Side.getNum(Side_)))));
                runAction(new Intake(false));

                runAction(new Wait(500));
                runAction(new Intake(true, true));
                ThreadAction(new IntakeStopBlockIn());
                break;
        }

        // Drive to foundation
        runAction(new Drive3DimensionalPID(new Pose2d(25, 85 * Side.getNum(Side_), Math.toRadians(-90 * Side.getNum(Side_))), .8));
        runAction(new Wait(200));
        runAction(new Drive3DimensionalPID(new Pose2d(25, 85 * Side.getNum(Side_), Math.toRadians(180 * Side.getNum(Side_)))));

        runAction(new CloseGrabber());
        runAction(new DriveToFoundation(3000));

        // Score Skystone and pull foundation to bridge
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
                runAction(new Drive3DimensionalPID(new Pose2d(20, 15 * Side.getNum(Side_), Math.toRadians(-90 * Side.getNum(Side_))), .8));

                runAction(new Intake(true));

                runAction(new Drive3DimensionalPID(new Pose2d(20, 18 * Side.getNum(Side_), Math.toRadians(-40 * Side.getNum(Side_)))));
                runAction(new Drive3DimensionalPID(new Pose2d(33, 8 * Side.getNum(Side_), Math.toRadians(-40 * Side.getNum(Side_)))));
                runAction(new Drive3DimensionalPID(new Pose2d(20, 20 * Side.getNum(Side_), Math.toRadians(-90 * Side.getNum(Side_)))));

                runAction(new Intake(false));
                runAction(new Wait(500));
                runAction(new Intake(true, true));
                ThreadAction(new IntakeStopBlockIn());

                break;

            case MIDDLE:
                runAction(new Drive3DimensionalPID(new Pose2d(20, 16 * Side.getNum(Side_), Math.toRadians(-90 * Side.getNum(Side_))), .8));

                runAction(new Intake(true));

                runAction(new Drive3DimensionalPID(new Pose2d(20, 17 * Side.getNum(Side_), Math.toRadians(-40 * Side.getNum(Side_)))));
                runAction(new Drive3DimensionalPID(new Pose2d(32, 17 * Side.getNum(Side_), Math.toRadians(-40 * Side.getNum(Side_)))));
                runAction(new Drive3DimensionalPID(new Pose2d(23, 20 * Side.getNum(Side_), Math.toRadians(-90 * Side.getNum(Side_)))));

                runAction(new Intake(false));
                runAction(new Wait(500));
                runAction(new Intake(true, true));
                ThreadAction(new IntakeStopBlockIn());
                break;

            case LEFT:
                runAction(new Drive3DimensionalPID(new Pose2d(20, 40 * Side.getNum(Side_), Math.toRadians(-90 * Side.getNum(Side_))), .8));

                runAction(new Intake(true));

                runAction(new Drive3DimensionalPID(new Pose2d(20, 28 * Side.getNum(Side_), Math.toRadians(-40 * Side.getNum(Side_)))));
                runAction(new Drive3DimensionalPID(new Pose2d(29, 19 * Side.getNum(Side_), Math.toRadians(-40 * Side.getNum(Side_)))));
                runAction(new Drive3DimensionalPID(new Pose2d(23, 20 * Side.getNum(Side_), Math.toRadians(-90 * Side.getNum(Side_)))));

                runAction(new Intake(false));
                runAction(new Wait(500));
                runAction(new Intake(true, true));
                ThreadAction(new IntakeStopBlockIn());
                break;
        }

        //score second skystone
        runAction(new Drive3DimensionalPID(
                new Pose2d(rememberPoseScoringBlock.getX() + 2,
                        (rememberPoseScoringBlock.getY() - 2), Math.toRadians(-90 * Side.getNum(Side_))),
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
            runAction(new Drive3DimensionalPID(new Pose2d(rememberPoseScoringBlock.getX() + 2, 60 * Side.getNum(Side_), Math.toRadians(-90 * Side.getNum(Side_))), 1));
        else
            runAction(new Drive3DimensionalPID(new Pose2d(rememberPoseScoringBlock.getX() + 2, 45 * Side.getNum(Side_), Math.toRadians(-90 * Side.getNum(Side_))), 1));
    }
}
