package com.team9889.ftc2019.subsystems;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.team9889.ftc2019.Constants;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import java.util.ArrayList;
import java.util.List;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XYZ;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.YZX;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.EXTRINSIC;

/**
 * Created by MannoMation on 10/27/2018.
 */

public class Camera extends Subsystem{
    private VuforiaLocalizer vuforia = null;
    private static final float mmPerInch = 25.4f;
    private static final float stoneZ = 2.00f * mmPerInch;

    private final float CAMERA_FORWARD_DISPLACEMENT  = -4.0f * mmPerInch;   // eg: Camera is 4 Inches in front of robot center
    private final float CAMERA_VERTICAL_DISPLACEMENT = 5.5f * mmPerInch;   // eg: Camera is 8 Inches above ground
    private final float CAMERA_LEFT_DISPLACEMENT     = 7.5f;     // eg: Camera is ON the robot's center line

    private final float phoneXRotate = 90;
    private final float phoneYRotate = -90;
    private final float phoneZRotate = 180;

    private VuforiaTrackables targetsSkyStone = null;
    List<VuforiaTrackable> allTrackables = null;

    private OpenGLMatrix lastLocation = null;

    @Override
    public void init(boolean auto) {
        if (auto) {
        }
    }

    public void initVuforia(OpMode opMode){
        int cameraMonitorViewId = opMode.hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", opMode.hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);

        //VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();
        parameters.vuforiaLicenseKey = Constants.kVuforiaLicenceKey;
        parameters.cameraDirection   = VuforiaLocalizer.CameraDirection.BACK;

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Load the data sets for the trackable objects. These particular data
        // sets are stored in the 'assets' part of our application.
        targetsSkyStone = this.vuforia.loadTrackablesFromAsset("Skystone");

        VuforiaTrackable stoneTarget = targetsSkyStone.get(0);
        stoneTarget.setName("Stone Target");

        allTrackables = new ArrayList<VuforiaTrackable>();
        allTrackables.addAll(targetsSkyStone);

        stoneTarget.setLocation(OpenGLMatrix
                .translation(0, 0, stoneZ)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, -90)));

        OpenGLMatrix robotFromCamera = OpenGLMatrix
                .translation(CAMERA_FORWARD_DISPLACEMENT, CAMERA_LEFT_DISPLACEMENT, CAMERA_VERTICAL_DISPLACEMENT)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, YZX, DEGREES, phoneYRotate, phoneZRotate, phoneXRotate));

        for (VuforiaTrackable trackable : allTrackables) {
            ((VuforiaTrackableDefaultListener) trackable.getListener()).setPhoneInformation(robotFromCamera, parameters.cameraDirection);
        }
    }

    public void startTracking(){
        targetsSkyStone.activate();
    }
    public void stopTracking(){
        targetsSkyStone.deactivate();
    }

    @Override
    public void outputToTelemetry(Telemetry telemetry) {

    }


    @Override
    public void update() {

    }

    public double[] getCurrentSkyStonePosition(){

        // check all the trackable targets to see which one (if any) is visible.
        boolean targetVisible = false;
        for (VuforiaTrackable trackable : allTrackables) {
            if (((VuforiaTrackableDefaultListener) trackable.getListener()).isVisible()) {
                targetVisible = true;

                OpenGLMatrix robotLocationTransform = ((VuforiaTrackableDefaultListener)trackable.getListener()).getUpdatedRobotLocation();
                if (robotLocationTransform != null) {
                    lastLocation = robotLocationTransform;
                }
                break;
            }
        }

        // Provide feedback as to where the robot is located (if we know).
        if (targetVisible) {
            // express position (translation) of robot in inches.
            VectorF translation = lastLocation.getTranslation();

            // express the rotation of the robot in degrees.
            Orientation rotation = Orientation.getOrientation(lastLocation, EXTRINSIC, XYZ, DEGREES);

            // return {X, Y, Z, Roll, Pitch, Heading}
            return new double[]{translation.get(0) / mmPerInch, translation.get(1) / mmPerInch,
                    translation.get(2) / mmPerInch,
                    rotation.firstAngle, rotation.secondAngle, rotation.thirdAngle};
        } else {
            return new double[6];
        }


    }

    public double getStoredSkyStonePosition(){
//        getImage();
//
        double skyStonePosition =0;// detector.getXPosition();

        if (skyStonePosition < 100){
            return 1;
        }else if (skyStonePosition > 99 && skyStonePosition < 200){
            return 2;
        }
        else {
            return 3;
        }
    }

//    @Override
    public void stop() {

    }

    @Override
    public String toString() {
        return "Camera";
    }

}
