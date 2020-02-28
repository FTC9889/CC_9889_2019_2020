package com.team9889.ftc2019.auto.actions.lift;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.team9889.ftc2019.auto.actions.Action;
import com.team9889.ftc2019.subsystems.Robot;
import com.team9889.lib.CruiseLib;

/**
 * Created by Eric on 2/17/2020.
 */
public class Grabber extends Action {
    private boolean grabberOpen, poseBool = false, xBool = false, yBool = false, xGreaterThan, yGreaterThan, atPose, finished;
    private Pose2d pose;
    private double tolerance, x, y;
    int time;
    ElapsedTime timer = new ElapsedTime();

    public Grabber(boolean grabberOpen, int time){
        this.grabberOpen = grabberOpen;
        this.time = time;
        poseBool = false;
        xBool = false;
        yBool = false;
    }

    public Grabber(boolean grabberOpen, int time, Pose2d pose, double tolerance){
        this.grabberOpen = grabberOpen;
        this.time = time;
        this.pose = pose;
        this.tolerance = tolerance;
        poseBool = true;
    }

    public Grabber(boolean grabberOpen, int time, boolean xGreaterThan, double x){
        this.grabberOpen = grabberOpen;
        this.time = time;
        this.x = x;
        this.xGreaterThan = xGreaterThan;
        xBool = true;
    }

    public Grabber(boolean grabberOpen, int time, double y, boolean yGreaterThan){
        this.grabberOpen = grabberOpen;
        this.time = time;
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
            atPose = true;
        }else if (poseBool){
            if (CruiseLib.isBetween(Robot.getInstance().getMecanumDrive().getCurrentPose().getX(),
                    pose.getX() - tolerance, pose.getX() + tolerance) &&
                    CruiseLib.isBetween(Robot.getInstance().getMecanumDrive().getCurrentPose().getY(),
                            pose.getY() - tolerance, pose.getY() + tolerance) &&
                    CruiseLib.isBetween(Robot.getInstance().getMecanumDrive().getCurrentPose().getHeading(),
                            pose.getHeading() - tolerance, pose.getHeading() + tolerance)){
                atPose = true;
            }
        }else if (xBool){
            if (xGreaterThan) {
                if (Robot.getInstance().getMecanumDrive().getCurrentPose().getX() > x){
                    atPose = true;
                }
            }else {
                if (Robot.getInstance().getMecanumDrive().getCurrentPose().getX() < x){
                    atPose = true;
                }
            }
        }else if (yBool){
            if (yGreaterThan) {
                if (Robot.getInstance().getMecanumDrive().getCurrentPose().getY() > y){
                    atPose = true;
                }
            }else {
                if (Robot.getInstance().getMecanumDrive().getCurrentPose().getY() < y){
                    atPose = true;
                }
            }
        }

        if (atPose){
            if (timer.milliseconds() > time){
                finished = true;
            }
        }else {
            timer.reset();
        }
    }

    @Override
    public boolean isFinished() {
        return finished;
    }

    @Override
    public void done() {
        if (grabberOpen){
            Robot.getInstance().getLift().GrabberOpen();
        }else {
            Robot.getInstance().getLift().GrabberClose();
        }
    }
}
