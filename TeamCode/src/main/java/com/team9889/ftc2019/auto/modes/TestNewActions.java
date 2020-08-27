package com.team9889.ftc2019.auto.modes;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.team9889.ftc2019.auto.AutoModeBase;
import com.team9889.ftc2019.auto.actions.intake.IntakeStopBlockInWait;
import com.team9889.ftc2019.auto.actions.lift.Grabber;
import com.team9889.ftc2019.auto.actions.lift.Lift;
import com.team9889.ftc2019.auto.actions.lift.LiftLinearBar;
import com.team9889.ftc2019.auto.actions.utl.ParallelAction;
import com.team9889.ftc2019.auto.actions.utl.TimeoutAction;
import com.team9889.ftc2019.auto.actions.drive.DriveFollowPath;
import com.team9889.ftc2019.auto.actions.drive.DriveToFoundation;
import com.team9889.ftc2019.auto.actions.drive.Foundation;
import com.team9889.ftc2019.auto.actions.foundation.FoundationHookClose;
import com.team9889.ftc2019.auto.actions.intake.IntakeDown;
import com.team9889.ftc2019.auto.actions.intake.IntakeStopBlockIn;
import com.team9889.ftc2019.auto.actions.intake.Outtake;
import com.team9889.ftc2019.auto.actions.lift.LiftWait;
import com.team9889.ftc2019.auto.actions.lift.LinearWait;
import com.team9889.ftc2019.auto.actions.lift.OpenGrabberWait;
import com.team9889.lib.Path;
import com.team9889.lib.control.Timeout;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by joshua9889 on 6/29/2020.
 */
@Disabled
@Autonomous
public class TestNewActions extends AutoModeBase {

    List<Path> red_to_first_stone_left = Arrays.asList(
            new Path(new Pose2d(4, 0, 0), new Pose2d(2, 2, 3), 3, .8),
            new Path(new Pose2d(26, -15, -45), new Pose2d(2, 2, 3), 4, .8),
            new Path(new Pose2d(42, -15, -45), new Pose2d(2, 2, 3), 4, .6),
            new Path(new Pose2d(42, -20, -45), new Pose2d(2, 2, 3), 4, .8, 1000),
            new Path(new Pose2d(24, -20, -45), new Pose2d(2, 2, 3), 8.5, .8),
            new Path(new Pose2d(24, -20, -90), new Pose2d(2, 2, 3), 8.5, .8)
    );

    private List<Path> red_to_first_stone_right = Arrays.asList(
            new Path(new Pose2d(4, 0 , 0), new Pose2d(2, 2, 3), 3, .8),
            new Path(new Pose2d(26, 0, -40), new Pose2d(2, 2, 3), 4, .8),
            new Path(new Pose2d(42, 0, -40), new Pose2d(2, 2, 3), 4, .6),
            new Path(new Pose2d(42, -5, -40), new Pose2d(2, 2, 3), 4, .6),
            new Path(new Pose2d(26, -5, -40), new Pose2d(2, 2, 3), 4, .8)
    );

    private List<Path> red_to_first_stone_middle = Arrays.asList(
            new Path(new Pose2d(4, 0 , 0), new Pose2d(2, 2, 3), 3, .8),
            new Path(new Pose2d(26, -12, -30), new Pose2d(2, 2, 3), 4, .8, 1500),
            new Path(new Pose2d(40, -12, -30), new Pose2d(2, 2, 3), 4, .6, 1000),
            new Path(new Pose2d(40, -18, -30), new Pose2d(2, 2, 3), 4, .8, 1000),
            new Path(new Pose2d(26, -10, -90), new Pose2d(2, 2, 3), 3, .8, 1500)
    );

    private List<Path> foundation_to_second_stone_left = Arrays.asList(
            new Path(new Pose2d(24, 60, -100), new Pose2d(2, 2, 3), 4, 1),
            new Path(new Pose2d(24, 60, -100), new Pose2d(1, 2, 3), 4, 1),

            new Path(new Pose2d(26, 20, -90), new Pose2d(2, 2, 3), 4, .8),
            new Path(new Pose2d(28, 20, -75), new Pose2d(2, 2, 3), 4, .6),
            new Path(new Pose2d(42, 15, -75), new Pose2d(2, 2, 3), 2, .8),
            new Path(new Pose2d(42, 5, -75), new Pose2d(2, 2, 3), 2, .8, 1000)
    );

