package com.team9889.ftc2019.test;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.team9889.ftc2019.auto.AutoModeBase;
import com.team9889.ftc2019.auto.actions.drive.DriveFollowPath;
import com.team9889.lib.FollowPath;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eric on 2/21/2020.
 */

@Autonomous
@Disabled
public class JoshTest extends AutoModeBase {
    @Override
    public void run(Side side, SkyStonePosition stonePosition) {
        waitForStart(true);

        AutoModeBase.Side Side_ = AutoModeBase.Side.RED;
        List<FollowPath> pose = new ArrayList<>();
        Robot.redAuto = true;

        pose.add(new FollowPath(new Pose2d(0, -20, 0), new Pose2d(2, 2, 3), 4, 1));
        pose.add(new FollowPath(new Pose2d(0, -20, 0), new Pose2d(2, 2, 3), 4, 1));
        runAction(new DriveFollowPath(pose));
    }
}
