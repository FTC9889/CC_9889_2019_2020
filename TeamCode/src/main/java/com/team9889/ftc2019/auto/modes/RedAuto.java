package com.team9889.ftc2019.auto.modes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.team9889.ftc2019.auto.AutoModeBase;
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
import com.team9889.ftc2019.auto.actions.intake.IntakeStop;
import com.team9889.ftc2019.auto.actions.intake.IntakeStopBlockIn;

/**
 * Created by Eric on 11/26/2019.
 */

@Autonomous
public class RedAuto extends AutoModeBase {

    @Override
    public void run(Side side, AutoModeBase.SkyStonePosition stonePosition) {
        ThreadAction(new IntakeDown());
        ThreadAction(new Intake());
        runAction(new MecanumDriveSimpleAction(4, 0));
        switch (stonePosition){
            case LEFT:
                runAction(new MecanumDriveSimpleAction(36, -35));
                runAction(new MecanumDriveSimpleAction(-8, -35));

                ThreadAction(new IntakeStop());
                runAction(new Wait(500));

                Robot.getIntake().SetIntakePower(1); // Can be replaced with ThreadAction(new Intake(true, true));

                ThreadAction(new IntakeRollerOn());
                ThreadAction(new IntakeStopBlockIn());

                runAction(new MecanumDriveSimpleAction(0, -90, 1000));
                runAction(new MecanumDriveSimpleAction(-99, -90));
                runAction(new MecanumDriveSimpleAction(0, -180));

                Robot.getLift().SetLiftPower(-.5);
                //TODO add color sensor detection of foundation
                runAction(new MecanumDriveSimpleAction(-15, -180));
                Robot.getLift().SetLiftPower(0);
                runAction(new FoundationHookClose());

                ThreadAction(new LiftOut());

                runAction(new MecanumDriveSimpleAction(24, -135));

                Robot.getLift().SetLiftPower(1);
                runAction(new Wait(500));
                Robot.getLift().SetLiftPower(0);
                runAction(new OpenGrabber());

                runAction(new MecanumDriveSimpleAction(0, -90));
                runAction(new LiftIn());
                ThreadAction(new FoundationHookOpen());


//                runAction(new MecanumDriveSimpleAction(40, -90));
                break;

            case MIDDLE:
                break;

            case RIGHT:
                break;
        }
    }
}
