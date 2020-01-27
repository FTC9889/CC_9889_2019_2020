package com.team9889.ftc2019.test.drive;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.team9889.ftc2019.auto.AutoModeBase;
import com.team9889.ftc2019.auto.actions.drive.DriveFollowPath;
import com.team9889.lib.CruiseLib;
import com.team9889.lib.FollowPath;
import com.team9889.lib.control.math.cartesian.Pose;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eric on 1/10/2020.
 */

@Autonomous (group = "Test")
public class TestDriveFollowPath extends AutoModeBase {
    @Override
    public void run(Side side, SkyStonePosition stonePosition) {

        List<FollowPath> pose = new ArrayList<>();

        pose.add(new FollowPath(new Pose2d(10, 0, Math.toRadians(0)), new Pose2d(2, 2, 3), 8.5, .5));
        pose.add(new FollowPath(new Pose2d(20, 12, 0), new Pose2d(2, 2, 3), 8.5, .5));
        pose.add(new FollowPath(new Pose2d(50, 0, Math.toRadians(90)), new Pose2d(3, 3, 3), 8.5, .5));

        runAction(new DriveFollowPath(pose));
        pose.clear();

//        runAction(new DriveFollowPath(
//                new Pose2d[]{
//                        new Pose2d(12, 24, 0),
//                        new Pose2d(36, 36, 0),
//                        new Pose2d(50, 12, Math.toRadians(90))}));
    }
}
