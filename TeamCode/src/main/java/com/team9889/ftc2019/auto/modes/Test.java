package com.team9889.ftc2019.auto.modes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.team9889.ftc2019.Team9889Linear;
import com.team9889.ftc2019.auto.actions.Wait;
import com.team9889.ftc2019.auto.actions.drive.MecanumDriveAction;

/**
 * Created by Eric on 11/25/2019.
 */

@Autonomous
@Deprecated
@Disabled
public class Test extends Team9889Linear {
    private boolean stop = false;

    @Override
    public void runOpMode() throws InterruptedException {
        Robot.init(hardwareMap, true );
        waitForStart();

        while (opModeIsActive() && !stop){
            runAction(new MecanumDriveAction(0, 24, 0, 3000));

            runAction(new Wait(5000));
            runAction(new MecanumDriveAction(0, -12, 0, 3000));

            stop = true;
        }
    }
}
