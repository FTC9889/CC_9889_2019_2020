package com.team9889.ftc2019.auto.actions.drive;

import com.team9889.ftc2019.auto.actions.Action;
import com.team9889.ftc2019.subsystems.Robot;

/**
 * Created by Eric on 9/9/2019.
 */
public class McanumDriveAction extends Action {
    private double xAxis, yAxis;
    private int angle, curve;
    private double xSpeed, ySpeed;
    private Boolean end;


    public McanumDriveAction(double xAxis, double yAxis, int angle, boolean stopAtEnd){
        this.xAxis = xAxis;
        this.yAxis = yAxis;
        this.angle = angle;
        this.curve = 0;
        this.end = stopAtEnd;
    }

    public McanumDriveAction(double radius, double yAxis, int angle, int curve, boolean stopAtEnd){
        this.xAxis = radius;
        this.yAxis = yAxis;
        this.angle = angle;
        this.curve = curve;
        this.end = stopAtEnd;
    }

    @Override
    public void setup(String args) {

    }

    @Override
    public void start() {
        if (curve == 0){
            Robot.getInstance().getMecanumDrive().setPosition(-xAxis, -yAxis, angle);
        }else {
            double circumference = xAxis * 2 * Math.PI / 4;
            xSpeed = 100 - ySpeed;
        }
    }

    @Override
    public void update() {
        Robot.getInstance().getMecanumDrive().update();
    }

    @Override
    public boolean isFinished() {
        if (curve == 0)
            return !Robot.getInstance().getMecanumDrive().setStraightPositionActive;
        else
            return !Robot.getInstance().getMecanumDrive().setCurvePositionActive;
    }

    @Override
    public void done() {
        if(end == true){
            Robot.getInstance().getMecanumDrive().setPower(0, 0, 0);
        }
    }
}
