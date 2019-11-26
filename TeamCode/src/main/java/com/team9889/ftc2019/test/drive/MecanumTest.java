package com.team9889.ftc2019.test.drive;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.team9889.ftc2019.Team9889Linear;
import com.team9889.ftc2019.auto.AutoModeBase;
import com.team9889.ftc2019.auto.actions.drive.MecanumDriveSimpleAction;

/**
 * Created by Eric on 11/26/2019.
 */
@TeleOp(group = "Test")
public class MecanumTest extends Team9889Linear {
    @Override
    public void runOpMode() throws InterruptedException {
        waitForStart(true);

        runAction(new MecanumDriveSimpleAction(24, 45));
    }
}
