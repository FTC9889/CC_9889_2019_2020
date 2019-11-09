package com.team9889.ftc2019.subsystems;

import com.team9889.ftc2019.Constants;
import com.team9889.lib.control.math.cartesian.Pose;
import com.team9889.lib.control.math.cartesian.Rotation2d;
import com.team9889.lib.hardware.RevIMU;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.opencv.core.Mat;

/**
 * Created by Eric on 9/7/2019.
 */

public class MecanumDrive extends Subsystem {

    private double currentAngle, wantedAngle;
    private double xEncoderOffset, yEncoderOffset, yDirection;
    private double xPositionTicks, yPositionTicks, radius;

    public boolean setStraightPositionActive = false;
    public boolean setCurvePositionActive = false;
    public double x, y, xSpeed, ySpeed, lastXSpeed, lastYSpeed = 0;
    public double curve, circ, distance, unit, nextMilestone, travelTotal, travelDuringMilestone = 0;
    private double speed = 1;

    private double gyro;

    private double curveTimerOffset = 0;

    private boolean xDirectionRight;
    private boolean yDirectionFoward;

    public double test = 0;

    private static Pose currentPose = new Pose();

    public double frontLeft, frontRight, backLeft, backRight;
    public int frontLeftInt, frontRightInt, backLeftInt, backRightInt;

    public double xAutoPower, yAutoPower;

    @Override
    public void init(boolean auto) {

    }

    @Override
    public void outputToTelemetry(Telemetry telemetry) {
        telemetry.addData("encoder", Robot.getInstance().fRDrive.getPosition());
    }

    @Override
    public void update() {


        if (setStraightPositionActive){
            if ((Robot.getInstance().fLDrive.getPosition() + Robot.getInstance().fRDrive.getPosition() / 2) - xEncoderOffset < Math.abs(xPositionTicks)) {
                xSpeed = .5;
            } else
                xSpeed = 0;
            if (Robot.getInstance().bLDrive.getPosition() - yEncoderOffset < yPositionTicks){
                ySpeed = .5;
            } else
                ySpeed = 0;

                setPower(xSpeed, ySpeed, wantedAngle - currentAngle);
                lastXSpeed = xSpeed;
                lastYSpeed = ySpeed;

            if (xSpeed == 0 && ySpeed == 0 && wantedAngle == currentAngle){
                setStraightPositionActive = false;
            }
        }else if (setCurvePositionActive && Robot.timer.milliseconds() > (100 + curveTimerOffset)){
            if (travelTotal > nextMilestone) {
                double factor = (travelTotal/distance); 	//Percent distance travelled (ex: 5/50 = .1 or 10% travelled)
                double yFactor = (1-factor); 		//Percent distance not travelled (ex: 1-5/50 = .9 or 90% not travelled)
                double xFactor = factor;

                y += (travelDuringMilestone * yFactor);	//Increase the total distance travelled along the Y-axis
                x += (travelDuringMilestone * xFactor);	//Increase the total distance travelled along the X-axis

                ySpeed = (speed * yFactor);		//Speed along the Y-axis
                xSpeed = (speed * xFactor);		//Speed along the X-axis

                nextMilestone += unit;			//Increase the milestone by 1 unit
                travelDuringMilestone = 0;		//Reset distance travelled since the last milestone
            }

            travelDuringMilestone += speed;			//Increase the distance travelled since the last milestone
            travelTotal += speed;				//Most likely not needed; you have a means of tracking the distance travelled.

            setPower(xSpeed, ySpeed, wantedAngle);

            curveTimerOffset = Robot.timer.milliseconds();

            if (travelTotal > (distance * curve)) {
                xSpeed = 0;
                ySpeed = 0;
                setPower(0, 0, 0);
                setCurvePositionActive = false;
            }
        }
    }

    public void setAngle(double angle){
        this.currentAngle = angle;
    }

    public Rotation2d getAngle(){
        try {
            currentPose.setRotation2d(new Rotation2d(-Robot.getInstance().imu.getNormalHeading(), AngleUnit.DEGREES));
            return currentPose.getRotation2d();
        } catch (Exception e){
            return new Rotation2d(0, AngleUnit.DEGREES);
        }
    }

    /*
    //straight
    public void setPosition(double xPosition, double yPosition, int angle){
        xPositionTicks = xPosition * Constants.DriveConstants.InchToTick;
        yPositionTicks = yPosition * Constants.DriveConstants.InchToTick;
        wantedAngle = angle;

//        xEncoderOffset = Robot.getInstance().fLDrive.getPosition() + Robot.getInstance().fRDrive.getPosition() / 2;
        yEncoderOffset = Robot.getInstance().bLDrive.getPosition();

        setStraightPositionActive = true;
    }
    //curve
    public void setPosition(double radius, double yAxis, int angle, double curve){
        this.radius = radius;
        this.yDirection = yAxis;

        wantedAngle = angle;
        this.curve = curve / 100;

        if (radius > 0)
            xDirectionRight = true;
        else if (radius < 0)
            xDirectionRight = false;
        if (yDirection > 0)
            yDirectionFoward = true;
        else if (yDirection < 0)
            yDirectionFoward = false;

        circ = (2 * radius) * Math.PI;
        distance = circ / 4;
        unit = distance / 100;
        nextMilestone = unit;

        ySpeed = speed;


        xEncoderOffset = Robot.getInstance().fLDrive.getPosition() + Robot.getInstance().fRDrive.getPosition() / 2;
        yEncoderOffset = Robot.getInstance().bLDrive.getPosition();

        setCurvePositionActive = true;
    }
    */

