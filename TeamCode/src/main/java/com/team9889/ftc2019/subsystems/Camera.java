package com.team9889.ftc2019.subsystems;

import android.graphics.Bitmap;

import com.team9889.lib.VuMark;
import com.team9889.lib.detectors.SkyStoneDetector;
import com.vuforia.Image;
import com.vuforia.PIXEL_FORMAT;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

/**
 * Created by MannoMation on 10/27/2018.
 */

public class Camera extends Subsystem{
    public SkyStoneDetector detector;

    @Override
    public void init(boolean auto) {
        if (auto) {
            detector = new SkyStoneDetector();
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

    public void getImage(){
        Image bmp = VuMark.getImage();
        Mat tmp = null;

        if(bmp != null) {
            tmp = new Mat(bmp.getHeight(), bmp.getWidth(), CvType.CV_8UC1);
        }
        detector.process(tmp);
    }

    public double getSkyStonePosition(){
        getImage();

        double skyStonePosition = detector.getXPosition();

        if (skyStonePosition < 100){
            return 1;
        }else if (skyStonePosition > 99 && skyStonePosition < 200){
            return 2;
        }
        else {
            return 3;
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
