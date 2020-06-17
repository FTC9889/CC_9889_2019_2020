package com.team9889.ftc2019.auto.actions.drive;

import android.util.Log;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.team9889.ftc2019.auto.actions.Action;
import com.team9889.ftc2019.subsystems.MecanumDrive;
import com.team9889.ftc2019.subsystems.Robot;
import com.team9889.lib.CruiseLib;
import com.team9889.lib.FollowPath;
import com.team9889.lib.control.controllers.PID;

import java.util.List;

/**
 * Created by Eric on 1/10/2020.
 */
public class DriveFollowPath extends Action {
    List<FollowPath> path;
    int pose = 0;
    int rNum = 0;
    int tNum = 0;
    int vNum = 0;

    double xAverage;
    double yAverage;

    double startOfNextLineX, endOfNextLineX;
    double startOfNextLineY, endOfNextLineY;

    // Controllers
    // XPID tuned
    private PID xPID = new PID(-0.2, 0, -16);
    private PID yPID = new PID(-0.1, 0, -16);
    private PID turnPID = new PID(.02, 0, .6);

    // Max Speed
    double maxVel = 0.5;

    // Wanted Pose of the Robot
    private Pose2d wantedPose;
    private Pose2d tolerancePose;

    // End Conditions
    private int timeOut = 30000; //  Milliseconds
    private ElapsedTime timer = new ElapsedTime();
    private int angleCounter = 0;
    private int xCounter = 0;
    private int yCounter = 0;

    private double time;
    private ElapsedTime tempTimer = new ElapsedTime();

    double lastYPower;

    double currentAngle;
    int first = 5;

    // Drivetrain object
    private MecanumDrive mDrive = Robot.getInstance().getMecanumDrive();

    public DriveFollowPath(List<FollowPath> path){
        this.path = path;
    }

    public DriveFollowPath(List<FollowPath> path, int timeOut){
        this.path = path;
        this.timeOut = timeOut;
    }


    public DriveFollowPath(List<FollowPath> path, PID xPID){
        this.path = path;
        this.xPID = xPID;
    }

    @Override
    public void setup(String args) {

    }

    @Override
    public void start() {
        startOfNextLineX = path.get(0).getPose().getX();
        startOfNextLineY = path.get(0).getPose().getY();

        endOfNextLineX = path.get(1).getPose().getX();
        endOfNextLineY = path.get(1).getPose().getY();

        wantedPose = path.get(pose).getPose();
        tolerancePose = path.get(tNum).getTollerancePose();
        maxVel = path.get(vNum).getMaxVelocity();

        tempTimer.reset();

        Log.i("pose started", "");
    }

