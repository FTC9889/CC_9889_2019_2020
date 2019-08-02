package com.team9889.ftc2019.test.teleOp;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by Eric on 8/2/2019.
 */

@Autonomous(group = "testing")
public class RevExtensions2 extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        org.openftc.revextensions2.RevExtensions2.init();
    }
}
