package com.team9889.ftc2019.test.drive;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.team9889.ftc2019.Team9889Linear;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Created by Eric on 8/16/2020.
 */

@TeleOp
public class OdometryCodeTest extends Team9889Linear {

    @Override
    public void runOpMode() throws InterruptedException {
        waitForStart(false);
        Robot.odometryLifter.setPosition(1);

        while (opModeIsActive()){
            telemetry.addData("x", Robot.getMecanumDrive().odometry.returnXCoordinate());
            telemetry.addData("Y", Robot.getMecanumDrive().odometry.returnYCoordinate());
            telemetry.addData("Angle", Robot.getMecanumDrive().odometry.returnOrientation());
            Robot.outputToTelemetry(telemetry);

            telemetry.update();

            Robot.update();
            Robot.getMecanumDrive().update();
            Robot.intakeLeft.update(Robot.bulkDataSlave);
            Robot.intakeRight.update(Robot.bulkDataSlave);
            Robot.leftLift.update(Robot.bulkDataSlave);
            Robot.rightLift.update(Robot.bulkDataSlave);
        }
    }
}
