package com.team9889.lib.control.controllers;

/**
 * Created by Eric on 11/7/2019.
 */
public class cruiseController extends FeedBackController{
    private double tuner, maxSpeed, minSpeed;
    private double error, output;
    private double priorError;

    public cruiseController(double tuner, double minSpeed){
        this.tuner = tuner;
        this.minSpeed = minSpeed;
    }

    public cruiseController(double tuner, double minSpeed, double maxSpeed){
        this.tuner = tuner;
        this.minSpeed = minSpeed;
        this.maxSpeed = maxSpeed;
    }

    @Override
    public double update(double current, double wanted) {
        error = wanted - current;

        if (Math.abs(error) > Math.abs(wanted) / 3){
            output = tuner * (Math.abs(current) / wanted * 16 + (wanted / Math.abs(wanted) * 0.1));
        }
        else {
            output = tuner * (wanted / Math.abs(current) / 2);
        }

        if (Math.abs(output) < minSpeed)
            output = (error / Math.abs(error)) * minSpeed;

        priorError = error;
        return output;
    }
}
