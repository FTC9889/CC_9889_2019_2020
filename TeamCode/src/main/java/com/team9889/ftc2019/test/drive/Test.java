package com.team9889.ftc2019.test.drive;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.team9889.ftc2019.Team9889Linear;
import com.team9889.ftc2019.auto.AutoModeBase;
import com.team9889.ftc2019.auto.actions.drive.DriveFollowPath;
import com.team9889.ftc2019.auto.actions.drive.MecanumDriveSimpleAction;
import com.team9889.lib.FollowPath;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eric on 11/25/2019.
 */

@Autonomous
public class Test extends AutoModeBase {
    @Override
    public void run(Side side, AutoModeBase.SkyStonePosition stonePosition) {
        Side Side_ = Side.RED;
        List<FollowPath> pose = new ArrayList<>();
        Robot.redAuto = true;

        pose.add(new FollowPath(new Pose2d(24, 0, 0), new Pose2d(2, 2, 3), 3, .3));
        pose.add(new FollowPath(new Pose2d(24, 0, 0), new Pose2d(2, 2, 3), 3, .3));
        runAction(new DriveFollowPath(pose));
    }
}