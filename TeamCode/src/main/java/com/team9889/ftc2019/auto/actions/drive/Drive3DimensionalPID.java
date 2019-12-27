package com.team9889.ftc2019.auto.actions.drive;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.team9889.ftc2019.auto.actions.Action;
import com.team9889.ftc2019.subsystems.MecanumDrive;
import com.team9889.ftc2019.subsystems.Robot;
import com.team9889.lib.CruiseLib;
import com.team9889.lib.control.controllers.PID;
import com.team9889.lib.control.math.cartesian.Pose;
import com.team9889.lib.control.math.cartesian.Rotation2d;
import com.team9889.lib.control.math.cartesian.Vector2d;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

/**
 * Created by joshua9889 on 12/24/2019.
 */
public class Drive3DimensionalPID extends Action {

    /**
     * @param wantedPose Wanted Pose of the robot at the end of the move.
     */
    public Drive3DimensionalPID (Pose2d wantedPose) {
        this.wantedPose = wantedPose;
        this.tolerancePose = new Pose2d(2,2,2);
    }

    /**
     * @param wantedPose Wanted Pose of the robot at the end of the move.
     * @param tolerancePose
     */
    public Drive3DimensionalPID (Pose2d wantedPose, Pose2d tolerancePose) {
        this.wantedPose = wantedPose;
        this.tolerancePose = tolerancePose;
    }

    // Controllers
    private PID xPID = new PID(-0.12, -0.000001, 0.08, 0.025);
    private PID yPID = new PID(-0.12, -0.000001, 0.08, 0.025);
    private PID turnPID = new PID(0.02, 0, 0.3, 0);

    // Max Speed
    double maxVel = 0.5;

    // Wanted Pose of the Robot
    private Pose2d wantedPose = new Pose2d(0, 0, 0);
    private Pose2d tolerancePose = new Pose2d(3, 3, 3);

    // Angle adjustment
    private double angle = 0;
    private boolean offsetAxis = false;

    // End Conditions
    private int timeOut = 30000; //  Milliseconds
    private ElapsedTime timer = new ElapsedTime();
    private int angleCounter = 0;
    private int xCounter = 0;
    private int yCounter = 0;

    // Drivetrain object
    private MecanumDrive mDrive = Robot.getInstance().getMecanumDrive();

    @Override
    public void setup(String args) {

    }

    @Override
    public void start() {
        angle = Math.toDegrees(wantedPose.getHeading());

        if (Math.abs(angle) > 175) {
            angle = Math.signum(angle) > 0 ? -(angle - 180) : -(angle + 180);
            offsetAxis = true;
        }

        timer.reset();
    }

    @Override
    public void update() {
        double x = xPID.update(wantedPose.getX(), mDrive.getCurrentPose().getX());
        double y = yPID.update(wantedPose.getY(), mDrive.getCurrentPose().getY());

        double currentAngle = mDrive.gyroAngle.getTheda(AngleUnit.DEGREES);
        if (offsetAxis) currentAngle = Math.signum(currentAngle) > 0 ? currentAngle - 180 : currentAngle + 180;
        double rotation = turnPID.update(currentAngle, angle);

        x = CruiseLib.limitValue(x, maxVel);
        y = CruiseLib.limitValue(y, maxVel);
        rotation = CruiseLib.limitValue(rotation, 0.75);

        mDrive.setFieldCentricAutoPower(y, x, rotation);
    }

    @Override
    public boolean isFinished() {
        if (Math.abs(xPID.getError()) < Math.abs(tolerancePose.getX())) xCounter++; else xCounter = 0;
        if (Math.abs(yPID.getError()) < Math.abs(tolerancePose.getX())) yCounter++; else yCounter = 0;
        if (Math.abs(turnPID.getError()) < Math.abs(Math.toDegrees(tolerancePose.getHeading()))) angleCounter++; else angleCounter = 0;

        return (xCounter > 3 && yCounter > 3 && angleCounter > 3) || timeOut < timer.milliseconds();
    }

    @Override
    public void done() {
        mDrive.setPower(0,0 ,0);
    }
}
