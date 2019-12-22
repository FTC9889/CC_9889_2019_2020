package com.team9889.ftc2019.auto.modes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.team9889.ftc2019.Team9889Linear;
import com.team9889.ftc2019.auto.AutoModeBase;
import com.team9889.ftc2019.auto.actions.Action;
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
import com.team9889.ftc2019.auto.actions.intake.IntakeStopBlockIn;
import com.team9889.ftc2019.auto.actions.intake.IntakeUp;

import java.util.Arrays;

/**
 * Created by Eric on 11/26/2019.
 */

@Autonomous
public class RedAuto extends AutoModeBase {

    @Override
    public void run(Side side, AutoModeBase.SkyStonePosition stonePosition) {
        ThreadAction(new IntakeDown());
        switch (stonePosition){
            case LEFT:
                runAction(new MecanumDriveSimpleAction(23, 0));
                runAction(new MecanumDriveSimpleAction(0, -20, 1000));
                runAction(new Intake());
                runAction(new MecanumDriveSimpleAction(6, -20));
                runAction(new MecanumDriveSimpleAction(-6, -20));
                runAction(new MecanumDriveSimpleAction(0, -90, 1000));
                break;

            case MIDDLE:
                break;

            case RIGHT:
                break;
        }
    }
}
