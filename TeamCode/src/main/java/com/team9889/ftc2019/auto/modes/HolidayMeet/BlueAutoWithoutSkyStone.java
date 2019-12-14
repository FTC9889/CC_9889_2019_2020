package com.team9889.ftc2019.auto.modes.HolidayMeet;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.team9889.ftc2019.Team9889Linear;
import com.team9889.ftc2019.auto.actions.Lift.CloseGrabber;
import com.team9889.ftc2019.auto.actions.Lift.LiftIn;
import com.team9889.ftc2019.auto.actions.Lift.LiftOut;
import com.team9889.ftc2019.auto.actions.Lift.OpenGrabber;
import com.team9889.ftc2019.auto.actions.Wait;
import com.team9889.ftc2019.auto.actions.drive.FoundationHookClose;
import com.team9889.ftc2019.auto.actions.drive.FoundationHookOpen;
import com.team9889.ftc2019.auto.actions.drive.MecanumDriveSimpleAction;
import com.team9889.ftc2019.auto.actions.intake.Intake;
import com.team9889.ftc2019.auto.actions.intake.IntakeDown;
import com.team9889.ftc2019.auto.actions.intake.IntakeRollerOn;
import com.team9889.ftc2019.auto.actions.intake.IntakeRollerStop;
import com.team9889.ftc2019.auto.actions.intake.IntakeStop;
import com.team9889.ftc2019.auto.actions.intake.IntakeUp;

/**
 * Created by Eric on 11/26/2019.
 */

@Autonomous
public class BlueAutoWithoutSkyStone extends Team9889Linear {
    int skyStonePosition = 1;

    @Override
    public void runOpMode() throws InterruptedException {

        waitForStart(true);

        runAction(new MecanumDriveSimpleAction(10, 0));
        ThreadAction(new IntakeDown());
        ThreadAction(new Intake());
        runAction(new MecanumDriveSimpleAction(8, 28));
        runAction(new IntakeStop());
        runAction(new Wait(250));
        runAction(new Intake());

//          Drive To Foundation
         runAction(new MecanumDriveSimpleAction(-7, 23));
         runAction(new MecanumDriveSimpleAction(0, 87, 1, 1000));
         runAction(new IntakeStop());
         ThreadAction(new IntakeRollerOn());
         runAction(new MecanumDriveSimpleAction(-35, 87));

        ThreadAction(new CloseGrabber());
        runAction(new MecanumDriveSimpleAction(0, 180));
        runAction(new MecanumDriveSimpleAction(-6, 180));
        ThreadAction(new FoundationHookClose());
        runAction(new MecanumDriveSimpleAction(-3, 180));
        runAction(new IntakeUp());
        ThreadAction(new IntakeRollerOn());

        Robot.getLift().SetLiftPower(-.5);
        runAction(new Wait(250));
        Robot.getLift().SetLiftPower(0);

        runAction(new MecanumDriveSimpleAction(30, 190));
        runAction(new LiftOut());
        runAction(new MecanumDriveSimpleAction(0, 270, 1.8, 2000));

        Robot.getMecanumDrive().setPower(1, 0, 0);
        runAction(new Wait(500));
        Robot.getMecanumDrive().setPower(0, 0, 0);


        runAction(new IntakeRollerStop());

        runAction(new Wait(1000));
        Robot.getLift().SetLiftPower(.5);
        runAction(new Wait(250));
        Robot.getLift().SetLiftPower(0);
        runAction(new OpenGrabber());
        runAction(new Wait(250));
        runAction(new LiftIn());

        runAction(new FoundationHookOpen());

        Robot.getMecanumDrive().setPower(-.5, 0, 0);
        runAction(new Wait(300));
        Robot.getMecanumDrive().setPower(0, 0, 0);
        runAction(new MecanumDriveSimpleAction(20, 90));

        runAction(new MecanumDriveSimpleAction(0, 50, .9, 2000));
        ThreadAction(new IntakeDown());
        ThreadAction(new Intake());
        runAction(new MecanumDriveSimpleAction(12, 50));
        runAction(new MecanumDriveSimpleAction(5, 50, 20000, 1, 1));
        runAction(new IntakeStop());
        runAction(new MecanumDriveSimpleAction(-18, 50));
        ThreadAction(new Intake());

        /*
        runAction(new MecanumDriveSimpleAction(0, 90, 1, 1000));
        runAction(new IntakeRollerOn());
        runAction(new MecanumDriveSimpleAction(-22, 90));

        runAction(new CloseGrabber());

        Robot.getMecanumDrive().setPower(1, 0, 0);
        runAction(new Wait(1000));
        Robot.getMecanumDrive().setPower(0, 0, 0);

        runAction(new MecanumDriveSimpleAction(-5, 90, 30000, 1, 1));

        Robot.getLift().SetLiftPower(-1);
        runAction(new Wait(250));
        Robot.getLift().SetLiftPower(0);
        runAction(new LiftOut());
        runAction(new Wait(1000));
        Robot.getLift().SetLiftPower(.5);
        runAction(new Wait(150));
        Robot.getLift().SetLiftPower(0);

        runAction(new OpenGrabber());
        runAction(new Wait(500));

        Robot.getLift().SetLiftPower(-.7);
        runAction(new Wait(250));
        Robot.getLift().SetLiftPower(0);

        Robot.getMecanumDrive().setPower(1, 0, 0);
        runAction(new Wait(500));
        runAction(new MecanumDriveSimpleAction(15, 90));
        */
    }
}
