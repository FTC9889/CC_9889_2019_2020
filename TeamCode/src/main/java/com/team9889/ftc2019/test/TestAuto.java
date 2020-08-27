package com.team9889.ftc2019.test;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.team9889.ftc2019.auto.AutoModeBase;
import com.team9889.ftc2019.auto.actions.drive.DriveFollowPath;
import com.team9889.ftc2019.auto.actions.lift.LiftLinearBar;
import com.team9889.lib.Path;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eric on 2/17/2020.
 */

@Autonomous(group = "Test")
@Disabled
public class TestAuto extends AutoModeBase {
    @Override
    public void run(Side side, SkyStonePosition stonePosition) {
        Side Side_ = Side.RED;
        List<Path> pose = new ArrayList<>();
        Robot.redAuto = true;

        ThreadAction(new LiftLinearBar(false, true, 10));

        pose.add(new Path(new Pose2d(30, 0, 0), new Pose2d(5, 5, 50), 5, .8));
        pose.add(new Path(new Pose2d(30, 24, 0), new Pose2d(5, 5, 50), 5, .8));
        runAction(new DriveFollowPath(pose));
    }
}
