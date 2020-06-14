package com.team9889.ftc2019.subsystems;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.hardware.rev.RevTouchSensor;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.RobotLog;
import com.team9889.ftc2019.Constants;
import com.team9889.ftc2019.DriverStation;
import com.team9889.lib.android.FileWriter;
import com.team9889.lib.hardware.Motor;
import com.team9889.lib.hardware.RevIMU;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.openftc.revextensions2.ExpansionHubEx;
import org.openftc.revextensions2.RevBulkData;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

//import org.openftc.revextensions2.RevExtensions2;

/**
 * Created by Eric on 7/26/2019.
 */

public class Robot{

    public WebcamName webcam;

    public Motor fLDrive, fRDrive, bLDrive, bRDrive;
    public Servo foundationHook;
    public DistanceSensor foundationDetector;
    public RevIMU imu = null;


    public Motor intakeLeft, intakeRight;
    public Servo intakeLeftS, intakeRightS;
    public CRServo roller;
    public DistanceSensor blockDetector;

    public Motor leftLift, rightLift;
    public Servo grabber, linearBar, liftBrake;
    public RevTouchSensor downLimit;

    public Servo odometryLifter;

    public Servo tapeMeasureDeploy;

    public Servo teamMarkerDeployServo;

    public boolean redAuto;

    RevBulkData bulkDataMaster, bulkDataSlave;
    ExpansionHubEx revHubMaster, revHubSlave;

    public HardwareMap hardwareMap;

    int lastTime = 0, loopTimeOverCount = 0, loopTimeOverCount30 = 0, loopTimeOverCount25 = 0;

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

    private boolean mAuto = false;

    boolean debugging = false;
    private com.team9889.lib.android.FileWriter writer = new FileWriter("Drive3.csv");
    private com.team9889.lib.android.FileWriter poseWriter = new FileWriter("Pose.csv");

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
        liftBrake = hardwareMap.get(Servo.class, Constants.LiftConstants.kLiftBrake);

        downLimit = hardwareMap.get(RevTouchSensor.class, Constants.LiftConstants.kDownLimit);

        odometryLifter = hardwareMap.get(Servo.class, Constants.OdometryConstants.kOdometryLift);

        teamMarkerDeployServo = hardwareMap.get(Servo.class, Constants.IntakeConstants.kCapStone);

        tapeMeasureDeploy = hardwareMap.get(Servo.class, Constants.kTMShooter);
        tapeMeasureDeploy.setPosition(1);

        imu = new RevIMU("imu1", hardwareMap);

        if (auto)
            debugging = true;

        if(debugging) writer.write("clock,x,y,theda");

        getMecanumDrive().init(auto);
        getIntake().init(auto);
        getCamera().init(auto);
        getLift().init(auto);

        if (auto)
            teamMarkerDeployServo.setPosition(1);

        lastTime = 0;
        timer.reset();

        loopTimeOverCount = 0;
        loopTimeOverCount25 = 0;
        loopTimeOverCount30 = 0;
    }


    private ElapsedTime updateTimer = new ElapsedTime();
    public void update(){
        RobotLog.v("loop time " + (timer.milliseconds()));


//        if (redAuto && mAuto) {
//                getMecanumDrive().getAngle().getTheda(AngleUnit.RADIANS);
//        }else {
//            if (Robot.gyroTimer.milliseconds() > 100) {
//                gyroTimer.reset();
        getMecanumDrive().getAngle().getTheda(AngleUnit.RADIANS);
//                getMecanumDrive().updated = true;

//                if (getMecanumDrive().currentPose.getX() > getMecanumDrive().currentPose.getY())
//                    getMecanumDrive().setCurrentPose(new Pose2d(getMecanumDrive().currentPose.getY(), getMecanumDrive().currentPose.getY(), getMecanumDrive().gyroAngle.getTheda(AngleUnit.DEGREES)));
//                else if (getMecanumDrive().currentPose.getY() > getMecanumDrive().currentPose.getX())
//                    getMecanumDrive().setCurrentPose(new Pose2d(getMecanumDrive().currentPose.getX(), getMecanumDrive().currentPose.getX(), getMecanumDrive().gyroAngle.getTheda(AngleUnit.DEGREES)));

//                getMecanumDrive().setCurrentPose(new Pose2d(getMecanumDrive().getCurrentPose().getX(), getMecanumDrive().getCurrentPose().getY(), getMecanumDrive().gyroAngle.getTheda(AngleUnit.RADIANS)));
//            }
//        }
        if (mAuto){
//            bulkDataMaster = revHubMaster.getBulkInputData();
            bulkDataSlave = revHubSlave.getBulkInputData();

//            fRDrive.update(bulkDataMaster);
//            bRDrive.update(bulkDataMaster);
//            fLDrive.update(bulkDataMaster);
//            bLDrive.update(bulkDataMaster);

            intakeLeft.update(bulkDataSlave);
            intakeRight.update(bulkDataSlave);

            leftLift.update(bulkDataSlave);
            rightLift.update(bulkDataSlave);

            mMecanumDrive.update();
        }

        if(debugging)
            writer.write(updateTimer.milliseconds() + "," + getMecanumDrive().getCurrentPose().getX() + ","
                    + getMecanumDrive().getCurrentPose().getY() + ","
                    + -Robot.getInstance().getMecanumDrive().gyroAngle.getTheda(AngleUnit.RADIANS)
            );

        updateTimer.reset();
        if (timer.milliseconds() - lastTime > 20)
            loopTimeOverCount++;
        else if (timer.milliseconds() - lastTime > 25)
            loopTimeOverCount25++;
        else if (timer.milliseconds() - lastTime > 30)
            loopTimeOverCount30++;

        while (timer.milliseconds() - lastTime < 25){

        }

        lastTime = (int) timer.milliseconds();
    }

    public void outputToTelemetry(Telemetry telemetry) {
        getMecanumDrive().outputToTelemetry(telemetry);
//        telemetry.addData("Loop Time", (timer.milliseconds() - lastTime));
//        telemetry.addData("Loop Time Over 20 Milliseconds", loopTimeOverCount);
//        telemetry.addData("Loop Time Over 25 Milliseconds", loopTimeOverCount25);
//        telemetry.addData("Loop Time Over 30 Milliseconds", loopTimeOverCount30);
    }

    public void stop(){
        if(debugging) writer.close();

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
