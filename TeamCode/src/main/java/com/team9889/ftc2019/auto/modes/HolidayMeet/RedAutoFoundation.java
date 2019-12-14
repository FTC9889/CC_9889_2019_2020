package com.team9889.ftc2019.auto.modes.HolidayMeet;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.team9889.ftc2019.Team9889Linear;
import com.team9889.ftc2019.auto.actions.RobotUpdate;
import com.team9889.ftc2019.auto.actions.Wait;
import com.team9889.ftc2019.auto.actions.drive.FoundationHookClose;
import com.team9889.ftc2019.auto.actions.drive.MecanumDriveSimpleAction;

/**
 * Created by Eric on 12/13/2019.
 */

@Autonomous
public class RedAutoFoundation extends Team9889Linear {
    @Override
    public void runOpMode() throws InterruptedException {
        waitForStart(true);
        ThreadAction(new RobotUpdate());

        runAction(new MecanumDriveSimpleAction(-20, 0));
        runAction(new FoundationHookClose());

        runAction(new Wait(500));
        runAction(new MecanumDriveSimpleAction(25, -170));
        runAction(new MecanumDriveSimpleAction(0, -90, 1.8, 2000));
    }
}
