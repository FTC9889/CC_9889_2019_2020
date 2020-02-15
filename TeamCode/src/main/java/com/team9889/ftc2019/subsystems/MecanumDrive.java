package com.team9889.ftc2019.subsystems;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.localization.ThreeTrackingWheelLocalizer;
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

    @Override
    public void init(boolean auto) {
        if(auto) {
            Robot.getInstance().odometryLifter.setPosition(1);
            OpenFoundationHook();
            setCurrentPose(new Pose2d());
        } else {
            readAngleFromFile();
        }
    }

    @Override
    public void outputToTelemetry(Telemetry telemetry) {
//        telemetry.addData("Pose of Robot", getCurrentPose().toString());
    }

    @Override
    public void update() {
        odometry.update();
        currentPose = odometry.getPoseEstimate();
    }

    private void resetOdometryEncoders() {
        Right_Position_Offset = Right_OdometryPosition();
        Left_Position_Offset = Left_OdometryPosition();
        Y_Position_Offset = Y_OdometryPosition();
    }

    public double Right_OdometryPosition() {
        return (-Robot.getInstance().intakeRight.getPosition() * Constants.OdometryConstants.ENCODER_TO_DISTANCE_RATIO) - Right_Position_Offset;
    }

    public double Left_OdometryPosition() {
        return (-Robot.getInstance().leftLift.getPosition() * Constants.OdometryConstants.ENCODER_TO_DISTANCE_RATIO) - Left_Position_Offset;
    }

    public double Y_OdometryPosition() {
        return (Robot.getInstance().intakeLeft.getPosition() * Constants.OdometryConstants.ENCODER_TO_DISTANCE_RATIO) - Y_Position_Offset;
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
        double xMod = x * Math.cos(angle + angleFromAuto) - y * Math.sin(angle + angleFromAuto);
        double yMod = x * Math.sin(angle + angleFromAuto) + y * Math.cos(angle + angleFromAuto);

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
        double r = Math.hypot(0, 4.149773633411362);
        double robotAngle = Math.atan2(4.149773633411362, 0) - Math.PI / 4;
        double rightX = 0;
        System.out.println(r * Math.cos(robotAngle) + rightX);
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

    public class Odometry extends ThreeTrackingWheelLocalizer {

        private static final double LATERAL_DISTANCE = 7.25;
        private static final double FORWARD_OFFSET = 2.75 + (1.0/16.0);

        Odometry() {
            super(Arrays.asList(
                    new Pose2d(-1.375, -LATERAL_DISTANCE - 0.25, Math.toRadians(0)),
                    new Pose2d(FORWARD_OFFSET + 0.25, LATERAL_DISTANCE + 0.25, Math.toRadians(0)),
                    new Pose2d(0.25, LATERAL_DISTANCE, Math.toRadians(90))
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