    public void setFieldCentricPower(double x, double y, double rotation){
        double timerOffset = 0;
        if(Robot.timer.milliseconds() > 100 + timerOffset){
            timerOffset = Robot.timer.milliseconds();
            gyro = getAngle().getTheda(AngleUnit.RADIANS);
        }
        double xMod = x * Math.cos(/*getAngle().getTheda(AngleUnit.RADIANS)*/ gyro) - y * Math.sin(/*getAngle().getTheda(AngleUnit.RADIANS)*/ gyro);
        double yMod = x * Math.sin(/*getAngle().getTheda(AngleUnit.RADIANS)*/ gyro) + y * Math.cos(/*getAngle().getTheda(AngleUnit.RADIANS)*/ gyro);
        setPower(xMod, yMod, rotation);
    }

    /*public void setPower(double leftStickX, double leftStickY, double rightStickX){

        //double x, y, angle;

//        x = leftStickX * ((1 / 360) * getAngle().getTheda(AngleUnit.DEGREES));
//        y = leftStickY / ((1 / 360) * getAngle().getTheda(AngleUnit.DEGREES));

        //x = leftStickY * (Math.cos(getAngle().getTheda(AngleUnit.RADIANS) + 1.5708));
        //y = leftStickY * (Math.sin(getAngle().getTheda(AngleUnit.RADIANS) + 1.5708));

        //x = x + (leftStickY * (Math.cos(getAngle().getTheda(AngleUnit.RADIANS))));
        //y = y + (leftStickY * (Math.sin(getAngle().getTheda(AngleUnit.RADIANS))));

        double r = Math.hypot(x, y);
        //double robotAngle = Math.atan2(y, x) - Math.PI / 4;
        double robotAngle = Math.atan2(y, x) - getAngle().getTheda(AngleUnit.RADIANS);
        double rightX = rightStickX;
        final double v1 = r * Math.cos(robotAngle) + rightX;
        final double v2 = r * Math.sin(robotAngle) - rightX;
        final double v3 = r * Math.sin(robotAngle) + rightX;
        final double v4 = r * Math.cos(robotAngle) - rightX;

        Robot.getInstance().fLDrive.setPower(v1);
        Robot.getInstance().fRDrive.setPower(v2);
        Robot.getInstance().bLDrive.setPower(v3);
        Robot.getInstance().bRDrive.setPower(v4);
    }*/


    public void setAutoPower(double leftStickX, double leftStickY, double rightStickX, double controller, double negative){
        double r = Math.hypot(leftStickX, leftStickY);
        double robotAngle = Math.atan2(leftStickY, leftStickX) - Math.PI / 4;
        double rightX = rightStickX;
        final double v1 = Math.abs(r * Math.cos(robotAngle) + rightX) * controller * frontLeftInt;
        final double v2 = Math.abs(r * Math.sin(robotAngle) - rightX) * controller * frontRightInt;
        final double v3 = Math.abs(r * Math.sin(robotAngle) + rightX) * controller * backLeftInt;
        final double v4 = Math.abs(r * Math.cos(robotAngle) - rightX) * controller * backRightInt;

        Robot.getInstance().fLDrive.setPower(v1 * frontLeft / Math.abs(frontLeft));
        Robot.getInstance().fRDrive.setPower(v2 * frontRight / Math.abs(frontRight));
        Robot.getInstance().bLDrive.setPower(v3 * backLeft / Math.abs(backLeft));
        Robot.getInstance().bRDrive.setPower(v4 * backRight / Math.abs(backRight));
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

    public void setPosition(double xPosition, double yPosition, double rotation){
        if (Math.abs(xPosition) > Math.abs(yPosition)){
            xAutoPower = 1;
            yAutoPower = Math.abs(yPosition / xPosition);
        }
        else if (Math.abs(xPosition) < Math.abs(yPosition)){
            xAutoPower = Math.abs(xPosition / yPosition);
            yAutoPower = 1;
        }

        //setPower(.5, 0, 0);

        double r = Math.hypot(xPosition, yPosition);
        double robotAngle = Math.atan2(yPosition, xPosition) - Math.PI / 4;
        double rightX = rotation;
        frontLeft = (r * Math.cos(robotAngle) + rightX) * 1.414;
        frontRight = (r * Math.sin(robotAngle) - rightX) * 1.414;
        backLeft = (r * Math.sin(robotAngle) + rightX) * 1.414;
        backRight = (r * Math.cos(robotAngle) - rightX) * 1.414;
    }

    public static void main(String[] args) {
        double r = Math.hypot(1000, 0);
        double robotAngle = Math.atan2(0, 1000) - Math.PI / 4;
        double rightX = 0;
        System.out.println((r * Math.cos(robotAngle) + rightX) * 1.414);
        System.out.println(r * Math.sin(robotAngle) - rightX);
        System.out.println(r * Math.sin(robotAngle) + rightX);
        System.out.println(r * Math.cos(robotAngle) - rightX);
    }
}