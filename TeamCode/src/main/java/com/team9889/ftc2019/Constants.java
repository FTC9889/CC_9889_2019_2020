package com.team9889.ftc2019;

/**
 * Class to store constants
 * Created by joshua9889 on 4/10/2017.
 */

public class Constants {

    //VuMark Licence Key
    public final static String kVuforiaLicenceKey = "AUUHzRL/////AAABmaGWp2jobkGOsFjHcltHEso2M1NH" +
            "Ko/nBDlUfwXKZBwui9l88f5YAT31+8u5yoy3IEJ1uez7rZrIyLdyR4cCMC+a6I7X/EzkR1F094ZegAsXdg9n" +
            "ht01zju+nxxi1+rabsSepS+TZfAa14/0rRidvX6jqjlpncDf8uGiP75f38cW6W4uFRPrLdufA8jMqODGux9d" +
            "w7VkFMqB+DQuNk8n1pvJP87FFo99kr653cjMwO4TYbztNmUYaQUXjHHNhOFxHufN42r7YcCErvX90n/gIvs4" +
            "wrvffXGyU/xkmSaTJzrgiy8R+ZJx2T0JcEJ0m1UUEoF2BmW4ONAVv/TkG9ESGf6iAmx66vrVm3tk6+YY+1q1";

    public final static String kRevHubMaster = "4";
    public final static String kRevHubSlave = "1";

    /*---------------------
    |                     |
    |     Drivetrain!     |
    |                     |
    ---------------------*/

    //Settings for Drive class
    public static class DriveConstants {
        public final static String kLeftDriveMasterId = "lf";
        public final static String kRightDriveMasterId = "rf";
        public final static String kLeftDriveSlaveId = "lb";
        public final static String kRightDriveSlaveId = "rb";

        public final static double WheelbaseWidth = 14.5;
        public final static double WheelDiameter = 3.93701;

        /**
         * ticks to inch
         * (Wheel Diameter * PI) / Counts Per Rotation
         */
        public final static double ENCODER_TO_DISTANCE_RATIO = (WheelDiameter * Math.PI) / 537.6;
        public final static double AngleToInchRatio = (Math.PI / 180.) * (WheelbaseWidth / 2.);
        public final static double InchToTick = (Math.PI * WheelDiameter) * 537.6;
    }

    public static void main(String[] args) {
        double test = 1/DriveConstants.ENCODER_TO_DISTANCE_RATIO;
        double ratio = test/59.417845420974258687049938325739;
        System.out.println(0.00085 / ratio);
    }

   /*---------------------
    |                     |
    |       Intake        |
    |                     |
    ---------------------*/

    //Settings for Intake
    public static class IntakeConstants {
        public final static String kIntakeLeftMotorId = "li";
        public final static String kIntakeRightMotorId = "ri";

        public final static String kIntakeLeftServoId = "lis";
        public final static String kIntakeRightServoId = "ris";

        final static double kIntakeTicksPerRev = 537.6;  //NeveRest Orbital 20
        final static double kIntakeSpoolDiameter = 2;
        public final static double kIntakeTicksToInchRatio = kIntakeSpoolDiameter * Math.PI / kIntakeTicksPerRev;
    }

    /*---------------------
    |                     |
    |       Dumper!       |
    |                     |
    ---------------------*/

    //Settings for Dumper
    public static class DumperConstants {
        public final static String kDumperRotatorId = "dumperrotator";
    }

    /*---------------------
    |                     |
    |       Lift!         |
    |                     |
    ---------------------*/

    //Settings for Lift
    public static class LiftConstants {
        public final static String kLeftLift = "leftlift";
        public final static String kRightLift = "rightlift";
    }

    /*---------------------
    |                     |
    |        Camera!      |
    |                     |
    ---------------------*/

    //Settings for Camera
    public static class CameraConstants {
        public final static String kCameraXAxisId = "cameraxaxis";
        public final static String kCameraYAxisId = "camerayaxis";
    }
}
