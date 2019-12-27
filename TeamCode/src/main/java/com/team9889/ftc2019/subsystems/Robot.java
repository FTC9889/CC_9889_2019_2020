package com.team9889.ftc2019.subsystems;

import com.qualcomm.hardware.rev.RevTouchSensor;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.RobotLog;
import com.team9889.ftc2019.Constants;
import com.team9889.ftc2019.test.teleOp.RevExtensions2;
import com.team9889.lib.control.kinematics.TankDriveKinematicModel;
import com.team9889.lib.hardware.Motor;
import com.team9889.lib.hardware.RevColorDistance;
import com.team9889.lib.hardware.RevIMU;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.openftc.revextensions2.ExpansionHubEx;
import org.openftc.revextensions2.RevBulkData;
//import org.openftc.revextensions2.RevExtensions2;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by Eric on 7/26/2019.
 */

public class Robot{

    public WebcamName webcam;

    public Motor fLDrive, fRDrive, bLDrive, bRDrive;
    public Servo foundationHook;
    public DistanceSensor foundationDetector;

    public Motor intakeLeft, intakeRight;
    public Servo intakeLeftS, intakeRightS;
    public CRServo roller;
    public DistanceSensor blockDetector;

    public Motor leftLift, rightLift;
    public Servo grabber, linearBar;
    public RevTouchSensor downLimit;

    RevBulkData bulkDataMaster, bulkDataSlave;
    ExpansionHubEx revHubMaster, revHubSlave;

    public HardwareMap hardwareMap;

    public static ElapsedTime timer = new ElapsedTime();
    public static ElapsedTime gyroTimer = new ElapsedTime();

    private static Robot mInstance = null;

    public static Robot getInstance() {
        if (mInstance == null)
            mInstance = new Robot();

        return mInstance;
    }

    private MecanumDrive mMecanumDrive = new MecanumDrive();
    private Intake mIntake = new Intake();
    private ScoringLift mLift = new ScoringLift();
    private Camera mCamera = new Camera();

    public double gyroAfterAuto;

    public RevIMU imu = null;

    public double gyro;
    private Thread trackerThread;

    private boolean mAuto = false;

    public void init(HardwareMap hardwareMap, boolean auto){
        timer.reset();
        gyroTimer.reset();
        this.mAuto = auto;
        this.hardwareMap = hardwareMap;

        Date currentData = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd.M.yyyy hh:mm:ss");

        RobotLog.a("Robot Init Started at " + format.format(currentData));

        // Rev Hubs
        revHubMaster = hardwareMap.get(ExpansionHubEx.class, Constants.kRevHubMaster);
        revHubSlave = hardwareMap.get(ExpansionHubEx.class, Constants.kRevHubSlave);

        // Camera
        webcam = hardwareMap.get(WebcamName.class, Constants.kWebcam);

        // Drive
        fLDrive = new Motor(hardwareMap, Constants.DriveConstants.kLeftDriveMasterId, 1,
                DcMotorSimple.Direction.FORWARD, true, false, true);
        bLDrive = new Motor(hardwareMap, Constants.DriveConstants.kLeftDriveSlaveId, 1,
                DcMotorSimple.Direction.FORWARD, true, false, true);
        fRDrive = new Motor(hardwareMap, Constants.DriveConstants.kRightDriveMasterId, 1,
                DcMotorSimple.Direction.REVERSE, true, false, true);
        bRDrive = new Motor(hardwareMap, Constants.DriveConstants.kRightDriveSlaveId, 1,
                DcMotorSimple.Direction.REVERSE, true, false, true);

        foundationHook = hardwareMap.get(Servo.class, Constants.DriveConstants.kFoundationHook);

        foundationDetector = hardwareMap.get(DistanceSensor.class, Constants.kFoundationDetector);

        //Intake
        intakeLeft = new Motor(hardwareMap, Constants.IntakeConstants.kIntakeLeftMotorId, 1,
                DcMotorSimple.Direction.FORWARD, false, true, false);
        intakeRight = new Motor(hardwareMap, Constants.IntakeConstants.kIntakeRightMotorId, 1,
                DcMotorSimple.Direction.REVERSE, false, true, false);

        intakeLeftS = hardwareMap.get(Servo.class, Constants.IntakeConstants.kIntakeLeftServoId);
        intakeRightS = hardwareMap.get(Servo.class, Constants.IntakeConstants.kIntakeRightServoId);
        roller = hardwareMap.crservo.get(Constants.IntakeConstants.kIntakeRollerId);

        blockDetector = hardwareMap.get(DistanceSensor.class, Constants.IntakeConstants.kIntakeBlockDetectorId);

        //Lift
        leftLift = new Motor(hardwareMap, Constants.LiftConstants.kLeftLift, 1,
                DcMotorSimple.Direction.FORWARD, true, false, true);
        rightLift = new Motor(hardwareMap, Constants.LiftConstants.kRightLift, 1,
                DcMotorSimple.Direction.REVERSE, true, true, true);

        grabber = hardwareMap.get(Servo.class, Constants.LiftConstants.kGrabber);
        linearBar = hardwareMap.get(Servo.class, Constants.LiftConstants.kLinearBar);

        downLimit = hardwareMap.get(RevTouchSensor.class, Constants.LiftConstants.kDownLimit);

        imu = new RevIMU("imu", hardwareMap);

        Runnable trackerRunnable = new Runnable() {
            @Override
            public void run() {
                while (!Thread.interrupted())
                    Robot.getInstance().update();
            }
        };

        trackerThread = new Thread(trackerRunnable);
        trackerThread.start();

        getMecanumDrive().init(auto);
        getIntake().init(auto);
        getCamera().init(auto);
        getLift().init(auto);
    }

    public void update(){
        bulkDataMaster = revHubMaster.getBulkInputData();
        bulkDataSlave = revHubSlave.getBulkInputData();

        fRDrive.update(bulkDataMaster);
        bRDrive.update(bulkDataMaster);
        fLDrive.update(bulkDataMaster);
        bLDrive.update(bulkDataMaster);

        intakeLeft.update(bulkDataSlave);
        intakeRight.update(bulkDataSlave);

        leftLift.update(bulkDataSlave);
        rightLift.update(bulkDataSlave);

        if(Robot.gyroTimer.milliseconds() > 100 && !mAuto){
            gyroTimer.reset();
            getMecanumDrive().getAngle().getTheda(AngleUnit.RADIANS);
        } else { // Update every time in order for odometry to work properly
            getMecanumDrive().getAngle().getTheda(AngleUnit.RADIANS);
        }

        mMecanumDrive.update();
    }

    public void outputToTelemetry(Telemetry telemetry) {
        getMecanumDrive().outputToTelemetry(telemetry);
    }

    public void stop(){
        trackerThread.interrupt();

        for (Motor motor:Arrays.asList(fLDrive, fRDrive, bLDrive, bRDrive, intakeLeft, intakeRight)) {
            motor.setPower(0);
        }
    }
    public MecanumDrive getMecanumDrive(){
        return mMecanumDrive;
    }

    public Intake getIntake(){
        return mIntake;
    }

    public ScoringLift getLift(){
        return mLift;
    }

    public Camera getCamera(){
        return mCamera;
    }

}
