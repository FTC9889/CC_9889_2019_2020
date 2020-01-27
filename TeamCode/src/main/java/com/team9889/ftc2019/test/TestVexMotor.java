package com.team9889.ftc2019.test;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.team9889.ftc2019.Team9889Linear;
import com.team9889.ftc2019.subsystems.Robot;
import com.team9889.lib.CruiseLib;

/**
 * Created by Eric on 1/17/2020.
 */

@TeleOp (group = "Test")
public class TestVexMotor extends Team9889Linear {
//    CRServo vexMotor;



    @Override
    public void runOpMode() throws InterruptedException {
//        vexMotor = com.team9889.ftc2019.subsystems.Robot.getInstance().hardwareMap.crservo.get("vexmotor");

        waitForStart(false);

        while (opModeIsActive()){
            if (gamepad1.right_stick_y > .05)
                Robot.roller.setPower(CruiseLib.limitValue(gamepad1.right_stick_y, .5));
        }
    }
}
