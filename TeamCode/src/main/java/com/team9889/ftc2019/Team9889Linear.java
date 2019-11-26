package com.team9889.ftc2019;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.RobotLog;
import com.team9889.ftc2019.auto.actions.Action;
import com.team9889.ftc2019.subsystems.Robot;
import com.team9889.lib.detectors.ScanForSkyStonesPipeline;

import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by joshua9889 on 3/28/2018.
 *
 * This class extends LinearOpMode and makes it
 * easier to make code for the robot and not copy
 * and pasting init code.
 */

public abstract class Team9889Linear extends LinearOpMode {

    // Robot Object
    protected Robot Robot = com.team9889.ftc2019.subsystems.Robot.getInstance();

    // Match Timer
    protected ElapsedTime matchTime = new ElapsedTime();

    // Background
    private View relativeLayout;

    OpenCvInternalCamera phoneCam;
    public double positionOfSkyStone;

    public void waitForStart(boolean autonomous) {
        int relativeLayoutId = hardwareMap.appContext.getResources().
                getIdentifier("RelativeLayout", "id", hardwareMap.appContext.getPackageName());
        relativeLayout = ((Activity) hardwareMap.appContext).findViewById(relativeLayoutId);

        Robot.init(hardwareMap, autonomous);

        telemetry.setMsTransmissionInterval(autonomous ? 50:1000);
        matchTime.reset();

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        phoneCam = OpenCvCameraFactory.getInstance().createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);
        phoneCam.openCameraDevice();
        ScanForSkyStonesPipeline pipeline = new ScanForSkyStonesPipeline();
        phoneCam.setPipeline(pipeline);
        phoneCam.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);


        if(autonomous){
            setBackground(Color.GREEN);

            // Autonomous Init Loop code
            while(isInInitLoop()){
                telemetry.addData("Waiting for Start","");
                positionOfSkyStone = pipeline.getPositionOfSkyStone();
                telemetry.addData("Position", pipeline.getPositionOfSkyStone());
                Robot.outputToTelemetry(telemetry);
                telemetry.update();
            }
            phoneCam.stopStreaming();
        } else {
            // Teleop Init Loop code
            while(isInInitLoop()){
                telemetry.addData("Waiting for Start","");
                telemetry.update();
            }
        }

        matchTime.reset();
    }

    /**
     * Used to stop everything (Robot and OpMode).
     */
    protected void finalAction(){
        resetBackground();
        Robot.stop();
        requestOpModeStop();
    }

    /**
     * @return Is the robot waiting for start
     */
    private synchronized boolean isInInitLoop(){
        return !isStarted() && !isStopRequested();
    }

    public void setBackground(final int color){
        relativeLayout.post(new Runnable() {
            public void run() {
                relativeLayout.setBackgroundColor(color);
            }
        });
    }

    public void resetBackground() {
        setBackground(Color.WHITE);
    }

    public void setBackgroundHSV(final float[] values) {
        setBackground(Color.HSVToColor(0xff, values));
    }

    public void runAction(Action action){
        if(opModeIsActive())
            action.start();

        while (!action.isFinished() && opModeIsActive()) {
            action.update();
        }

        if(opModeIsActive())
            action.done();
    }

    public void ThreadAction(final Action action){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                runAction(action);
            }
        };

        if(opModeIsActive() && !isStopRequested())
            new Thread(runnable).start();
    }

    public void setBackgroundRGB(final int[] values) {
        float[] hsv = new float[3];
        Color.RGBToHSV(values[0], values[1], values[2], hsv);

        setBackgroundHSV(hsv);
    }
}
