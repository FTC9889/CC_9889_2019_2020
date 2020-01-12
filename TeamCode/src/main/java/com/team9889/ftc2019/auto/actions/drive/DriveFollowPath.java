package com.team9889.ftc2019.auto.actions.drive;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.RobotLog;
import com.team9889.ftc2019.auto.actions.Action;
import com.team9889.ftc2019.subsystems.MecanumDrive;
import com.team9889.ftc2019.subsystems.Robot;
import com.team9889.lib.CruiseLib;
import com.team9889.lib.control.controllers.PID;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

/**
 * Created by Eric on 1/10/2020.
 */
public class DriveFollowPath extends Action {
    Pose2d[] poses;
    double[] r;
    Pose2d[] tolerancePoses;
    float[] maxVels;
    int pose = 0;
    int rNum = 0;
    int tNum = 0;
    int vNum = 0;

    double startOfNextLineX, endOfNextLineX;
    double startOfNextLineY, endOfNextLineY;


    // Controllers
    private PID xPID = new PID(-0.1, 0, 0);
    private PID yPID = new PID(-0.1, 0, 0);
    private PID turnPID = new PID(0.03, 0, 0.1);

    // Max Speed
    double maxVel = 0.5;

    // Wanted Pose of the Robot
    private Pose2d wantedPose;
    private Pose2d tolerancePose;

    // End Conditions
    private int timeOut = 30000; //  Milliseconds
    private ElapsedTime timer = new ElapsedTime();
    private int angleCounter = 0;
    private int xCounter = 0;
    private int yCounter = 0;

    // Drivetrain object
    private MecanumDrive mDrive = Robot.getInstance().getMecanumDrive();

    public DriveFollowPath(Pose2d[] poses){
        this.poses = poses;
        this.r = new double[]{8.5};
        this.tolerancePoses = new Pose2d[]{new Pose2d(2,2,Math.toRadians(3))};
        this.maxVels = new float[]{.5f};
    }

    public DriveFollowPath(Pose2d[] poses, double[] r){
        this.poses = poses;
        this.r = r;
        this.tolerancePoses = new Pose2d[]{new Pose2d(2,2,Math.toRadians(3))};
        this.maxVels = new float[]{.5f};
    }

    public DriveFollowPath(Pose2d[] poses, double[] r, Pose2d[] tolerancePose){
        this.poses = poses;
        this.r = r;
        this.tolerancePoses = tolerancePose;
        this.maxVels = new float[]{.5f};
    }

    public DriveFollowPath(Pose2d[] poses, double[] r, Pose2d[] tolerancePose, float[] maxVel){
        this.poses = poses;
        this.r = r;
        this.tolerancePoses = tolerancePose;
        this.maxVels = maxVel;
    }

    public DriveFollowPath(Pose2d[] poses, Pose2d[] tolerancePose){
        this.poses = poses;
        this.r = new double[]{8.5};
        this.tolerancePoses = tolerancePose;
        this.maxVels = new float[]{.5f};
    }

    public DriveFollowPath(Pose2d[] poses, float[] maxVel){
        this.poses = poses;
        this.r = new double[]{8.5};
        this.tolerancePoses = new Pose2d[]{new Pose2d(2,2,Math.toRadians(3))};
        this.maxVels = maxVel;
    }

    public DriveFollowPath(Pose2d[] poses, Pose2d[] tolerancePose, float[] maxVel){
        this.poses = poses;
        this.r = new double[]{8.5};
        this.tolerancePoses = tolerancePose;
        this.maxVels = maxVel;
    }

    public DriveFollowPath(Pose2d[] poses, double[] r, float[] maxVel){
        this.poses = poses;
        this.r = r;
        this.tolerancePoses = new Pose2d[]{new Pose2d(2,2,Math.toRadians(3))};
        this.maxVels = maxVel;
    }

    @Override
    public void setup(String args) {

    }

