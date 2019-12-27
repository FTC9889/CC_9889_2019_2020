package com.team9889.lib.control.kinematics;

import com.team9889.lib.control.math.cartesian.Pose;
import com.team9889.lib.control.math.cartesian.Rotation2d;
import com.team9889.lib.control.math.cartesian.Vector2d;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

import java.util.Arrays;

public class TankDriveKinematicModel {
    //input values
    public static double w = 16;

    //output values
    public static double globalX = 0, globalY = 0, globalTheda = 0;
    public static Pose globalPose = new Pose();

    //function to calculate the arc of the drivetrain
    public Pose calculateDelta(double deltaLeftPos,double deltaRightPos, double pastAbsTheda){
        //calculate average distance of left and right motors
        double a = (deltaRightPos + deltaLeftPos) / 2;

        //calculate the change in angle
        double deltaTheda = (deltaLeftPos - deltaRightPos) / w;

        double deltaX, deltaY;

        //checks if we're going somewhat straight
        if (Math.abs(deltaTheda) > 0.001) {
            //finding the x intercept
            double l = a / deltaTheda;

            //calculating position based on the intercept
            deltaY = l * Math.sin(deltaTheda);
            deltaX = l * (1 - Math.cos(deltaTheda));
        }else{
            //calculating position in a straight line
            deltaX = a * Math.sin(pastAbsTheda);
            deltaY = a * Math.cos(pastAbsTheda);
        }

        //returns change in position and change in angle in both radians and degrees
        return new Pose(new Vector2d(deltaX, deltaY), new Rotation2d(-deltaTheda, AngleUnit.RADIANS));
    }

    //calculate global position and angle
    public Pose calculateAbs(double deltaLeftPos, double deltaRightPos) {
        //import data from calculateDelta into local variable
        Pose calculatedDelta = calculateDelta(deltaLeftPos, deltaRightPos, globalTheda);

        //adding all delta positions and angle to global positions and angle (pose)
        globalPose = Pose.add(globalPose, calculatedDelta);
        return globalPose;
    }

    //test
    public static void main(String... args ){
        TankDriveKinematicModel kinematicModel = new TankDriveKinematicModel();
//        System.out.println(Arrays.toString(kinematicModel.calculateDelta(20, 30)));
//        System.out.println(Arrays.toString(kinematicModel.calculateDelta(20, 20)));
//        System.out.println(Arrays.toString(kinematicModel.calculateDelta(30, 20)));

        System.out.println(kinematicModel.calculateAbs(20, 20));
        System.out.println(kinematicModel.calculateAbs(30, 20));
        System.out.println(kinematicModel.calculateAbs(0, 30));

    }

}
