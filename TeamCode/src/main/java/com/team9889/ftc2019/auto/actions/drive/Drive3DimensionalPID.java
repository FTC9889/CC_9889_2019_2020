package com.team9889.ftc2019.auto.actions.drive;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.team9889.ftc2019.auto.actions.Action;
import com.team9889.ftc2019.subsystems.MecanumDrive;
import com.team9889.ftc2019.subsystems.Robot;
import com.team9889.lib.CruiseLib;
import com.team9889.lib.android.FileReader;
import com.team9889.lib.control.controllers.PID;

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
        this.tolerancePose = new Pose2d(2,2,Math.toRadians(3));
    }

    /**
     * @param wantedPose Wanted Pose of the robot at the end of the move.
     * @param tolerancePose
     */
    public Drive3DimensionalPID (Pose2d wantedPose, Pose2d tolerancePose) {
        this.wantedPose = wantedPose;
        this.tolerancePose = tolerancePose;
    }

    public Drive3DimensionalPID (Pose2d wantedPose, Pose2d tolerancePose, double maxVel) {
        this.wantedPose = wantedPose;
        this.tolerancePose = tolerancePose;
        this.maxVel = maxVel;
    }

    public Drive3DimensionalPID (Pose2d wantedPose, double maxVel) {
        this.wantedPose = wantedPose;
        this.tolerancePose = new Pose2d(2,2,Math.toRadians(3));
        this.maxVel = maxVel;
    }

    // Controllers
    private PID xPID = new PID(-0.1, 0, 0);
    private PID yPID = new PID(-0.1, 0, 0);
    private PID turnPID = new PID(0.03, 0, 0.1);
    private boolean debugging = false;

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

    @Override
    public void setup(String args) {

    }

    @Override
    public void start() {
        if(debugging) loadPidConstantsFromFile();
        timer.reset();
    }

    @Override
    public void update() {
        double x = xPID.update(wantedPose.getX(), mDrive.getCurrentPose().getX());
        double y = yPID.update(wantedPose.getY(), mDrive.getCurrentPose().getY());

        double currentAngle = mDrive.gyroAngle.getTheda(AngleUnit.RADIANS);
        double dx = Math.cos(wantedPose.getHeading() - currentAngle);
        double dy = Math.sin(wantedPose.getHeading() - currentAngle);
        double turn = Math.toDegrees(Math.atan2(dy, dx));
        turn *= -1;

        double rotation = turnPID.update(turn, 0);

        x = CruiseLib.limitValue(x, maxVel);
        y = CruiseLib.limitValue(y, maxVel);
        rotation = CruiseLib.limitValue(rotation, maxVel);

        mDrive.setFieldCentricAutoPower(y, x, rotation);
    }

    @Override
    public boolean isFinished() {
        if (Math.abs(xPID.getError()) < Math.abs(tolerancePose.getX())) xCounter++; else xCounter = 0;
        if (Math.abs(yPID.getError()) < Math.abs(tolerancePose.getY())) yCounter++; else xCounter = 0;

        if (Math.abs(turnPID.getError()) < Math.abs(Math.toDegrees(tolerancePose.getHeading())))
            angleCounter++;
        else angleCounter = 0;

        return (xCounter > 3 && yCounter > 3 && angleCounter > 3) || timeOut < timer.milliseconds();
    }

    @Override
    public void done() {
        mDrive.setPower(0,0 ,0);
    }

    public void loadPidConstantsFromFile() {
        FileReader pidFile = new FileReader("drive3dPID.csv");

        String[] lines = pidFile.lines();
        double[][] pid_constants = new double[3][3];

        for (int i = 0; i < lines.length; i++) {
            // Remove unneeded characters
            lines[i] = lines[i].replace(" ", ""); // Remove spaces
            lines[i] = lines[i].replace("\t", ""); // Remove tabs
            lines[i] = lines[i].replace("\n", ""); // Remove newlines

            String[] parameters = lines[i].split(",");

            for (int j = 0; j < parameters.length; j++) {
                pid_constants[i][j] = Double.parseDouble(parameters[j]);
            }
        }

        System.out.println(pid_constants[0][0] + ", " + pid_constants[0][1] + ", " + pid_constants[0][2]);
        System.out.println(pid_constants[1][0] + ", " + pid_constants[1][1] + ", " + pid_constants[1][2]);
        System.out.println(pid_constants[2][0] + ", " + pid_constants[2][1] + ", " + pid_constants[2][2]);

        xPID = new PID(pid_constants[0][0] ,  pid_constants[0][1] ,pid_constants[0][2]);
        yPID = new PID(pid_constants[1][0] ,  pid_constants[1][1] ,pid_constants[1][2]);
        turnPID = new PID(pid_constants[2][0] ,  pid_constants[2][1] ,pid_constants[2][2]);

        pidFile.close();
    }
}
