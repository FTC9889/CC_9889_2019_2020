package com.team9889.ftc2019.auto.actions;

import com.qualcomm.robotcore.util.ElapsedTime;
import com.team9889.ftc2019.subsystems.Robot;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Created by Eric on 12/31/2019.
 */
public class LoopTimes extends Action{

    public LoopTimes(Telemetry telemetry) {
        this.telemetry = telemetry;
    }

    private Telemetry telemetry;
    ElapsedTime timer = new ElapsedTime();

    @Override
    public void setup(String args) {

    }

    @Override
    public void start() {
        timer.reset();
    }

    @Override
    public void update() {
        telemetry.addData("dt",timer.milliseconds());
        Robot.getInstance().outputToTelemetry(telemetry);
        telemetry.update();
        timer.reset();
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void done() {

    }
}
