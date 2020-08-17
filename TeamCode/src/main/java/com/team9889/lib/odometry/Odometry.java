package com.team9889.lib.odometry;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.ReadWriteFile;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.opencv.core.Mat;

import java.io.File;
import java.util.List;

/**
 * Created by Sarthak on 6/1/2019.
 */
public class Odometry {

//    Left, Right, Middle
    public double[] odometryValues;

    public Pose2d[] odometryOffsets;

    //Thead run condition
    private boolean isRunning = true;

    //Position variables used for storage and calculations
    double verticalRightEncoderWheelPosition = 0, verticalLeftEncoderWheelPosition = 0, normalEncoderWheelPosition = 0,  changeInRobotOrientation = 0;
    private double robotGlobalXCoordinatePosition = 0, robotGlobalYCoordinatePosition = 0, robotOrientationRadians = 0;
    private double previousVerticalRightEncoderWheelPosition = 0, previousVerticalLeftEncoderWheelPosition = 0, prevNormalEncoderWheelPosition = 0;

    //Algorithm constants
    private double robotEncoderWheelDistance;
    private double horizontalEncoderTickPerDegreeOffset;

    //Sleep time interval (milliseconds) for the position update thread
    private int sleepTime;

    //Files to access the algorithm constants
//    private File wheelBaseSeparationFile = AppUtil.getInstance().getSettingsFile("wheelBaseSeparation.txt");
//    private File horizontalTickOffsetFile = AppUtil.getInstance().getSettingsFile("horizontalTickOffset.txt");

    private int verticalLeftEncoderPositionMultiplier = 1;
    private int verticalRightEncoderPositionMultiplier = 1;
    private int normalEncoderPositionMultiplier = 1;

    public ElapsedTime timer = new ElapsedTime();
    public double leftDriftCalc, leftVelocityPose, leftLastPoseOfRobotBeforeDriftCalc;
    public double rightDriftCalc, rightVelocityPose, rightLastPoseOfRobotBeforeDriftCalc;
    public double angleDriftCalc, angleVelocityPose, angleLastPoseOfRobotBeforeDriftCalc;

    public double[] offset = new double[3];

//    /**
//     * Constructor for GlobalCoordinatePosition Thread
//     * @param verticalEncoderLeft left odometry encoder, facing the vertical direction
//     * @param verticalEncoderRight right odometry encoder, facing the vertical direction
//     * @param horizontalEncoder horizontal odometry encoder, perpendicular to the other two odometry encoder wheels
//     * @param threadSleepDelay delay in milliseconds for the GlobalPositionUpdate thread (50-75 milliseconds is suggested)
//     */
//    public OdometryGlobalCoordinatePosition(DcMotor verticalEncoderLeft, DcMotor verticalEncoderRight, DcMotor horizontalEncoder, double COUNTS_PER_INCH, int threadSleepDelay){
//        this.verticalEncoderLeft = verticalEncoderLeft;
//        this.verticalEncoderRight = verticalEncoderRight;
//        this.horizontalEncoder = horizontalEncoder;
//        sleepTime = threadSleepDelay;
//
//        robotEncoderWheelDistance = Double.parseDouble(ReadWriteFile.readFile(wheelBaseSeparationFile).trim()) * COUNTS_PER_INCH;
//        this.horizontalEncoderTickPerDegreeOffset = Double.parseDouble(ReadWriteFile.readFile(horizontalTickOffsetFile).trim());
//
//    }

