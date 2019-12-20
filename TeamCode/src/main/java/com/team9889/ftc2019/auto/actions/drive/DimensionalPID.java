package com.team9889.ftc2019.auto.actions.drive;

import android.os.Debug;
import android.util.Log;

import com.qualcomm.robotcore.util.ElapsedTime;
import com.team9889.ftc2019.Constants;
import com.team9889.ftc2019.auto.actions.Action;
import com.team9889.ftc2019.subsystems.MecanumDrive;
import com.team9889.ftc2019.subsystems.Robot;
import com.team9889.lib.CruiseLib;
import com.team9889.lib.control.controllers.MotionProfileFollower;
import com.team9889.lib.control.controllers.PID;
import com.team9889.lib.control.motion.ProfileParameters;
import com.team9889.lib.control.motion.TrapezoidalMotionProfile;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.opencv.core.Mat;

/**
 * Created by Eric on 12/13/2019.
 */

public class DimensionalPID extends Action {

    TrapezoidalMotionProfile profile;
    MotionProfileFollower follower = new MotionProfileFollower(0.002, 0, 0.0225, 0.005);

    PID turnPID = new PID(0.03, 0, 0.3);

    boolean offsetAxis = false;

    MecanumDrive mDrive = Robot.getInstance().getMecanumDrive();
    private String distance;
    private String [] positionsInMovement;
    private String[] positionsInMovementTemp;
    private String[] eachMovement;
    private double fullDistance;
    private int movement = 0;

    private double xOffset, yOffset;

    private double angle;
    private double[] offsets = new double[]{
            0, 0, 0, 0
    };
    private ElapsedTime timer = new ElapsedTime();

    private double[] currentPosition = new double[] {
            1000, 1000, 1000, 1000
    };

    public DimensionalPID(String distance) {
        this.distance = distance;
    }

    @Override
    public void setup(String args) {}

    @Override
    public void start() {
        offsets[0] = mDrive.backRight;
        offsets[1] = mDrive.backLeft;
        offsets[2] = mDrive.frontRight;
        offsets[3] = mDrive.frontLeft;
        Log.i("hi", "test");

        eachMovement = distance.split(";");
        Log.i("hi", "test2");

        for(int i = 0; i < eachMovement.length; i++){
            fullDistance += Math.abs(Double.parseDouble(eachMovement[i].split(",")[0])) + Math.abs(Double.parseDouble(eachMovement[i].split(",")[1]));
        }

        profile = new TrapezoidalMotionProfile(fullDistance,
                new ProfileParameters(
                        ((2 * Math.PI * ((5475.764) / 20)) / 60.0) * 0.8,
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
        currentPosition[0] = mDrive.backRight - offsets[0];
        currentPosition[1] = mDrive.backLeft - offsets[1];
        currentPosition[2] = mDrive.frontRight - offsets[2];
        currentPosition[3] = mDrive.frontLeft - offsets[3];

        angle = Double.parseDouble(eachMovement[movement].split(",")[2]);

        double averageDistance = 0;
        for (int i = 0; i < currentPosition.length; i++) {
            averageDistance += Math.abs(currentPosition[i]);
        }

        averageDistance = averageDistance * Constants.DriveConstants.ENCODER_TO_DISTANCE_RATIO / 4.0;

        double speed = follower.update(averageDistance, timer.seconds());

        double currentAngle = mDrive.getAngle().getTheda(AngleUnit.DEGREES);

        if (offsetAxis) currentAngle = Math.signum(currentAngle) > 0 ? currentAngle - 180 : currentAngle + 180;


        Log.d("-------------- Angles: ", currentAngle + ", " + angle);
        double rotation = turnPID.update(currentAngle, angle);

        if (CruiseLib.isBetween(Robot.getInstance().fLDrive.getPosition(),
                Robot.getInstance().getMecanumDrive().frontLeft - 5, Robot.getInstance().getMecanumDrive().frontLeft + 5)
                && CruiseLib.isBetween(Robot.getInstance().fRDrive.getPosition(),
                Robot.getInstance().getMecanumDrive().frontRight - 5, Robot.getInstance().getMecanumDrive().frontRight + 5)
                && CruiseLib.isBetween(Robot.getInstance().bLDrive.getPosition(),
                Robot.getInstance().getMecanumDrive().backLeft - 5, Robot.getInstance().getMecanumDrive().backLeft + 5)
                && CruiseLib.isBetween(Robot.getInstance().bRDrive.getPosition(),
                Robot.getInstance().getMecanumDrive().backRight - 5, Robot.getInstance().getMecanumDrive().backRight + 5)){

            //Add offset here

            movement++;
        }

        mDrive.setPower(Double.parseDouble(eachMovement[movement].split(",")[0]) * speed, Double.parseDouble(eachMovement[movement].split(",")[1]) * speed, rotation);
    }

    int angleCounter = 0;
    @Override
    public boolean isFinished() {
        if (Math.abs(turnPID.getError()) < 3) angleCounter++;

        return follower.isFinished() && angleCounter > 3 && movement > eachMovement.length;
    }

    @Override
    public void done() {
        mDrive.setPower(0,0,0);
    }
}