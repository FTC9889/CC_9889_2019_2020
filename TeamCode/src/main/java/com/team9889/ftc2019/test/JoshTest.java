package com.team9889.ftc2019.test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.team9889.ftc2019.Team9889Linear;
import com.team9889.ftc2019.auto.AutoModeBase;
import com.team9889.ftc2019.auto.actions.RobotUpdate;

/**
 * Created by Eric on 2/21/2020.
 */

@Autonomous
public class JoshTest extends Team9889Linear {
    @Override
    public void runOpMode() throws InterruptedException {
        waitForStart(true);
        telemetry.addData("H", "I");
        telemetry.update();

        ThreadAction(new RobotUpdate(new ElapsedTime()));

        while (opModeIsActive()) {
            Robot.outputToTelemetry(telemetry);
            telemetry.update();
        }
    }
}