    /**
     * Updates the global (x, y, theta) coordinate position of the robot using the odometry encoders
     */
    public void update(){
        //Get Current Positions
        verticalLeftEncoderWheelPosition = ((odometryValues[0] + leftDriftCalc - offset[0]) * verticalLeftEncoderPositionMultiplier);
        verticalRightEncoderWheelPosition = ((odometryValues[1] + rightDriftCalc - offset[1]) * verticalRightEncoderPositionMultiplier);
        normalEncoderWheelPosition = ((odometryValues[2] + angleDriftCalc - offset[2]) * normalEncoderPositionMultiplier);

        double leftAdjustValue = -0.0002;
        if(timer.milliseconds() > 0)
            leftVelocityPose = ((odometryValues[0] - leftLastPoseOfRobotBeforeDriftCalc) / timer.seconds()) * leftAdjustValue;
        else
            leftVelocityPose = ((odometryValues[0] - leftLastPoseOfRobotBeforeDriftCalc) / (20 / 1000)) * leftAdjustValue;
        leftLastPoseOfRobotBeforeDriftCalc = odometryValues[0];
        leftDriftCalc = leftDriftCalc + leftVelocityPose;


        double rightAdjustValue = -0.0002;
        if(timer.milliseconds() > 0)
            rightVelocityPose = ((odometryValues[1] - rightLastPoseOfRobotBeforeDriftCalc) / timer.seconds()) * rightAdjustValue;
        else
            rightVelocityPose = ((odometryValues[1] - rightLastPoseOfRobotBeforeDriftCalc) / (20 / 1000)) * rightAdjustValue;
        rightLastPoseOfRobotBeforeDriftCalc = odometryValues[1];
        rightDriftCalc = rightDriftCalc + rightVelocityPose;


        double angleAdjustValue = -0.0002;
        if(timer.milliseconds() > 0)
            angleVelocityPose = ((odometryValues[2] - angleLastPoseOfRobotBeforeDriftCalc) / timer.seconds()) * angleAdjustValue;
        else
            angleVelocityPose = ((odometryValues[2] - angleLastPoseOfRobotBeforeDriftCalc) / (20 / 1000)) * angleAdjustValue;
        angleLastPoseOfRobotBeforeDriftCalc = odometryValues[2];
        angleDriftCalc = angleDriftCalc + angleVelocityPose;

        timer.reset();

        double leftChange = verticalLeftEncoderWheelPosition - previousVerticalLeftEncoderWheelPosition;
        double rightChange = verticalRightEncoderWheelPosition - previousVerticalRightEncoderWheelPosition;

        //Calculate Angle
        changeInRobotOrientation = (leftChange - rightChange) / (robotEncoderWheelDistance);
        robotOrientationRadians = ((robotOrientationRadians + changeInRobotOrientation));

        //Get the components of the motion
        double rawHorizontalChange = normalEncoderWheelPosition - prevNormalEncoderWheelPosition;
        double horizontalChange = rawHorizontalChange - (changeInRobotOrientation * horizontalEncoderTickPerDegreeOffset);

        double p = ((rightChange + leftChange) / 2);
        double n = horizontalChange;
//        double n = 0;

        //Calculate and update the position values
        robotGlobalYCoordinatePosition = robotGlobalYCoordinatePosition + (p * Math.sin(robotOrientationRadians) + n * Math.cos(robotOrientationRadians));
        robotGlobalXCoordinatePosition = robotGlobalXCoordinatePosition + (p * Math.cos(robotOrientationRadians) - n * Math.sin(robotOrientationRadians));

        previousVerticalLeftEncoderWheelPosition = verticalLeftEncoderWheelPosition;
        previousVerticalRightEncoderWheelPosition = verticalRightEncoderWheelPosition;
        prevNormalEncoderWheelPosition = normalEncoderWheelPosition;
    }

    /**
     * Returns the robot's global x coordinate
     * @return global x coordinate
     */
    public double returnXCoordinate(){ return robotGlobalXCoordinatePosition; }

    /**
     * Returns the robot's global y coordinate
     * @return global y coordinate
     */
    public double returnYCoordinate(){ return robotGlobalYCoordinatePosition; }

    /**
     * Returns the robot's global orientation
     * @return global orientation, in degrees
     */
    public double returnOrientation(){ return Math.toDegrees(robotOrientationRadians) % 360; }
//
//    /**
//     * Stops the position update thread
//     */
//    public void stop(){ isRunning = false; }

    public void reverseLeftEncoder(){
        if(verticalLeftEncoderPositionMultiplier == 1){
            verticalLeftEncoderPositionMultiplier = -1;
        }else{
            verticalLeftEncoderPositionMultiplier = 1;
        }
    }

    public void reverseRightEncoder(){
        if(verticalRightEncoderPositionMultiplier == 1){
            verticalRightEncoderPositionMultiplier = -1;
        }else{
            verticalRightEncoderPositionMultiplier = 1;
        }
    }

    public void reverseNormalEncoder(){
        if(normalEncoderPositionMultiplier == 1){
            normalEncoderPositionMultiplier = -1;
        }else{
            normalEncoderPositionMultiplier = 1;
        }
    }

//    /**
//     * Runs the thread
//     */
//    @Override
//    public void run() {
//        while(isRunning) {
//            globalCoordinatePositionUpdate();
//            try {
//                Thread.sleep(sleepTime);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    /**
     * Returns the positions of the tracking wheels in the desired distance units (not encoder counts!)
     */
    public double[] OdometryValues(double leftWheel, double rightWheel, double middleWheel){
        return new double[] {leftWheel, rightWheel, middleWheel};
    }

    public void setOdometryOffsets(Pose2d[] offsets){
        odometryOffsets = offsets;
        robotEncoderWheelDistance = Math.abs(offsets[0].getY()) + Math.abs(offsets[1].getY());
        horizontalEncoderTickPerDegreeOffset = 20;
    }
}