    private List<Path> foundation_to_second_stone_right = Arrays.asList(
            new Path(new Pose2d(24, 60, -100), new Pose2d(2, 2, 3), 4, 1),
            new Path(new Pose2d(24, 60, -100), new Pose2d(1, 2, 3), 4, 1),

            new Path(new Pose2d(20, 25, -90), new Pose2d(2, 2, 3), 4, .8, 4000),
            new Path(new Pose2d(24, 25, -45), new Pose2d(2, 2, 3), 4, .6, 2000),
            new Path(new Pose2d(40, 25, -45), new Pose2d(2, 2, 3), 2, .6, 1000),
            new Path(new Pose2d(40, 16, -45), new Pose2d(2, 2, 3), 2, .6, 2000)
    );

    private List<Path> foundation_to_second_stone_middle = Arrays.asList(
            new Path(new Pose2d(24, 60, -100), new Pose2d(2, 2, 3), 4, 1),
            new Path(new Pose2d(24, 60, -100), new Pose2d(1, 2, 3), 4, 1),

            new Path(new Pose2d(26, 22, -90), new Pose2d(2, 2, 3), 2, .8, 4000),
            new Path(new Pose2d(28, 22, -65), new Pose2d(2, 2, 3), 2, .8, 1000),
            new Path(new Pose2d(40, 22, -65), new Pose2d(2, 2, 3), 2, .8, 1500),
            new Path(new Pose2d(40, 12, -65), new Pose2d(2, 2, 3), 2, .8, 2000),
            new Path(new Pose2d(28, 20, -90), new Pose2d(2, 2, 3), 2, .8, 1000)
    );

