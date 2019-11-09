package com.team9889.ftc2019.subsystems;

import com.team9889.lib.detectors.SkyStoneDetector;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Created by MannoMation on 10/27/2018.
 */

public class Camera extends Subsystem{
    private SkyStoneDetector detector;

    public enum GoldPositions{
        LEFT, CENTER, RIGHT, UNKNOWN
    }

    private GoldPositions gold = GoldPositions.UNKNOWN;

    public enum CameraPositions{
        FRONTCENTER, FRONTRIGHT, FRONTLEFT,
        FRONTHOPPER, BACKHOPPER,
        STORED, UPRIGHT,
        TWO_GOLD, TELEOP
    }


    @Override
    public void init(boolean auto) {
        if (auto) {
            setCameraPosition(CameraPositions.STORED);
        }
    }

    public void zeroSensors() {

    }


    @Override
    public void outputToTelemetry(Telemetry telemetry) {

    }


    @Override
    public void update() {

    }

    private double xPosition, yPosition;

    public void setXYAxisPosition(double xPos, double yPos) {
        xPosition = xPos;
        yPosition = yPos;
    }

    public double getGoldPosition(){
        return detector.getXPosition();
    }

    public GoldPositions getGold() {
        return gold;
    }

    public void setGold(GoldPositions gold) {
        this.gold = gold;
    }

    public void setCameraPosition(CameraPositions position){
        switch (position){
            case STORED:
                setXYAxisPosition(0, 0.1);
                break;

            case UPRIGHT:
                setXYAxisPosition(0, 0.5);
                break;

            case BACKHOPPER:
                setXYAxisPosition(1,0.9);
                break;

            case FRONTRIGHT:
                setXYAxisPosition(.265, .75);
                break;

            case FRONTCENTER:
                setXYAxisPosition(.075, 0.8);
                break;

            case FRONTLEFT:
                setXYAxisPosition(.0, 0.);

            case FRONTHOPPER:
                setXYAxisPosition(0, 1);
                break;

            case TWO_GOLD:
                setXYAxisPosition(0, 0.6);
                break;

            case TELEOP:
                setXYAxisPosition(1, .95);
        }
    }

//    @Override
    public void stop() {
        
    }

    @Override
    public String toString() {
        return "Camera";
    }

}
