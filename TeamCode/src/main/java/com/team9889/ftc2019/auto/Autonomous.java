package com.team9889.ftc2019.auto;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.team9889.ftc2019.Team9889Linear;
import com.team9889.ftc2019.auto.actions.Action;
import com.team9889.ftc2019.auto.actions.TestMotorAction;
import com.team9889.ftc2019.auto.actions.Wait;
import com.team9889.ftc2019.auto.actions.drive.MecanumToPosition;
import com.team9889.ftc2019.subsystems.Robot;

/**
 * Created by Jin on 11/10/2017.
 */

@com.qualcomm.robotcore.eventloop.opmode.Autonomous
public class Autonomous extends Team9889Linear {
    boolean stop = false;

    @Override
    public void runOpMode() throws InterruptedException {

        Robot.init(hardwareMap, true );
        waitForStart();

        while (opModeIsActive() && !stop) {
            runAction(new MecanumToPosition(0, -30, 0));

            Robot.update();
            telemetry.addData("Left Front", Robot.fLDrive.getPosition());
            telemetry.addData("Left Back", Robot.bLDrive.getPosition());
            telemetry.addData("Right Front", Robot.fRDrive.getPosition());
            telemetry.addData("Right Back", Robot.bRDrive.getPosition());
            telemetry.update();

            runAction(new MecanumToPosition(30, 0, 0));

            Robot.update();
            telemetry.addData("Left Front", Robot.fLDrive.getPosition());
            telemetry.addData("Left Back", Robot.bLDrive.getPosition());
            telemetry.addData("Right Front", Robot.fRDrive.getPosition());
            telemetry.addData("Right Back", Robot.bRDrive.getPosition());
            telemetry.addData("done", null);
            telemetry.update();

            runAction(new Wait(5000));

            stop = true;
        }
    }
}
