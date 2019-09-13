package com.team9889.ftc2019.subsystems;

import com.team9889.ftc2019.Constants;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Created by Eric on 9/7/2019.
 */

public class MecanumDriveTest extends Subsystem {

    private double currentAngle, wantedAngle;
    private double xEncoderOffset, yEncoderOffset, yDirection;
    private double xPositionTicks, yPositionTicks, radius;

    public boolean setStraightPositionActive = false;
    public boolean setCurvePositionActive = false;
    private double x, y, xSpeed, ySpeed = 0;
    private double curve, circ, distance, unit, nextMilestone, travelTotal, travelDuringMilestone = 0;
    private double speed = .5;

    private boolean xDirectionRight;
    private boolean yDirectionFoward;


    @Override
    public void init(boolean auto) {

    }

    @Override
    public void outputToTelemetry(Telemetry telemetry) {

    }

    @Override
    public void update() {
        if (setStraightPositionActive){
            if ((Robot.getInstance().fLDrive.getPosition() + Robot.getInstance().fRDrive.getPosition() / 2) - xEncoderOffset < xPositionTicks) {
                xSpeed = .5;
            } else
                xSpeed = 0;
            if (Robot.getInstance().bLDrive.getPosition() - yEncoderOffset < yPositionTicks){
                ySpeed = .5;
            } else
                ySpeed = 0;

            setPower(xSpeed, ySpeed, wantedAngle - currentAngle);

            if (xSpeed == 0 && ySpeed == 0 && wantedAngle == currentAngle){
                setStraightPositionActive = false;
            }
        }else if (setCurvePositionActive){
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

            if (travelTotal < (distance * curve)) {
                setCurvePositionActive = false;
            }
        }
    }

    public void setAngle(double angle){
        this.currentAngle = angle;
    }

    //straight
    public void setPosition(double xPosition, double yPosition, int angle){
        xPositionTicks = xPosition * Constants.DriveConstants.InchToTick;
        yPositionTicks = yPosition * Constants.DriveConstants.InchToTick;
        wantedAngle = angle;

        xEncoderOffset = Robot.getInstance().fLDrive.getPosition() + Robot.getInstance().fRDrive.getPosition() / 2;
        yEncoderOffset = Robot.getInstance().bLDrive.getPosition();

        setStraightPositionActive = true;
    }
    //curve
    public void setPosition(double radius, double yAxis, int angle, double curve){
        this.radius = radius;
        this.yDirection = yAxis;

        wantedAngle = angle;
        this.curve = curve / 100;

        if (xPositionTicks > 0)
            xDirectionRight = true;
        else if (xPositionTicks < 0)
            xDirectionRight = false;
        if (yPositionTicks > 0)
            yDirectionFoward = true;
        else if (yPositionTicks < 0)
            yDirectionFoward = false;

        circ = (2 * radius) * Math.PI;
        distance = circ / 4;
        unit = distance / 100;
        nextMilestone = unit;


        xEncoderOffset = Robot.getInstance().fLDrive.getPosition() + Robot.getInstance().fRDrive.getPosition() / 2;
        yEncoderOffset = Robot.getInstance().bLDrive.getPosition();

        setCurvePositionActive = true;
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
}
