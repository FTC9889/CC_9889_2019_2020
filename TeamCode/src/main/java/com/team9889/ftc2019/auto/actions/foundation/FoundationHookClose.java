package com.team9889.ftc2019.auto.actions.foundation;

import com.qualcomm.robotcore.util.ElapsedTime;
import com.team9889.ftc2019.auto.actions.Action;
import com.team9889.ftc2019.subsystems.Robot;

/**
 * Created by Eric on 11/22/2019.
 */
public class FoundationHookClose extends Action {
    private ElapsedTime timer = new ElapsedTime();

    @Override
    public void setup(String args) {

    }

    @Override
    public void start() {
        timer.reset();
        Robot.getInstance().getMecanumDrive().CloseFoundationHook();
    }

    @Override
    public void update() {}

    @Override
    public boolean isAtPose() {
        return timer.milliseconds() > 500;
    }

    @Override
    public void done() {}
}
