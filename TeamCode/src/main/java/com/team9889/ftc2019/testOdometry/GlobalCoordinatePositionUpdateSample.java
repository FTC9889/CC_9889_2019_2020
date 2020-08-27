package com.team9889.ftc2019.testOdometry;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.team9889.ftc2019.Constants;
import com.team9889.ftc2019.subsystems.Robot;
import com.team9889.lib.odometry.Odometry;

/**
 * Created by Sarthak on 6/1/2019.
 * Example OpMode that runs the GlobalCoordinatePosition thread and accesses the (x, y, theta) coordinate values
 */
@TeleOp(name = "Global Coordinate Position Test", group = "Calibration")
public class GlobalCoordinatePositionUpdateSample extends LinearOpMode {

    //Odometry encoder wheels
    DcMotor verticalRight, verticalLeft, horizontal;

    //Hardware map names for the encoder wheels. Again, these will change for each robot and need to be updated below
    String verticalLeftEncoderName = "leftlift", verticalRightEncoderName = "li", horizontalEncoderName = "ri";

    @Override
    public void runOpMode() throws InterruptedException {

        //Assign the hardware map to the odometry wheels
        verticalLeft = hardwareMap.dcMotor.get(verticalLeftEncoderName);
        verticalRight = hardwareMap.dcMotor.get(verticalRightEncoderName);
        horizontal = hardwareMap.dcMotor.get(horizontalEncoderName);

        //Reset the encoders
        verticalRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        verticalLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        horizontal.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        /*
        Reverse the direction of the odometry wheels. THIS WILL CHANGE FOR EACH ROBOT. Adjust the direction (as needed) of each encoder wheel
        such that when the verticalLeft and verticalRight encoders spin forward, they return positive values, and when the
        horizontal encoder travels to the right, it returns positive value
        */
        verticalLeft.setDirection(DcMotorSimple.Direction.REVERSE);
//        verticalRight.setDirection(DcMotorSimple.Direction.REVERSE);
//        horizontal.setDirection(DcMotorSimple.Direction.REVERSE);

        //Set the mode of the odometry encoders to RUN_WITHOUT_ENCODER
        verticalRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        verticalLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        horizontal.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        //Init complete
        telemetry.addData("Status", "Init Complete");
        telemetry.update();
        waitForStart();

        /**
         * *****************
         * OpMode Begins Here
         * *****************
         */

        //Create and start GlobalCoordinatePosition thread to constantly update the global coordinate positions\
//        Odometry odometry = new Odometry(verticalLeft, verticalRight, horizontal, Constants.OdometryConstants.ENCODER_TO_DISTANCE_RATIO);

        while(opModeIsActive()){
//            odometry.update();
//
//            //Display Global (x, y, theta) coordinates
//            telemetry.addData("X Position", odometry.returnXCoordinate() / Constants.OdometryConstants.ENCODER_TO_DISTANCE_RATIO);
//            telemetry.addData("Y Position", odometry.returnYCoordinate() / Constants.OdometryConstants.ENCODER_TO_DISTANCE_RATIO);
//            telemetry.addData("Orientation (Degrees)", odometry.returnOrientation());

            telemetry.addData("Left", verticalLeft.getCurrentPosition());
            telemetry.addData("Right", verticalRight.getCurrentPosition());
            telemetry.addData("Middle", horizontal.getCurrentPosition());

            telemetry.update();
        }
    }
}