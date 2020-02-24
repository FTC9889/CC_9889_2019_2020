package com.team9889.ftc2019.auto.actions.intake;

import com.qualcomm.robotcore.util.ElapsedTime;
import com.team9889.ftc2019.auto.actions.Action;
import com.team9889.ftc2019.subsystems.Robot;

/**
 * Created by Eric on 11/22/2019.
 */
public class Intake extends Action {

    private ElapsedTime timer = new ElapsedTime();

    private boolean intake = true;
    private boolean fullSpeed = false;

    public Intake() {
        this.intake = true;
        this.fullSpeed = false;
    }

    public Intake(boolean intake) {
        this.intake = intake;
        this.fullSpeed = false;
    }

    public Intake(boolean intake, boolean fullSpeed) {
        this.intake = intake;
        this.fullSpeed = fullSpeed;
    }

    @Override
    public void setup(String args) {

    }

    @Override
    public void start() {
        timer.reset();
    }

    @Override
    public void update() {
        if(timer.milliseconds() > 100 && timer.milliseconds() < 200) {
            if(intake) Robot.getInstance().getIntake().SetIntakePower(fullSpeed ? 1.0 : 0.5);
            else Robot.getInstance().getIntake().SetIntakePower(0);
        }
        else if(timer.milliseconds() > 200) {
            if (intake) Robot.getInstance().getIntake().SetRollerPower(1);
            else Robot.getInstance().getIntake().SetRollerPower(0);
        }
    }

    @Override
    public boolean isAtPose() {
        return timer.milliseconds() > 300;
    }

    @Override
    public void done() {}
}
