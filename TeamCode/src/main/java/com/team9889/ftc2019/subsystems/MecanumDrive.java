package com.team9889.ftc2019.subsystems;

import android.util.Log;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.localization.ThreeTrackingWheelLocalizer;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.team9889.ftc2019.Constants;
import com.team9889.lib.android.FileReader;
import com.team9889.lib.android.FileWriter;
import com.team9889.lib.control.math.cartesian.Rotation2d;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Eric on 9/7/2019.
 */

public class MecanumDrive extends Subsystem {
    public double x, y, xSpeed, ySpeed;

    public Pose2d currentPose = new Pose2d();
    public Rotation2d gyroAngle = new Rotation2d();
    private double Right_Position_Offset = 0, Left_Position_Offset = 0, Y_Position_Offset = 0;
    public double angleFromAuton = 0;

    private Odometry odometry = new Odometry();

    private String filename = "gyro.txt";

    public ElapsedTime timer = new ElapsedTime();
    public Pose2d driftCalc = new Pose2d(0,0,0);
    public Pose2d velocityPose = new Pose2d(0,0,0);
    public Pose2d lastPoseOfRobotBeforeDriftCalc = new Pose2d(0,0,0);

    public boolean first = true, updated = false;

    @Override
    public void init(boolean auto) {
        if(auto) {
            Robot.getInstance().odometryLifter.setPosition(1);
            OpenFoundationHook();
            setCurrentPose(new Pose2d());
        } else {
            readAngleFromFile();
        }

        lastPoseOfRobotBeforeDriftCalc = currentPose;
        driftCalc = new Pose2d(0, 0, 0);
        timer.reset();
    }

    @Override
    public void outputToTelemetry(Telemetry telemetry) {
        Log.i("Pose Of Robot", "" + getCurrentPose());
        telemetry.addData("Left Encoder", "" + Robot.getInstance().leftLift.getPosition());
        telemetry.addData("Right Encoder", "" + Robot.getInstance().intakeLeft.getPosition());
        telemetry.addData("Side Encoder", "" + Robot.getInstance().intakeRight.getPosition());
//        telemetry.addData("Right Offset", "" + Right_Position_Offset);

        telemetry.addData("Side Encoder", Robot.getInstance().intakeRight.getPosition());

        telemetry.addData("Pose of Robot", getCurrentPose().toString());
        telemetry.addData("Adjusted Pose of Robot", getAdjustedPose().toString());
        telemetry.addData("Velocity Pose of Robot", velocityPose);

//        telemetry.addData("Change in X", currentPose.getX() - lastPoseOfRobotBeforeDriftCalc.getX());
//        telemetry.addData("Change in Y", currentPose.getY() - lastPoseOfRobotBeforeDriftCalc.getY());

        Log.i("Right Offset", "" + Right_Position_Offset);
        Log.i("Left Offset", "" + Left_Position_Offset);
        Log.i("Side Offset", "" + Y_Position_Offset);


    }

//    public static void main(String[] args) {
//        double timer = .025;
//        Pose2d velocityPose, currentPose = new Pose2d(200, 0.3, 1.7), lastPoseOfRobotBeforeDriftCalc = new Pose2d(199, 0.3, 1.4);
//
//        double adjustValue = 0.01;
//        if(timer > 0)
//            velocityPose = currentPose.minus(lastPoseOfRobotBeforeDriftCalc).div(timer).times(adjustValue);
//        else
//            velocityPose = currentPose.minus(lastPoseOfRobotBeforeDriftCalc).div(20 / 1000).times(adjustValue);
////        lastPoseOfRobotBeforeDriftCalc = currentPose;
////        driftCalc = driftCalc.plus(velocityPose);
//        System.out.println(velocityPose);
//    }

    @Override
    public void update() {
        odometry.update();

        double adjustValue = -0.0002;
//        double adjustValue = 0.0;
        if(timer.milliseconds() > 0)
            velocityPose = currentPose.minus(lastPoseOfRobotBeforeDriftCalc).div(timer.seconds()).times(adjustValue);
        else
            velocityPose = currentPose.minus(lastPoseOfRobotBeforeDriftCalc).div(20 / 1000).times(adjustValue);
        lastPoseOfRobotBeforeDriftCalc = currentPose;
        timer.reset();
        driftCalc = driftCalc.plus(velocityPose);

        if (updated) {
            setCurrentPose(new Pose2d(odometry.getPoseEstimate().getX(),
                    odometry.getPoseEstimate().getY(),
                    gyroAngle.getTheda(AngleUnit.RADIANS)));
//            updated = false;
        }
        odometry.update();
//        else {

        updated = true;
//            currentPose = odometry.getPoseEstimate();
//        }

//        if (first){
//            resetOdometryEncoders();
//            first = false;
//        }
    }

    private void resetOdometryEncoders() {
        Right_Position_Offset = Right_OdometryPosition();
        Left_Position_Offset = Left_OdometryPosition();
        Y_Position_Offset = Y_OdometryPosition();
    }

    public double Right_OdometryPosition() {
        return (Robot.getInstance().intakeLeft.getPosition() * Constants.OdometryConstants.ENCODER_TO_DISTANCE_RATIO) - Right_Position_Offset;
    }

    public double Left_OdometryPosition() {
        return (Robot.getInstance().leftLift.getPosition() * Constants.OdometryConstants.ENCODER_TO_DISTANCE_RATIO) - Left_Position_Offset;
    }

