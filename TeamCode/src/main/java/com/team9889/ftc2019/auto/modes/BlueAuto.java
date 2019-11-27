package com.team9889.ftc2019.auto.modes;

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
public class BlueAuto extends Team9889Linear {
    int skyStonePosition = 1;

    @Override
    public void runOpMode() throws InterruptedException {

        waitForStart(true);

        if (positionOfSkyStone < 110) {
            skyStonePosition = 1;
        }else if (positionOfSkyStone > 109 && positionOfSkyStone < 180){
            skyStonePosition = 2;
        }else if (positionOfSkyStone > 179){
            skyStonePosition = 3;
        }

        runAction(new MecanumDriveSimpleAction(3, 0));
        ThreadAction(new IntakeDown());
        if (skyStonePosition == 1) {
//          Pickup Sky Stone
//            runAction(new MecanumToAngle(-25, 2000));
            runAction(new Intake());
            runAction(new MecanumDriveSimpleAction(25, -15));
            runAction(new IntakeStop());
            runAction(new Wait(250));
            runAction(new Intake());

//          Drive To Foundation
            runAction(new MecanumDriveSimpleAction(-10, -15));
            runAction(new MecanumDriveSimpleAction(0, 85));
            runAction(new IntakeStop());
            ThreadAction(new IntakeRollerOn());
            runAction(new MecanumDriveSimpleAction(-65, 85));
        }else if (skyStonePosition == 2){
            runAction(new Intake());
            runAction(new MecanumDriveSimpleAction(29, 25));
            runAction(new IntakeStop());
            runAction(new Wait(250));
            runAction(new Intake());

//          Drive To Foundation
            runAction(new MecanumDriveSimpleAction(-10, 25));
            runAction(new MecanumDriveSimpleAction(0, 85));
            runAction(new IntakeStop());
            ThreadAction(new IntakeRollerOn());
            runAction(new MecanumDriveSimpleAction(-75, 85));
        }else if (skyStonePosition == 3){
            runAction(new Intake());
            runAction(new MecanumDriveSimpleAction(33, 32));
            runAction(new IntakeStop());
            runAction(new Wait(250));
            runAction(new Intake());

//          Drive To Foundation
            runAction(new MecanumDriveSimpleAction(-10, 30));
            runAction(new MecanumDriveSimpleAction(0, 85));
            runAction(new IntakeStop());
            ThreadAction(new IntakeRollerOn());
            runAction(new MecanumDriveSimpleAction(-86, 85));
        }

        ThreadAction(new CloseGrabber());
        runAction(new IntakeRollerStop());
        runAction(new MecanumDriveSimpleAction(0, 180));
        runAction(new MecanumDriveSimpleAction(-10, 180));
        ThreadAction(new FoundationHookClose());
        runAction(new MecanumDriveSimpleAction(-3, 180));
        ThreadAction(new MecanumDriveSimpleAction(110, 180));
        runAction(new IntakeUp());
        runAction(new LiftOut());
        runAction(new Wait(250));
        runAction(new OpenGrabber());
        runAction(new Wait(250));
        ThreadAction(new LiftIn());
        runAction(new IntakeUp());
        runAction(new FoundationHookOpen());
        Robot.getMecanumDrive().setPower(-.7, 0, 0);
        runAction(new Wait(1500));
        Robot.getMecanumDrive().setPower(0, 0, 0);
        runAction(new MecanumDriveSimpleAction(-15, 180));
        Robot.getMecanumDrive().setPower(.7, 0, 0);
        runAction(new Wait(1000));
        Robot.getMecanumDrive().setPower(0, 0, 0);
        runAction(new MecanumDriveSimpleAction(-8, 180));
        Robot.getMecanumDrive().setPower(-.7, 0, 0);
        runAction(new Wait(1250));
        Robot.getMecanumDrive().setPower(0, 0, 0);

        runAction(new MecanumDriveSimpleAction(0, 0));
        runAction(new MecanumDriveSimpleAction(0, 0));
        runAction(new MecanumDriveSimpleAction(0, 0));
        runAction(new Wait(250));
        runAction(new MecanumDriveSimpleAction(0, 0));
        runAction(new MecanumDriveSimpleAction(0, 0));
    }
}
