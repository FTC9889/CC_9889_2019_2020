package com.team9889.ftc2019.auto.actions.lift;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.team9889.ftc2019.auto.actions.Action;
import com.team9889.ftc2019.subsystems.Robot;
import com.team9889.lib.CruiseLib;

/**
 * Created by Eric on 2/17/2020.
 */
public class LiftLinearBar extends Action {
    private boolean linearBarIn, poseBool = false, xBool = false, yBool = false, xGreaterThan, yGreaterThan, finished;
    private Pose2d pose;
    private double tolerance, x, y;

    public LiftLinearBar(boolean linearBarIn){
        this.linearBarIn = linearBarIn;
        poseBool = false;
        xBool = false;
        yBool = false;
    }

    public LiftLinearBar(boolean linearBarIn, Pose2d pose, double tolerance){
        this.linearBarIn = linearBarIn;
        this.pose = pose;
        this.tolerance = tolerance;
        poseBool = true;
    }

    public LiftLinearBar(boolean linearBarIn, boolean xGreaterThan, double x){
        this.linearBarIn = linearBarIn;
        this.x = x;
        this.xGreaterThan = xGreaterThan;
        xBool = true;
    }

    public LiftLinearBar(boolean linearBarIn, double y, boolean yGreaterThan){
        this.linearBarIn = linearBarIn;
        this.y = y;
        this.yGreaterThan = yGreaterThan;
        yBool = true;
    }

    @Override
    public void setup(String args) {

    }

    @Override
    public void start() {

    }

    @Override
    public void update() {
        if (!poseBool && !xBool && !yBool){
            finished = true;
        }else if (poseBool){
            if (CruiseLib.isBetween(Robot.getInstance().getMecanumDrive().getCurrentPose().getX(),
                    pose.getX() - tolerance, pose.getX() + tolerance) &&
                    CruiseLib.isBetween(Robot.getInstance().getMecanumDrive().getCurrentPose().getY(),
                            pose.getY() - tolerance, pose.getY() + tolerance) &&
                    CruiseLib.isBetween(Robot.getInstance().getMecanumDrive().getCurrentPose().getHeading(),
                            pose.getHeading() - tolerance, pose.getHeading() + tolerance)){
                finished = true;
            }
        }else if (xBool){
            if (xGreaterThan) {
                if (Robot.getInstance().getMecanumDrive().getCurrentPose().getX() > x){
                    finished = true;
                }
            }else {
                if (Robot.getInstance().getMecanumDrive().getCurrentPose().getX() < x){
                    finished = true;
                }
            }
        }else if (yBool){
            if (yGreaterThan) {
                if (Robot.getInstance().getMecanumDrive().getCurrentPose().getY() > y){
                    finished = true;
                }
            }else {
                if (Robot.getInstance().getMecanumDrive().getCurrentPose().getY() < y){
                    finished = true;
                }
            }
        }
    }

    @Override
    public boolean isFinished() {
        return finished;
    }

    @Override
    public void done() {
        if (linearBarIn){
            Robot.getInstance().getLift().LinearBarIn();
        }else {
            Robot.getInstance().getLift().LinearBarOut();
        }
    }
}
