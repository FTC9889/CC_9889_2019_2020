package com.team9889.ftc2019.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.RobotLog;
import com.team9889.ftc2019.Constants;
import com.team9889.ftc2019.states.LiftStates;
import com.team9889.lib.control.kinematics.TankDriveKinematicModel;
import com.team9889.lib.hardware.Motor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.openftc.revextensions2.ExpansionHubEx;
import org.openftc.revextensions2.ExpansionHubMotor;
import org.openftc.revextensions2.RevBulkData;
import org.openftc.revextensions2.RevExtensions2;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by Eric on 7/26/2019.
 */

public class Robot{

    // motors (remember to stop motors)
    public Motor fLDrive, fRDrive, bLDrive, bRDrive;
    public Motor intake;

    RevBulkData bulkDataMaster, bulkDataSlave;
    ExpansionHubEx revHubMaster, revHubSlave;

    private int lastLeftPosition, lastRightPosition;
    TankDriveKinematicModel model = new TankDriveKinematicModel();

    private static ElapsedTime timer = new ElapsedTime();

    public double[] pose = new double[4];

    private static Robot mInstance = null;

    public static Robot getInstance() {
        if (mInstance == null)
            mInstance = new Robot();

        return mInstance;
    }

    private Drive mDrive = new Drive();

    public void init(HardwareMap hardwareMap, boolean auto){
        timer.reset();

        Date currentData = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd.M.yyyy hh:mm:ss");

        RobotLog.a("Robot Init Started at " + format.format(currentData));

        RevExtensions2.init();

        revHubMaster = hardwareMap.get(ExpansionHubEx.class, Constants.kRevHubMaster);
        revHubSlave = hardwareMap.get(ExpansionHubEx.class, Constants.kRevHubSlave);

        fLDrive = new Motor(hardwareMap, Constants.DriveConstants.kLeftDriveMasterId, 1,
                DcMotorSimple.Direction.REVERSE, true);
        bLDrive = new Motor(hardwareMap, Constants.DriveConstants.kLeftDriveSlaveId, 1,
                DcMotorSimple.Direction.REVERSE, true);
        fRDrive = new Motor(hardwareMap, Constants.DriveConstants.kRightDriveMasterId, 1,
                DcMotorSimple.Direction.FORWARD, true);
        bRDrive = new Motor(hardwareMap, Constants.DriveConstants.kRightDriveSlaveId, 1,
                DcMotorSimple.Direction.FORWARD, true);
        intake = new Motor(hardwareMap, Constants.IntakeConstants.kIntakeMotorId);
    }

    boolean first = true;
    public void update(){
        bulkDataMaster = revHubMaster.getBulkInputData();
        bulkDataSlave = revHubSlave.getBulkInputData();
        Motor.numHardwareUsesThisUpdate+=2;

        fRDrive.update(bulkDataMaster);
        bRDrive.update(bulkDataMaster);
        fLDrive.update(bulkDataSlave);
        bLDrive.update(bulkDataSlave);
        intake.update(bulkDataSlave);

        if(!first)
            pose = model.calculateAbs((fRDrive.getPosition() - lastRightPosition) * Constants.DriveConstants.ENCODER_TO_DISTANCE_RATIO,
                    (fLDrive.getPosition() - lastLeftPosition) * Constants.DriveConstants.ENCODER_TO_DISTANCE_RATIO);
        else
            first = false;

        lastLeftPosition = fLDrive.getPosition();
        lastRightPosition = fRDrive.getPosition();

//        for (Motor motor:Arrays.asList(fLDrive, fRDrive, bLDrive, bRDrive, intake)) {
//            motor.update(bulkDataMaster, bulkDataSlave);
//        }
    }

    public void outputToTelemetry(Telemetry telemetry) {

    }

    public void stop(){
        for (Motor motor:Arrays.asList(fLDrive, fRDrive, bLDrive, bRDrive, intake)) {
            motor.setPower(0);
        }
    }

//    public Dumper getDumper(){
//        return null;
//    }
//    public Intake getIntake(){
//        return null;
//    }
//    public ScoringLift getLift(){
//        return null;
//    }
//    public HangingLift getHangingLift(){
//        return null;
//    }
//    public Camera getCamera(){
//        return null;
//    }

}
