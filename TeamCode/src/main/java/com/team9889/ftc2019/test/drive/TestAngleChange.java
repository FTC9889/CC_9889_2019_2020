package com.team9889.ftc2019.test.drive;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.team9889.ftc2019.auto.AutoModeBase;
import com.team9889.ftc2019.auto.actions.Wait;

/**
 * Created by Eric on 1/3/2020.
 */
@Autonomous(group = "Test")
public class TestAngleChange extends AutoModeBase {
    @Override
    public void run(Side side, SkyStonePosition stonePosition) {

        runAction(new Wait(3000));
        Robot.getMecanumDrive().writeAngleToFile();
    }
}
