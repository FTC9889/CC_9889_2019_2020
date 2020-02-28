package com.team9889.lib;

import com.acmerobotics.roadrunner.geometry.Pose2d;

/**
 * Created by Eric on 1/17/2020.
 */
public class FollowPath {
    Pose2d pose, tollerancePose;
    double radius, maxVelocity;
    int timeOut = 30000;

    public FollowPath(Pose2d pose, Pose2d tollerancePose, double radius, double maxVelocity){
        this.pose = pose;
        this.tollerancePose = tollerancePose;
        this.radius = radius;
        this.maxVelocity = maxVelocity;
    }

    public FollowPath(Pose2d pose, Pose2d tollerancePose, double radius, double maxVelocity, int timeOut){
        this.pose = pose;
        this.tollerancePose = tollerancePose;
        this.radius = radius;
        this.maxVelocity = maxVelocity;
        this.timeOut = timeOut;
    }

    public FollowPath(FollowPath path){
        this.pose = path.pose;
        this.tollerancePose = path.tollerancePose;
        this.radius = path.radius;
        this.maxVelocity = path.maxVelocity;
    }

    public Pose2d getPose(){
        return pose;
    }
    public Pose2d getTollerancePose(){
        return tollerancePose;
    }
    public double getRadius(){
        return radius;
    }
    public double getMaxVelocity(){
        return maxVelocity;
    }
    public int getTimeOut(){
        return timeOut;
    }
}
