package com.team9889.ftc2019.auto.actions.drive;

import android.os.health.PidHealthStats;
import android.sax.StartElementListener;
import android.util.Log;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.team9889.ftc2019.auto.actions.Action;
import com.team9889.ftc2019.subsystems.Robot;
import com.team9889.lib.CruiseLib;
import com.team9889.lib.Path;
import com.team9889.lib.PurePursuit;
import com.team9889.lib.control.controllers.PID;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.direct.CodeObserver;
import org.opencv.core.Point;

import java.util.ArrayList;

/**
 * Created by Eric on 8/26/2020.
 */
public class DrivePurePursuit extends Action {
    ArrayList<Path> paths;

    int lineNum = 0;

    PurePursuit pp = new PurePursuit();
    private PID xPID = new PID(-0.2, 0, -0.2);
    private PID yPID = new PID(-0.2, 0, -0.3);
    private PID orientationPID = new PID(0, 0, 0.008);

    private int angleCounter = 0;
    private int xCounter = 0;
    private int yCounter = 0;

    Telemetry telemetry;

    public DrivePurePursuit (ArrayList<Path> paths, Telemetry telemetry) {
        this.paths = paths;
        this.telemetry = telemetry;
    }

    @Override
    public void setup(String args) {

    }

    @Override
    public void start() {

    }

    @Override
    public void update() {
        Robot.getInstance().update();

        paths.add(0, new Path(Robot.getInstance().getMecanumDrive().odometry.returnPose(),
                paths.get(0).getTolerancePose(), paths.get(0).getRadius(), paths.get(0).getMaxVelocity()));

        Object[] ppObject = pp.bestPointToFollow(paths.get(lineNum).getPoint(),
                paths.get(lineNum + 1).getPoint(), paths.get(lineNum + 2).getPoint(),
                paths.get(lineNum + 1).getRadius(), paths.get(lineNum + 2).getRadius());

        Point point = (Point) ppObject[0];

        if ((boolean) ppObject[1]) {
            lineNum++;
        }

        Log.i("Point X : ", "" + point.x);

        double xPower = xPID.update(Robot.getInstance().getMecanumDrive().odometry.returnXCoordinate(),
                point.x);
        double yPower = yPID.update(Robot.getInstance().getMecanumDrive().odometry.returnYCoordinate(),
                point.y);

        double wantedAngle;
        if (paths.get(lineNum + 1).getPose().getHeading() > 180){
            wantedAngle = paths.get(lineNum + 1).getPose().getHeading() - 360;
        }else if (paths.get(lineNum + 1).getPose().getHeading() < -180){
            wantedAngle = paths.get(lineNum + 1).getPose().getHeading() + 360;
        }else
            wantedAngle = paths.get(lineNum + 1).getPose().getHeading();

        double turn = wantedAngle - Robot.getInstance().getMecanumDrive().odometry.returnOrientation();

        if (turn > 180){
            turn = turn - 360;
        }else if (turn < -180){
            turn = turn + 360;
        }
        turn *= -1;

        double rotation = orientationPID.update(turn, 0);

        double maxPower = paths.get(lineNum + 1).getMaxVelocity();
        xPower = CruiseLib.limitValue(xPower, -.2, -maxPower, .2, maxPower);
        yPower = CruiseLib.limitValue(yPower, -.2, -maxPower, .2, maxPower);
        rotation = CruiseLib.limitValue(rotation, -.2, -paths.get(lineNum + 1).getMaxTurnVelocity(),
                .2, paths.get(lineNum + 1).getMaxTurnVelocity());

        Robot.getInstance().getMecanumDrive().setFieldCentricAutoPower(xPower, yPower, rotation);
    }

    @Override
    public boolean isFinished() {
        Pose2d tolerancePose = paths.get(paths.size() - 1).getTolerancePose();

        if (Math.abs(xPID.getError()) < Math.abs(tolerancePose.getX())) xCounter++; else xCounter = 0;
        if (Math.abs(yPID.getError()) < Math.abs(tolerancePose.getY())) yCounter++; else yCounter = 0;

        if (Math.abs(orientationPID.getError()) < Math.abs(tolerancePose.getHeading()))
            angleCounter++;
        else angleCounter = 0;

        Log.i("X Counter", "" + xCounter);
        Log.i("Y Counter", "" + yCounter);
        Log.i("H Counter", "" + angleCounter);

        return (xCounter > 3 && yCounter > 3 && angleCounter > 3 && lineNum == paths.size()-1);
    }

    @Override
    public void done() {
        Robot.getInstance().getMecanumDrive().setPower(0,0 ,0);
    }
}
