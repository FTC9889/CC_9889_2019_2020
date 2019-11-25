package com.team9889.ftc2019.auto;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.team9889.ftc2019.Team9889Linear;
import com.team9889.ftc2019.auto.actions.Action;
import com.team9889.ftc2019.auto.actions.Lift.CloseGrabber;
import com.team9889.ftc2019.auto.actions.Lift.LiftIn;
import com.team9889.ftc2019.auto.actions.Lift.LiftOut;
import com.team9889.ftc2019.auto.actions.Lift.OpenGrabber;
import com.team9889.ftc2019.auto.actions.TestMotorAction;
import com.team9889.ftc2019.auto.actions.Wait;
import com.team9889.ftc2019.auto.actions.drive.FoundationHookClose;
import com.team9889.ftc2019.auto.actions.drive.FoundationHookOpen;
import com.team9889.ftc2019.auto.actions.drive.MecanumDriveAction;
import com.team9889.ftc2019.auto.actions.drive.MecanumToAngle;
import com.team9889.ftc2019.auto.actions.drive.MecanumToPosition;
import com.team9889.ftc2019.auto.actions.intake.Intake;
import com.team9889.ftc2019.auto.actions.intake.IntakeDown;
import com.team9889.ftc2019.auto.actions.intake.IntakeStop;
import com.team9889.ftc2019.auto.actions.intake.IntakeUp;
import com.team9889.ftc2019.subsystems.Robot;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

/**
 * Created by Jin on 11/10/2017.
 */

@com.qualcomm.robotcore.eventloop.opmode.Autonomous
public class Autonomous extends Team9889Linear {
    boolean stop = false;

    @Override
    public void runOpMode() throws InterruptedException {
        waitForStart(true);

        while (opModeIsActive() && !stop) {
//          Grab Sky Stone
            runAction(new MecanumDriveAction(0, 1, 0, 1000));
            ThreadAction(new IntakeDown());
            runAction(new MecanumToAngle(-25, 600));
            telemetry.addData("", Robot.getMecanumDrive().getAngle().getTheda(AngleUnit.DEGREES));
            telemetry.update();
            Robot.getIntake().SetIntakePower(.75);
            runAction(new MecanumDriveAction(0, 32, -25, 2500));
            runAction(new IntakeStop());

//          Drive To Foundation
            runAction(new MecanumDriveAction(0, -4, -25, 1000));
            ThreadAction(new CloseGrabber());
            runAction(new MecanumToAngle(-65, 2000));
//            runAction(new MecanumDriveAction(0, -44, -90, 3000));
//            runAction(new MecanumDriveAction(8, 0, -90, 500));
            ThreadAction(new IntakeUp());
            runAction(new MecanumDriveAction(0, -64, -90, 4000));
            runAction(new MecanumDriveAction(30, 0, -90, 2000));

//          Move Foundation To Wall
            ThreadAction(new LiftOut());
            runAction(new MecanumDriveAction(0, -15, -90, 1000));

//          Place Sky Stone On Foundation
            runAction(new Wait(1000));
            runAction(new OpenGrabber());
            runAction(new Wait(1000));

//          Drive To Pull Foundation
            ThreadAction(new LiftIn());
            runAction(new MecanumDriveAction(-22, 0, -90, 2000));
            runAction(new MecanumDriveAction(0, -24, -90, 2000));
            runAction(new MecanumToAngle(-90, 2000));
            telemetry.addData("", Robot.getMecanumDrive().getAngle().getTheda(AngleUnit.DEGREES));
            telemetry.update();
            runAction(new MecanumDriveAction(0, -8, 180, 500, true));

//          Pull Foundation
            runAction(new FoundationHookClose());
            runAction(new MecanumDriveAction(0, 48, 180, 3000, true, .3));
            runAction(new FoundationHookOpen());

//          Park
            runAction(new MecanumDriveAction(36, 0, 180, 3000, true));

            stop = true;
        }
    }
}
