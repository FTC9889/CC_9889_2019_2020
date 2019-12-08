package com.team9889.ftc2019.test.teleOp;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.team9889.ftc2019.subsystems.Robot;

import static java.lang.Math.round;

/**
 * Created by Eric on 11/25/2019.
 */

@TeleOp
@Deprecated
@Disabled
public class SkyStoneDetect extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        Robot.getInstance().getCamera().initVuforia(this);
        Robot.getInstance().getCamera().startTracking();

        while (opModeIsActive()){
            double[] pos = Robot.getInstance().getCamera().getCurrentSkyStonePosition();
            telemetry.addData("Sky Stone", round(pos[0]) + ", " +
                    round(pos[1]) + ", " + round(pos[2])+ ", " + round(pos[3]) + ", " +
                    round(pos[4]) + ", " + round(pos[5]));
            telemetry.update();
        }
    }
}
