package com.team9889.lib;

import com.acmerobotics.roadrunner.geometry.Pose2d;

import org.opencv.core.Point;

/**
 * Created by Eric on 1/17/2020.
 */
public class Path {
    Pose2d pose, tolerancePose;
    double radius, maxVelocity, maxTurnVelocity;
    int timeOut = 30000;

    public Path(Pose2d pose, Pose2d tolerancePose, double radius, double maxVelocity){
        this.pose = pose;
        this.tolerancePose = tolerancePose;
        this.radius = radius;
        this.maxVelocity = maxVelocity;
        this.maxTurnVelocity = maxVelocity;
    }

    public Path(Pose2d pose, Pose2d tolerancePose, double radius, double maxVelocity, int timeOut){
        this.pose = pose;
        this.tolerancePose = tolerancePose;
        this.radius = radius;
        this.maxVelocity = maxVelocity;
        this.maxTurnVelocity = maxVelocity;
        this.timeOut = timeOut;
    }

    public Path(Path path){
        this.pose = path.pose;
        this.tolerancePose = path.tolerancePose;
        this.radius = path.radius;
        this.maxVelocity = path.maxVelocity;
        this.maxTurnVelocity = maxVelocity;
    }

    public Pose2d getPose(){
        return pose;
    }
    public Point getPoint() {return new Point(pose.getX(), pose.getY());}
    public Pose2d getTolerancePose(){
        return tolerancePose;
    }
    public double getRadius(){
        return radius;
    }
    public double getMaxVelocity(){
        return maxVelocity;
    }
    public double getMaxTurnVelocity() {return  maxTurnVelocity;}
    public int getTimeOut(){
        return timeOut;
    }
}