    @Override
    public void start() {
        startOfNextLineX = poses[0].getX();
        startOfNextLineY = poses[0].getY();

        endOfNextLineX = poses[1].getX();
        endOfNextLineY = poses[1].getY();

        wantedPose = poses[pose];
        tolerancePose = tolerancePoses[tNum];
        maxVel = maxVels[vNum];
    }

    @Override
    public void update() {
//      x and y = robot position
        double x = Robot.getInstance().getMecanumDrive().getCurrentPose().getX();
        double y = Robot.getInstance().getMecanumDrive().getCurrentPose().getY();

        tolerancePose = tolerancePoses[tNum];
        maxVel = maxVels[vNum];

        if(rNum > r.length-1){
            rNum = r.length-1;
        }


        if (Math.sqrt(Math.pow(poses[pose].getX() - x, 2) + Math.pow(poses[pose].getY() - y, 2)) <= r[rNum] &&
//                Math.abs(pDistance(x, y, endOfNextLineX, endOfNextLineY, startOfNextLineX, startOfNextLineY)) <= r[rNum] &&
                pose < poses.length-1){
            pose++;
            rNum++;
            tNum++;
            vNum++;

            startOfNextLineX = poses[pose].getX();
            startOfNextLineY = poses[pose].getY();

            endOfNextLineX = poses[pose].getX();
            endOfNextLineY = poses[pose].getY();

            wantedPose = poses[pose];
        }

        RobotLog.a("Distance From wanted pose: " + String.valueOf(Math.sqrt(Math.pow(poses[pose].getX() - x, 2) + Math.pow(poses[pose].getY() - y, 2))));
        RobotLog.a("Distance From line: " + String.valueOf(pDistance(x, y, endOfNextLineX, endOfNextLineY, startOfNextLineX, startOfNextLineY)));

        double x_power = xPID.update(wantedPose.getX(), x);
        double y_power = yPID.update(wantedPose.getY(), y);

        double currentAngle = mDrive.gyroAngle.getTheda(AngleUnit.RADIANS);
        double dx = Math.cos(wantedPose.getHeading() - currentAngle);
        double dy = Math.sin(wantedPose.getHeading() - currentAngle);
        double turn = Math.toDegrees(Math.atan2(dy, dx));
        turn *= -1;

        double rotation = turnPID.update(turn, 0);

        x_power = CruiseLib.limitValue(x_power, maxVel);
        y_power = CruiseLib.limitValue(y_power, maxVel);
        rotation = CruiseLib.limitValue(rotation, maxVel);

        mDrive.setFieldCentricAutoPower(y_power, x_power, rotation);


    }

    @Override
    public boolean isFinished() {
        if (Math.abs(xPID.getError()) < Math.abs(tolerancePose.getX())) xCounter++; else xCounter = 0;
        if (Math.abs(yPID.getError()) < Math.abs(tolerancePose.getY())) yCounter++; else xCounter = 0;

        if (Math.abs(turnPID.getError()) < Math.abs(Math.toDegrees(tolerancePose.getHeading())))
            angleCounter++;
        else angleCounter = 0;

        return (xCounter > 3 && yCounter > 3 && angleCounter > 3 && pose == poses.length-1) || timeOut < timer.milliseconds();
    }

    @Override
    public void done() {
        mDrive.setPower(0,0 ,0);
    }

    public static void main(String[] args){
        DriveFollowPath path = new DriveFollowPath(new Pose2d[]{new Pose2d(5, 0, 0), new Pose2d(10, 0, 0)}, new double[]{5, 5});

        System.out.println(path.pDistance(0, 0, -5, 0, 5, 0));
        System.out.println(path.pDistance(3, 1, 5, 0, 10, 0));


    }

    public double pDistance(double x, double y, double x1, double y1, double x2, double y2) {
        double A = x - x1; // position of point rel one end of line
        double B = y - y1;
        double C = x2 - x1; // vector along line
        double D = y2 - y1;
        double E = -D; // orthogonal vector
        double F = C;

        double dot = A * E + B * F;
        double len_sq = E * E + F * F;

        return Math.abs(dot) / Math.sqrt(len_sq);
    }
}
