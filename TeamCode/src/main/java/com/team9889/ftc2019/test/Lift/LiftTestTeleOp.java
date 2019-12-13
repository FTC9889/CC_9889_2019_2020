package com.team9889.ftc2019.test.Lift;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.team9889.ftc2019.Team9889Linear;

/**
 * Created by Eric on 12/12/2019.
 */

@TeleOp
public class LiftTestTeleOp extends Team9889Linear {
    @Override
    public void runOpMode() throws InterruptedException {

        Robot.init(hardwareMap, false);

        waitForStart();
        while (opModeIsActive()) {
            if (gamepad1.left_trigger > .1)
                Robot.getLift().SetLiftPower(gamepad1.left_trigger);
            else if (gamepad1.right_trigger > .1)
                Robot.getLift().SetLiftPower(-gamepad1.right_trigger);
            else if (gamepad1.a) {
                Robot.getLift().SetLiftPower(1);
            } else if (gamepad1.b) {
                Robot.getLift().SetLiftPower(-1);
            }else
                Robot.getLift().SetLiftPower(0);

            Robot.update();
        }
    }
}