    public double Y_OdometryPosition() {
        return (Robot.getInstance().intakeRight.getPosition() * Constants.OdometryConstants.ENCODER_TO_DISTANCE_RATIO) - Y_Position_Offset;
    }

    public Pose2d getCurrentPose() {
        return currentPose;
    }
    public Pose2d getAdjustedPose(){
        return currentPose.plus(driftCalc);
    }

    public void setCurrentPose(Pose2d pose) {
        resetOdometryEncoders();
        odometry.setPoseEstimate(pose);
        currentPose = odometry.getPoseEstimate();
    }

    public Rotation2d getAngle(){
        try {
            gyroAngle.setTheda(-Robot.getInstance().imu.getNormalHeading(), AngleUnit.DEGREES);
            return gyroAngle;
        } catch (Exception e){
            return new Rotation2d(0, AngleUnit.DEGREES);
        }
    }

    public void writeAngleToFile() {
        FileWriter angleWriter = new FileWriter(filename);
        angleWriter.write(gyroAngle.getTheda(AngleUnit.RADIANS));
        angleWriter.close();
    }

    public void readAngleFromFile() {
        try {
            FileReader angleReader = new FileReader(filename);
            String[] rows = angleReader.lines();
            String value = rows[rows.length - 1];
            angleFromAuton = Double.parseDouble(value);
            angleReader.close();
        } catch (NumberFormatException e) {
            angleFromAuton = 0;
        }
    }

    public void setFieldCentricPower(double x, double y, double rotation){
        double angle = getAngle().getTheda(AngleUnit.RADIANS);

        double angleFromAuto = Robot.getInstance().getMecanumDrive().angleFromAuton;
        double xMod = x * Math.cos(angle - angleFromAuto) - y * Math.sin(angle - angleFromAuto);
        double yMod = x * Math.sin(angle - angleFromAuto) + y * Math.cos(angle - angleFromAuto);

        setPower(xMod, yMod, rotation);
    }

    public void setFieldCentricAutoPower(double x, double y, double rotation){
        double xMod = x * gyroAngle.cos() - y * gyroAngle.sin();
        double yMod = x * gyroAngle.sin() + y * gyroAngle.cos();
        setPower(xMod, yMod, rotation);
    }

    public void setPower(double leftStickX, double leftStickY, double rightStickX){
        double r = Math.hypot(leftStickX, leftStickY);
        double robotAngle = Math.atan2(leftStickY, leftStickX) - Math.PI / 4;
        double rightX = rightStickX;
        final double v1 = r * Math.cos(robotAngle) + rightX;
        final double v2 = r * Math.sin(robotAngle) - rightX;
        final double v3 = r * Math.sin(robotAngle) + rightX;
        final double v4 = r * Math.cos(robotAngle) - rightX;

        Robot.getInstance().fLDrive.setPower(v1);
        Robot.getInstance().fRDrive.setPower(v2);
        Robot.getInstance().bLDrive.setPower(v3);
        Robot.getInstance().bRDrive.setPower(v4);
        Log.i("FRDrive", "" + v2);
        Log.i("BRDrive", "" + v4);
        Log.i("FLDrive", "" + v1);
        Log.i("BLDrive", "" + v3);
    }

    public double getSpeed(double xPosition, double yPosition, double rotation){
        double r = Math.hypot(xPosition, yPosition);
        double robotAngle = Math.atan2(yPosition, xPosition) - Math.PI / 4;
        double rightX = rotation;
        return (r * Math.cos(robotAngle) + rightX) * 1.414;
    }

//    public static void main(String[] args) {
//        double r = Math.hypot(0, 4.149773633411362);
//        double robotAngle = Math.atan2(4.149773633411362, 0) - Math.PI / 4;
//        double rightX = 0;
//        System.out.println(r * Math.cos(robotAngle) + rightX);
//        System.out.println(r * Math.sin(robotAngle) - rightX);
//        System.out.println(r * Math.sin(robotAngle) + rightX);
//        System.out.println(r * Math.cos(robotAngle) - rightX);
//    }

    public void OpenFoundationHook(){
        Robot.getInstance().foundationHook.setPosition(.9);
    }
    public void CloseFoundationHook(){
        Robot.getInstance().foundationHook.setPosition(0.55);
    }

    public class Odometry extends ThreeTrackingWheelLocalizer {

        private static final double LATERAL_DISTANCE = 7.25;
        private static final double FORWARD_OFFSET = 2.5 + (1.0/16.0);

        Odometry() {
            super(Arrays.asList(
                    new Pose2d(-1.375, -LATERAL_DISTANCE - 0.25, Math.toRadians(180)),
                    new Pose2d(FORWARD_OFFSET, LATERAL_DISTANCE + 0.25, Math.toRadians(0)),
                    new Pose2d(0.25, LATERAL_DISTANCE, Math.toRadians(-90))
            ));
        }

        @Override
        public List<Double> getWheelPositions() {
            return Arrays.asList(
                    Robot.getInstance().getMecanumDrive().Left_OdometryPosition(),
                    Robot.getInstance().getMecanumDrive().Right_OdometryPosition(),
                    Robot.getInstance().getMecanumDrive().Y_OdometryPosition()
            );
        }
    }
}
