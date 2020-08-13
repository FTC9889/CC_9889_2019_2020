package com.team9889.ftc2019.auto.modes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.team9889.ftc2019.auto.AutoModeBase;
import com.team9889.ftc2019.auto.actions.utl.Wait;
import com.team9889.ftc2019.auto.actions.lift.LiftOut;

/**
 * Created by Eric on 2/1/2020.
 */

@Autonomous
public class ParkAuto extends AutoModeBase {


    @Override
    public void run(Side side, SkyStonePosition stonePosition) {
        runAction(new LiftOut());
        runAction(new Wait(3000));
    }
}
