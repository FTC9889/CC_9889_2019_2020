package com.team9889.ftc2019.auto.modes;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.team9889.ftc2019.auto.AutoModeBase;
import com.team9889.ftc2019.auto.actions.Lift.CloseGrabber;
import com.team9889.ftc2019.auto.actions.Lift.LiftIn;
import com.team9889.ftc2019.auto.actions.Lift.LiftOut;
import com.team9889.ftc2019.auto.actions.Lift.LiftUp;
import com.team9889.ftc2019.auto.actions.Lift.OpenGrabber;
import com.team9889.ftc2019.auto.actions.Wait;
import com.team9889.ftc2019.auto.actions.drive.Drive3DimensionalPID;
import com.team9889.ftc2019.auto.actions.drive.DriveToFoundation;
import com.team9889.ftc2019.auto.actions.foundation.FoundationHookClose;
import com.team9889.ftc2019.auto.actions.foundation.FoundationHookOpen;
import com.team9889.ftc2019.auto.actions.drive.MecanumDriveSimpleAction;
import com.team9889.ftc2019.auto.actions.intake.Intake;
import com.team9889.ftc2019.auto.actions.intake.IntakeDown;
import com.team9889.ftc2019.auto.actions.intake.IntakeRollerOn;
import com.team9889.ftc2019.auto.actions.intake.IntakeRollerStop;
import com.team9889.ftc2019.auto.actions.intake.IntakeStop;
import com.team9889.ftc2019.auto.actions.intake.IntakeStopBlockIn;

/**
 * Created by Eric on 11/26/2019.
 */

@Autonomous
public class RedAuto extends AutoModeBase {

    @Override
    public void run(Side side, AutoModeBase.SkyStonePosition stonePosition) {
        ThreadAction(new IntakeDown());
        runAction(new Drive3DimensionalPID(new Pose2d(4, 0, 0)));
        ThreadAction(new Intake());

        switch (stonePosition){
            case LEFT:
                //pick up skystone
//                runAction(new Drive3DimensionalPID(new Pose2d(27, -14, Math.toRadians(-35))));
                runAction(new Drive3DimensionalPID(new Pose2d(33, -17, Math.toRadians(-35)), .35));
                runAction(new Drive3DimensionalPID(new Pose2d(25, -10, Math.toRadians(-90))));
                runAction(new Intake(false));

                runAction(new Wait(500));
                runAction(new Intake(true));
                ThreadAction(new IntakeStopBlockIn());
                break;

            case MIDDLE:
                break;

            case RIGHT:
                break;
        }

        //drive to foundation
        runAction(new Drive3DimensionalPID(new Pose2d(25, 85, Math.toRadians(-90)), .8));
        runAction(new Wait(200));
        runAction(new Drive3DimensionalPID(new Pose2d(30, 85, Math.toRadians(180))));

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

        runAction(new Drive3DimensionalPID(new Pose2d(22, 56, Math.toRadians(-90))));

        runAction(new Drive3DimensionalPID(new Pose2d(24, 56, Math.toRadians(-90))));
        runAction(new IntakeRollerStop());
        ThreadAction(new OpenGrabber());
        ThreadAction(new FoundationHookOpen());
        Pose2d rememberPoseScoringBlock = Robot.getMecanumDrive().currentPose;

        runAction(new Wait(500));

        ThreadAction(new LiftIn());

        //grab second skystone
        switch (stonePosition){
            case LEFT:
                runAction(new Drive3DimensionalPID(new Pose2d(23, 21.5, Math.toRadians(-90)), .8));

                runAction(new Intake(true));

                runAction(new Drive3DimensionalPID(new Pose2d(30, 9, Math.toRadians(-40))));
                runAction(new Drive3DimensionalPID(new Pose2d(23, 20, Math.toRadians(-90))));

                runAction(new Intake(false));
                runAction(new Wait(500));
                runAction(new Intake(true));
                ThreadAction(new IntakeStopBlockIn());

                break;

            case MIDDLE:
                break;

            case RIGHT:
                break;
        }

        //score second skystone
        runAction(new Drive3DimensionalPID(
                new Pose2d(rememberPoseScoringBlock.getX() - 1,
                        rememberPoseScoringBlock.getY() + 2, Math.toRadians(-90)),
                new Pose2d(1.4, 1.4, 2)));

        runAction(new Wait(500));
        runAction(new CloseGrabber());
        runAction(new Wait(250));

        runAction(new LiftUp(14));
        runAction(new LiftOut());
        runAction(new Wait(1500));
        runAction(new OpenGrabber());
        ThreadAction(new LiftUp(20));
        runAction(new LiftIn());
        runAction(new Drive3DimensionalPID(new Pose2d(rememberPoseScoringBlock.getX(), 85, Math.toRadians(-90)), .8));
        Robot.getLift().SetLiftPower(.5);
        runAction(new Drive3DimensionalPID(new Pose2d(rememberPoseScoringBlock.getX(), 40, Math.toRadians(-90)), .8));
    }
}