    @Override
    public void update() {
        time = tempTimer.milliseconds();

//      x and y = robot position
        double x = Robot.getInstance().getMecanumDrive().getAdjustedPose().getX();
        double y = Robot.getInstance().getMecanumDrive().getAdjustedPose().getY();

        if (Math.toDegrees(Robot.getInstance().getMecanumDrive().getAdjustedPose().getHeading()) > 180){
            currentAngle = Math.toDegrees(Robot.getInstance().getMecanumDrive().getAdjustedPose().getHeading()) - 360;
        }else {
            currentAngle = Math.toDegrees(Robot.getInstance().getMecanumDrive().getAdjustedPose().getHeading());
        }

        tolerancePose = path.get(tNum).getTollerancePose();
        maxVel = path.get(vNum).getMaxVelocity();
        timeOut = path.get(vNum).getTimeOut();

        if(rNum > path.size()-1){
            rNum = path.size()-1;
        }
        if (pose > path.size()-1){
            pose = path.size()-1;
        }
        if (tNum > path.size()-1){
            tNum = path.size()-1;
        }
        if (vNum > path.size()-1){
            vNum = path.size()-1;
        }

        if (Math.sqrt(Math.pow(path.get(pose).getPose().getX() - x, 2) + Math.pow(path.get(pose).getPose().getY() - y, 2)) <= path.get(rNum).getRadius() &&
                Math.abs(path.get(pose).getPose().getHeading() - currentAngle) <= path.get(rNum).getRadius() + 2 &&
//                Math.abs(pDistance(x, y, endOfNextLineX, endOfNextLineY, startOfNextLineX, startOfNextLineY)) <= r[rNum] &&
                pose < path.size()-1 || timer.milliseconds() > timeOut){
            pose++;
            rNum++;
            tNum++;
            vNum++;

            if(rNum > path.size()-1){
                rNum = path.size()-1;
            }
            if (pose > path.size()-1){
                pose = path.size()-1;
            }
            if (tNum > path.size()-1){
                tNum = path.size()-1;
            }
            if (vNum > path.size()-1){
                vNum = path.size()-1;
            }

            first = 5;

            timeOut = path.get(vNum).getTimeOut();
            timer.reset();

            startOfNextLineX = path.get(pose).getPose().getX();
            startOfNextLineY = path.get(pose).getPose().getY();

            endOfNextLineX = path.get(pose).getPose().getX();
            endOfNextLineY = path.get(pose).getPose().getY();

            wantedPose = path.get(pose).getPose();
        }

        Log.i("pose size", "" + pose);

//        RobotLog.a("Distance From wanted pose: " + String.valueOf(Math.sqrt(Math.pow(path[pose].getX() - x, 2) + Math.pow(path[pose].getY() - y, 2))));
//        RobotLog.a("Distance From line: " + String.valueOf(pDistance(x, y, endOfNextLineX, endOfNextLineY, startOfNextLineX, startOfNextLineY)));

        double x_power = xPID.update(wantedPose.getX(), x);

        double y_power = yPID.update(wantedPose.getY(), y);

        double wA;
        if (wantedPose.getHeading() > 180){
            wA = wantedPose.getHeading() - 360;
        }else if (wantedPose.getHeading() < -180){
            wA = wantedPose.getHeading() + 360;
        }else
            wA = wantedPose.getHeading();

//        double turn = wA - Robot.getInstance().getMecanumDrive().getCurrentPose().getHeading();
        double turn = wA - currentAngle;

        if (turn > 180){
            turn = turn - 360;
        }else if (turn < -180){
            turn = turn + 360;
        }
        turn *= -1;

        double rotation = turnPID.update(turn, 0);

        Log.i("Current Angle", "" + currentAngle);
        Log.i("Wanted Angle", "" + wA);

        if (Math.abs(xPID.getError()) > 15){
            if (-xPID.getError() > 0)
                x_power = CruiseLib.limitValue(x_power, (-Math.signum(xPID.getError()) * maxVel), (-Math.signum(xPID.getError()) * 0.15));
            else
                x_power = CruiseLib.limitValue(x_power, (-Math.signum(xPID.getError()) * 0.15), (-Math.signum(xPID.getError()) * maxVel));
        }else {
            if (Math.abs(xPID.getError()) > Math.abs(tolerancePose.getX() - 1))
                x_power = CruiseLib.limitValue(x_power, -0.15, -maxVel, 0.15, maxVel);
            else
                x_power = 0;
        }

        if (Math.abs(yPID.getError()) > 15){
            if (-yPID.getError() > 0)
                y_power = CruiseLib.limitValue(y_power, (-Math.signum(yPID.getError()) * maxVel), (-Math.signum(yPID.getError()) * 0.15));
            else
                y_power = CruiseLib.limitValue(y_power, (-Math.signum(yPID.getError()) * 0.15), (-Math.signum(yPID.getError()) * maxVel));
        }else {
            if (Math.abs(yPID.getError()) > Math.abs(tolerancePose.getY() - 1))
                y_power = CruiseLib.limitValue(y_power, -0.15, -maxVel, 0.15, maxVel);
            else
                y_power = 0;
        }

        Log.i("X Error", "" + xPID.getError());
        Log.i("Y Error", "" + yPID.getError());
        Log.i("Heading Error", "" + turnPID.getError());

        if (Math.abs(turnPID.getError()) > Math.abs(tolerancePose.getHeading()))
            rotation = CruiseLib.limitValue(rotation, -.2, -maxVel, .2, maxVel);
        else
            rotation = 0;

        mDrive.setFieldCentricAutoPower(y_power, x_power, rotation);
    }

    @Override
    public boolean isFinished() {
        if (Math.abs(xPID.getError()) < Math.abs(tolerancePose.getX())) xCounter++; else xCounter = 0;
        if (Math.abs(yPID.getError()) < Math.abs(tolerancePose.getY())) yCounter++; else yCounter = 0;

        if (Math.abs(turnPID.getError()) < Math.abs(tolerancePose.getHeading()))
            angleCounter++;
        else angleCounter = 0;

        Log.i("X Counter", "" + xCounter);
        Log.i("Y Counter", "" + yCounter);
        Log.i("H Counter", "" + angleCounter);

        return (xCounter > 3 && yCounter > 3 && angleCounter > 3 && pose == path.size()-1) || timeOut < timer.milliseconds();
    }

    @Override
    public void done() {
        mDrive.setPower(0,0 ,0);
    }

    public double pDistance(double x, double y, double x1, double y1, double x2, double y2) {
        double A = x - x1; // position of point rel one end of line
        double B = y - y1;
        double C = x2 - x1; // vector along line
        double D = y2 - y1;
        double E = -D; // orthogonal vector
        double F = C;

        double dot = A * E + B * F;
        double len_sq = E * E + F * F;

        return Math.abs(dot) / Math.sqrt(len_sq);
    }
}
