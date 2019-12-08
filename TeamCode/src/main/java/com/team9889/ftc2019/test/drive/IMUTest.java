package com.team9889.ftc2019.test.drive;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.team9889.ftc2019.Team9889Linear;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

/**
 * Created by Eric on 11/26/2019.
 */
@TeleOp(group = "Test")
@Deprecated
@Disabled
public class IMUTest extends Team9889Linear {
    @Override
    public void runOpMode() throws InterruptedException {
        waitForStart(true);

        while (opModeIsActive()) {
            double currentAngle = Robot.getMecanumDrive().getAngle().getTheda(AngleUnit.DEGREES);
            double angle = 175;

            if (Math.abs(angle) > 175) {
                currentAngle = Math.signum(currentAngle) > 0 ? currentAngle - 175 : currentAngle + 175;
                angle = Math.signum(angle) > 0 ? angle - 175 : angle + 175;
            }

            Log.d("Angles: ", currentAngle + ", " + angle);
            telemetry.addData("Current Angle", currentAngle);
            telemetry.addData("Wanted Angle", angle);
            telemetry.update();
        }
    }
}
