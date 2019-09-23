package com.team9889.ftc2019.auto.modes;

import com.qualcomm.hardware.ams.AMSColorSensor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.team9889.ftc2019.auto.AutoModeBase;
import com.team9889.ftc2019.auto.actions.Wait;
import com.team9889.ftc2019.auto.actions.drive.McanumDriveAction;

/**
 * Created by Eric on 9/21/2019.
 */

@Autonomous
public class NJITAuto extends AutoModeBase {

    @Override
    public void run(Side side, boolean doubleSample, boolean scoreSample) {
        Robot.getMecanumDrive().setPower(.3, 0, 0);
        runAction(new Wait(500));
        Robot.getMecanumDrive().setPower(0, .5, 0);
        runAction(new Wait(700));
    }
}
