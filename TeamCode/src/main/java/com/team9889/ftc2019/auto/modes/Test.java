package com.team9889.ftc2019.auto.modes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.team9889.ftc2019.Team9889Linear;
import com.team9889.ftc2019.auto.actions.Wait;
import com.team9889.ftc2019.auto.actions.drive.DimensionalPID;
import com.team9889.ftc2019.auto.actions.drive.MecanumDriveAction;
import com.team9889.ftc2019.auto.actions.drive.MecanumDriveSimpleAction;

/**
 * Created by Eric on 11/25/2019.
 */

@Autonomous
public class Test extends Team9889Linear {
    private boolean stop = false;

    @Override
    public void runOpMode() throws InterruptedException {
        Robot.init(hardwareMap, true );
        waitForStart();
//            Robot.getMecanumDrive().setFieldCentricPower(-.18, .6, .5);

//            runAction(new DimensionalPID("5, 5, 0; 20, 0, 0"));

        runAction(new MecanumDriveSimpleAction(100, 0));
    }
}
