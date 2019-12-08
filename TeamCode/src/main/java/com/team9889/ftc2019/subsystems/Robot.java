package com.team9889.ftc2019.subsystems;

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
import org.openftc.revextensions2.ExpansionHubEx;
import org.openftc.revextensions2.RevBulkData;
//import org.openftc.revextensions2.RevExtensions2;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by Eric on 7/26/2019.
 */

public class Robot{

    // motors (remember to stop motors)
    public Motor fLDrive, fRDrive, bLDrive, bRDrive;
    public Servo foundationHook;

    public Motor intakeLeft, intakeRight;
    public Servo intakeLeftS, intakeRightS;
    public CRServo roller;
    public DistanceSensor blockDetector;

    public Motor leftLift, rightLift;
    public Servo grabber, linearBar;
    public ColorSensor downLimit;
//    public Motor hangingLiftMotor;
//    public Motor intakeMotor, intakeExtender;
//
//    public CRServo hangingHook;
//    public Servo intakeRotator, intakeGate, markerDumper;
//    public Servo xAxis, yAxis;
//
//    public DigitalChannel hangingLowerLimit;
//    public DigitalChannel intakeInSwitch;

    RevBulkData bulkDataMaster, bulkDataSlave;
    ExpansionHubEx revHubMaster, revHubSlave;

    public HardwareMap hardwareMap;

    private int lastLeftPosition, lastRightPosition;
    TankDriveKinematicModel model = new TankDriveKinematicModel();

    public static ElapsedTime timer = new ElapsedTime();

    public double[] pose = new double[4];

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

    public RevIMU imu = null;


    public void init(HardwareMap hardwareMap, boolean auto){
        timer.reset();
        this.hardwareMap = hardwareMap;

        Date currentData = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd.M.yyyy hh:mm:ss");

        RobotLog.a("Robot Init Started at " + format.format(currentData));

        revHubMaster = hardwareMap.get(ExpansionHubEx.class, Constants.kRevHubMaster);
        revHubSlave = hardwareMap.get(ExpansionHubEx.class, Constants.kRevHubSlave);

        fLDrive = new Motor(hardwareMap, Constants.DriveConstants.kLeftDriveMasterId, 1,
                DcMotorSimple.Direction.FORWARD, true, false, true);
        bLDrive = new Motor(hardwareMap, Constants.DriveConstants.kLeftDriveSlaveId, 1,
                DcMotorSimple.Direction.FORWARD, true, false, true);
        fRDrive = new Motor(hardwareMap, Constants.DriveConstants.kRightDriveMasterId, 1,
                DcMotorSimple.Direction.REVERSE, true, false, true);
        bRDrive = new Motor(hardwareMap, Constants.DriveConstants.kRightDriveSlaveId, 1,
                DcMotorSimple.Direction.REVERSE, true, false, true);

        foundationHook = hardwareMap.get(Servo.class, Constants.DriveConstants.kFoundationHook);

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
                DcMotorSimple.Direction.FORWARD, true, true, true);
        rightLift = new Motor(hardwareMap, Constants.LiftConstants.kRightLift, 1,
                DcMotorSimple.Direction.REVERSE, true, true, true);

        grabber = hardwareMap.get(Servo.class, Constants.LiftConstants.kGrabber);
        linearBar = hardwareMap.get(Servo.class, Constants.LiftConstants.kLinearBar);

        downLimit = hardwareMap.get(ColorSensor.class, Constants.LiftConstants.kDownLimit);

//        if(auto)
            imu = new RevIMU("imu", hardwareMap);

            if (auto){
                getIntake().IntakeUp();
                getLift().GrabberOpen();
                getMecanumDrive().OpenFoundationHook();
                update();
            }
    }

    boolean first = true;
    public void update(){
        mMecanumDrive.update();

        bulkDataMaster = revHubMaster.getBulkInputData();
//        bulkDataSlave = revHubSlave.getBulkInputData();
//        Motor.numHardwareUsesThisUpdate+=2;

        fRDrive.update(bulkDataMaster);
        bRDrive.update(bulkDataMaster);
        fLDrive.update(bulkDataMaster);
        bLDrive.update(bulkDataMaster);
//        intake.update(bulkDataSlave);

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
        for (Motor motor:Arrays.asList(fLDrive, fRDrive, bLDrive, bRDrive, intakeLeft, intakeRight)) {
            motor.setPower(0);
        }
    }
    public MecanumDrive getMecanumDrive(){
        return mMecanumDrive;
    }
//    public Dumper getDumper(){
//        return null;
//    }
    public Intake getIntake(){
        return mIntake;
    }

    public ScoringLift getLift(){
        return mLift;
    }
//    public HangingLift getHangingLift(){
//        return null;
//    }
    public Camera getCamera(){
        return mCamera;
    }

}
