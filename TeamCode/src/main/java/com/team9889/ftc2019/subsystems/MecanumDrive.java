package com.team9889.ftc2019.subsystems;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.localization.TwoTrackingWheelLocalizer;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.team9889.ftc2019.Constants;
import com.team9889.lib.android.FileWriter;
import com.team9889.lib.control.math.cartesian.Pose;
import com.team9889.lib.control.math.cartesian.Rotation2d;
import com.team9889.lib.control.math.cartesian.Vector2d;

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
    private double X_Position_Offset = 0, Y_Position_Offset = 0;
    public double angleOffset = 0;

    private Odometry odometry = new Odometry();

    @Override
    public void init(boolean auto) {
        if(auto) {
            Robot.getInstance().odometryLifter.setPosition(1);
            OpenFoundationHook();
            setCurrentPose(new Pose2d());
        }
    }

    @Override
    public void outputToTelemetry(Telemetry telemetry) {
        telemetry.addData("Pose of Robot", getCurrentPose().toString());
    }

    @Override
    public void update() {
        odometry.update();
        currentPose = odometry.getPoseEstimate();
    }

    private void resetOdometryEncoders() {
        X_Position_Offset = X_OdometryPosition();
        Y_Position_Offset = Y_OdometryPosition();
    }

    private double X_OdometryPosition() {
        return ((-Robot.getInstance().intakeRight.getPosition()) - X_Position_Offset) * Constants.OdometryConstants.ENCODER_TO_DISTANCE_RATIO;
    }

    private double Y_OdometryPosition() {
        return (Robot.getInstance().intakeLeft.getPosition() - Y_Position_Offset) * Constants.OdometryConstants.ENCODER_TO_DISTANCE_RATIO;
    }

    public Pose2d getCurrentPose() {
        return currentPose;
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

    public void setFieldCentricPower(double x, double y, double rotation){
        double angle = getAngle().getTheda(AngleUnit.RADIANS);

        double xMod = x * Math.cos(angle + Robot.getInstance().gyroAfterAuto) - y * Math.sin(angle + Robot.getInstance().gyroAfterAuto);
        double yMod = x * Math.sin(angle + Robot.getInstance().gyroAfterAuto) + y * Math.cos(angle + Robot.getInstance().gyroAfterAuto);
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
    }

    public double getSpeed(double xPosition, double yPosition, double rotation){
        double r = Math.hypot(xPosition, yPosition);
        double robotAngle = Math.atan2(yPosition, xPosition) - Math.PI / 4;
        double rightX = rotation;
        return (r * Math.cos(robotAngle) + rightX) * 1.414;
    }

    public static void main(String[] args) {
        double r = Math.hypot(24, -24);
        double robotAngle = Math.atan2(-24, 24) - Math.PI / 4;
        double rightX = 0;
        System.out.println((r * Math.cos(robotAngle) + rightX) * 1.414);
        System.out.println(r * Math.sin(robotAngle) - rightX);
        System.out.println(r * Math.sin(robotAngle) + rightX);
        System.out.println(r * Math.cos(robotAngle) - rightX);
    }

    public void OpenFoundationHook(){
        Robot.getInstance().foundationHook.setPosition(.9);
    }
    public void CloseFoundationHook(){
        Robot.getInstance().foundationHook.setPosition(0.45);
    }

    public class Odometry extends TwoTrackingWheelLocalizer {

        private static final double LATERAL_DISTANCE = 7.5;
        private static final double FORWARD_OFFSET = 2.75;

        Odometry() {
            super(Arrays.asList(
                    new Pose2d(0, LATERAL_DISTANCE, Math.toRadians(90)), // Right
                    new Pose2d(FORWARD_OFFSET, LATERAL_DISTANCE, Math.toRadians(0)) // Sideways
            ));
        }

        @Override
        public double getHeading() {
            return Robot.getInstance().getMecanumDrive().gyroAngle.getTheda(AngleUnit.RADIANS);
        }

        @Override
        public List<Double> getWheelPositions() {
            return Arrays.asList(
                    Robot.getInstance().getMecanumDrive().X_OdometryPosition(),
                    Robot.getInstance().getMecanumDrive().Y_OdometryPosition()
            );
        }
    }
}
