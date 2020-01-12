package com.team9889.ftc2019.test.drive;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.team9889.ftc2019.auto.AutoModeBase;
import com.team9889.ftc2019.auto.actions.drive.DriveFollowPath;

/**
 * Created by Eric on 1/10/2020.
 */

@Autonomous (group = "Test")
public class TestDriveFollowPath extends AutoModeBase {
    @Override
    public void run(Side side, SkyStonePosition stonePosition) {
        runAction(new DriveFollowPath(
                new Pose2d[]{
                        new Pose2d(12, 24, 0),
                        new Pose2d(36, 36, 0),
                        new Pose2d(50, 12, Math.toRadians(90))}));
    }
}
