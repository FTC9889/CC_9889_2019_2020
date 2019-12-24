package com.team9889.ftc2019.auto.actions.drive;

import android.util.Log;

import com.qualcomm.robotcore.util.ElapsedTime;
import com.team9889.ftc2019.Constants;
import com.team9889.ftc2019.auto.actions.Action;
import com.team9889.ftc2019.subsystems.MecanumDrive;
import com.team9889.ftc2019.subsystems.Robot;
import com.team9889.lib.control.controllers.MotionProfileFollower;
import com.team9889.lib.control.controllers.PID;
import com.team9889.lib.control.motion.ProfileParameters;
import com.team9889.lib.control.motion.TrapezoidalMotionProfile;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

/**
 * Created by Eric on 11/26/2019.
 */
public class MecanumDriveSimpleAction extends Action {

    TrapezoidalMotionProfile profile;
    MotionProfileFollower follower = new MotionProfileFollower(0.0, 0, 0.0185, 0.004);
    PID turnPID = new PID(0.03, 0, 0.3);

    boolean offsetAxis = false;

    MecanumDrive mDrive = Robot.getInstance().getMecanumDrive();
    private double distance, angle, angleSpeed;
//    private double speedMultiply = 1.75;
    private int timeOut = 30000;
    private double[] offsets = new double[]{
            0, 0, 0, 0
    };
    private ElapsedTime timer = new ElapsedTime();

    private double[] currentPosition = new double[] {
            1000, 1000, 1000, 1000
    };

    public MecanumDriveSimpleAction(double distance, double angle) {
        this.distance = distance;
        this.angle = angle;
        this.angleSpeed = 1;
    }
    public MecanumDriveSimpleAction(double distance, double angle, int timeOut) {
        this.distance = distance;
        this.angle = angle;
        this.angleSpeed = 1;
        this.timeOut = timeOut;
    }
    public MecanumDriveSimpleAction(double distance, double angle, int timeOut, double speedMultiply, double angleSpeed) {
        this.distance = distance;
        this.angle = angle;
        this.angleSpeed = 1;
        this.timeOut = timeOut;
//        this.speedMultiply = speedMultiply;
    }

    @Override
    public void setup(String args) {}

    @Override
    public void start() {
        offsets[0] = Robot.getInstance().bRDrive.getPosition();
        offsets[1] = Robot.getInstance().bLDrive.getPosition();
        offsets[2] = Robot.getInstance().fRDrive.getPosition();
        offsets[3] = Robot.getInstance().fLDrive.getPosition();

        profile = new TrapezoidalMotionProfile(distance,
                new ProfileParameters(
                        ((2 * Math.PI * ((5475.764) / 19.2)) / 60.0) * 0.9,
                        50));

        follower.setProfile(profile);

        if (Math.abs(angle) > 175) {
            angle = Math.signum(angle) > 0 ? -(angle - 180) : -(angle + 180);
            offsetAxis = true;
        }

        timer.reset();
    }

    @Override
    public void update() {
        currentPosition[0] = Robot.getInstance().bRDrive.getPosition() - offsets[0];
        currentPosition[1] = Robot.getInstance().bLDrive.getPosition() - offsets[1];
        currentPosition[2] = Robot.getInstance().fRDrive.getPosition() - offsets[2];
        currentPosition[3] = Robot.getInstance().fLDrive.getPosition() - offsets[3];

        double averageDistance = 0;
        for (int i = 0; i < currentPosition.length; i++) {
            averageDistance += currentPosition[i];
        }

        averageDistance = averageDistance * Constants.DriveConstants.ENCODER_TO_DISTANCE_RATIO / 4.0;

        double speed = follower.update(averageDistance, timer.seconds());

        double currentAngle = mDrive.getAngle().getTheda(AngleUnit.DEGREES);

        if (offsetAxis) currentAngle = Math.signum(currentAngle) > 0 ? currentAngle - 180 : currentAngle + 180;


//        Log.e("-------------- Angles: ", currentAngle + ", " + angle);
        double rotation = turnPID.update(currentAngle, angle);

        mDrive.setPower(0, speed, rotation);
    }

    int angleCounter = 0;
    @Override
    public boolean isFinished() {
        if (Math.abs(turnPID.getError()) < 3) angleCounter++;

        return follower.isFinished() && angleCounter > 3 || timeOut < timer.milliseconds();
    }

    @Override
    public void done() {
        mDrive.setPower(0,0,0);
    }
}