    @Override
    public void run(Side side, SkyStonePosition stonePosition) {
        runAction(new ParallelAction(
                Arrays.asList(
                        new IntakeDown(),
                        new IntakeStopBlockIn()
                )
        ));

        switch (stonePosition) {
            case LEFT:
                runAction(new TimeoutAction(new DriveFollowPath(red_to_first_stone_left), new Timeout(8, TimeUnit.SECONDS)));
                break;
            case RIGHT:
                runAction(new TimeoutAction(new DriveFollowPath(red_to_first_stone_right), new Timeout(8, TimeUnit.SECONDS)));
                break;
            case MIDDLE:
                runAction(new TimeoutAction(new DriveFollowPath(red_to_first_stone_middle), new Timeout(8, TimeUnit.SECONDS)));
                break;
        }
        runAction(new Outtake(true));

        runAction(new Outtake(true));
        runAction(new DriveToFoundation(3000, 180));

        runAction(new ParallelAction(
                Arrays.asList(
                        new LiftWait(0, -1, 200),
                        new LiftWait(1900, 1, 300),
                        new LinearWait(0, true),
                        new OpenGrabberWait(1500, true),
                        new LinearWait(1700, false),
                        new Foundation(false, 65, false)
                        )
        ));

        runAction(new FoundationHookClose());

        Robot.getIntake().SetIntakePower(1);
        Robot.getIntake().SetRollerPower(.5);
        ThreadAction(new IntakeStopBlockInWait(3000));

        switch (stonePosition){
            case RIGHT:
                runAction(new DriveFollowPath(foundation_to_second_stone_right));
                break;

            case MIDDLE:
                runAction(new DriveFollowPath(foundation_to_second_stone_middle));
                break;

            case LEFT:
                runAction(new DriveFollowPath(foundation_to_second_stone_left));
                break;
        }

        ThreadAction(new Lift(-1, 400, 60, true));
        ThreadAction(new LiftLinearBar(false, 60, true));
        ThreadAction(new Grabber(true, 600, 60, true));

        // TODO: Need to convert the rest of this.
        /*

        pose.add(new FollowPath(new Pose2d(24, 45, -90), new Pose2d(2, 2, 3), 2, .8, 2000));
        pose.add(new FollowPath(new Pose2d(68, 45, -90), new Pose2d(2, 2, 3), 2, .8, 750));
        runAction(new DriveFollowPath(pose));
        pose.clear();

        Robot.getMecanumDrive().setCurrentPose(new Pose2d(38.5, Robot.getMecanumDrive().currentPose.getY()));
        Robot.getMecanumDrive().driftCalc = new Pose2d(0, Robot.getMecanumDrive().driftCalc.getY());

        pose.add(new FollowPath(new Pose2d(36, 65, -90), new Pose2d(1, 2, 3), 4, .8, 3000));
        pose.add(new FollowPath(new Pose2d(36, 65, -90), new Pose2d(1, 2, 3), 4, .8, 3000));
        runAction(new DriveFollowPath(pose));
        pose.clear();

        runAction(new Wait(500));

        runAction(new Intake(false));
        ThreadAction(new LinearWait(600, false));
        runAction(new LiftWait(1000, 1, 400));

        ThreadAction(new IntakeStopBlockInWait(500));
        switch (stonePosition){
            case RIGHT:
                pose.add(new FollowPath(new Pose2d(32, -20, -90), new Pose2d(2, 2, 3), 4, .8, 4000));
                pose.add(new FollowPath(new Pose2d(44, -20, -45), new Pose2d(2, 2, 3), 4, .8, 1000));
                pose.add(new FollowPath(new Pose2d(44, -35, -45), new Pose2d(2, 2, 3), 4, .8, 1000));
                pose.add(new FollowPath(new Pose2d(36, -15, -90), new Pose2d(2, 2, 3), 8.5, .8, 1000));
                break;

            case MIDDLE:
//                pose.add(new FollowPath(new Pose2d(36, 55, -90), new Pose2d(2, 2, 3), 2, 1));
                pose.add(new FollowPath(new Pose2d(36, 12, -90), new Pose2d(2, 2, 3), 3, .8));
                pose.add(new FollowPath(new Pose2d(34, 12, 0), new Pose2d(2, 2, 3), 2, .8));
                pose.add(new FollowPath(new Pose2d(60, 12, 0), new Pose2d(2, 2, 3), 2, .8, 2000));
                pose.add(new FollowPath(new Pose2d(36, 12, 0), new Pose2d(2, 2, 3), 4, .8));
                break;

            case LEFT:
//                pose.add(new FollowPath(new Pose2d(36, 55, -90), new Pose2d(2, 2, 3), 2, 1));
                pose.add(new FollowPath(new Pose2d(36, 15, -90), new Pose2d(2, 2, 3), 3, .8));
                pose.add(new FollowPath(new Pose2d(34, 15, 0), new Pose2d(2, 2, 3), 2, .8));
                pose.add(new FollowPath(new Pose2d(60, 15, 0), new Pose2d(2, 2, 3), 2, .8));
                pose.add(new FollowPath(new Pose2d(34, 15, 0), new Pose2d(2, 2, 3), 4, .8));
                break;
        }
        runAction(new DriveFollowPath(pose));
        pose.clear();

        ThreadAction(new LiftLinearBar(false, 75, true));
        ThreadAction(new Lift(-1, 600, 65, true));
        ThreadAction(new Grabber(true, 500, 75, true));
//        pose.add(new FollowPath(new Pose2d(30, 50, -90), new Pose2d(2, 2, 3), 2, .8, 3000));
//        pose.add(new FollowPath(new Pose2d(30, 60, -90), new Pose2d(2, 2, 3), 4, 1, 1000));
        pose.add(new FollowPath(new Pose2d(34, 100, -90), new Pose2d(2, 2, 3), 4, 1, 3000));
        pose.add(new FollowPath(new Pose2d(34, 100, -90), new Pose2d(2, 2, 3), 4, 1, 3000));
        runAction(new DriveFollowPath(pose));
        pose.clear();

        runAction(new Wait(700));

        ThreadAction(new LiftWait(100, 1, 700));
//        ThreadAction(new LiftLinearBar(true, -75, true));
        pose.add(new FollowPath(new Pose2d(36, 40, -90), new Pose2d(1, 1, 3), 2, 1));
        pose.add(new FollowPath(new Pose2d(36, 40, -90), new Pose2d(1, 1, 3), 4, 1));
        runAction(new DriveFollowPath(pose));
        pose.clear();
*/
    }
}
