package com.team9889.ftc2019.test.intake;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.team9889.ftc2019.auto.AutoModeBase;
import com.team9889.ftc2019.auto.actions.drive.Drive3DimensionalPID;
import com.team9889.ftc2019.auto.actions.intake.Intake;
import com.team9889.ftc2019.auto.actions.intake.IntakeDown;
import com.team9889.ftc2019.auto.actions.intake.IntakeStopBlockIn;

/**
 * Created by joshua9889 on 12/24/2019.
 */
@Autonomous(group = "Test")
@Disabled
public class IntakeTest extends AutoModeBase {
    @Override
    public void run(Side side, SkyStonePosition stonePosition) {
        runAction(new Intake());

        runAction(new IntakeStopBlockIn());
    }
}
