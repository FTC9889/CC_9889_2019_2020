package com.team9889.ftc2019.auto;

import com.qualcomm.robotcore.util.ElapsedTime;
import com.team9889.ftc2019.Team9889Linear;
import com.team9889.ftc2019.auto.actions.Action;
import com.team9889.ftc2019.auto.actions.RobotUpdate;
import com.team9889.ftc2019.auto.modes.RedAuto;
import com.team9889.lib.android.FileReader;

import java.lang.reflect.Array;
import java.util.List;

/**
 * Created by joshua9889 on 8/5/2017.
 */

public abstract class AutoModeBase extends Team9889Linear {

    // Autonomous Settings
    private Side currentAutoRunning = AutoModeBase.Side.RED;
    protected SkyStonePosition currentSkyStonePosition = SkyStonePosition.RIGHT;

    // Timer for autonomous
    protected ElapsedTime autoTimer = new ElapsedTime();

    protected enum Side {
        RED, BLUE;

        private static String redString = "Red";
        private static String blueString = "Blue";

        private static int RED_Num = 1;
        private static int BLUE_Num = -1;

        public static int getNum(Side side){
            return side == RED ? RED_Num : BLUE_Num;
        }

        public static Side fromText(String side) {
            return side.equals(redString) ? RED : BLUE;
        }
    }

    // Stone positions relative to the robot starting position
    public enum SkyStonePosition {
        LEFT, MIDDLE, RIGHT
    }

    // Test getting the Side number
    public static void main(String... args) {
        com.team9889.ftc2019.auto.AutoModeBase.Side side = AutoModeBase.Side.BLUE;

        System.out.println(AutoModeBase.Side.getNum(side));
    }

    // Checks for a saved file to see what auto we are running (not completely implemented yet)
    private void setCurrentAutoRunning(){
//        String filename = "autonomousSettings.txt";
//        FileReader settingsFile = new FileReader(filename);
//
//        String[] settings = settingsFile.lines();
//        settingsFile.close();

//        this.currentAutoRunning = Side.fromText(settings[0]);
    }

    // Method to implement in the auto to run the autonomous
    public abstract void run(Side side, SkyStonePosition stonePosition);

    @Override
    public void runOpMode() throws InterruptedException{
        setCurrentAutoRunning();

        waitForStart(true);
        ThreadAction(new RobotUpdate());
        autoTimer.reset();

        // From Camera -> To Stone Position
        if (positionOfSkyStone < 120 && positionOfSkyStone > 59)
            currentSkyStonePosition = SkyStonePosition.LEFT;
        else if (positionOfSkyStone > 119)
            currentSkyStonePosition = SkyStonePosition.MIDDLE;
        else if (positionOfSkyStone < 60)
            currentSkyStonePosition = SkyStonePosition.RIGHT;

        // If the opmode is still running, run auto
        if (opModeIsActive() && !isStopRequested()) {
            run(currentAutoRunning, currentSkyStonePosition);
        }

        // Stop all movement
        finalAction();
    }


    /**
     * Run a single action, once, thread-blocking
     * @param action Action Class wanting to run
     */
    public void runAction(Action action){
        if(opModeIsActive() && !isStopRequested())
            action.start();

        while (!action.isFinished() && opModeIsActive() && !isStopRequested()) {
            action.update();
            Robot.outputToTelemetry(telemetry);
            telemetry.update();
        }

        if(opModeIsActive() && !isStopRequested()) {
            action.done();
        }
    }

    /**
     * Run a single action, once, in a new thread
     * Caution to make sure that you don't run more one action on the same subsystem
     * @param action Action Class wanting to run
     */
    public void ThreadAction(final Action action){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                runAction(action);
            }
        };

        if(opModeIsActive() && !isStopRequested())
            new Thread(runnable).start();
    }

    /**
     * Run multiple actions at the same time, stop when all actions are completed
     * @param actions A List of Action objects
     *
     * @example ParallelActions(Arrays.asList (
     *                         new MecanumDriveSimpleAction ( 0, - 20, 1000),
     *                         new Intake()
     *                 ));
     */
    public void ParallelActions(List<Action> actions) {
        if(opModeIsActive() && !isStopRequested())
            for (Action action : actions) {
                action.start();
            }

        boolean all_finished = false;
        while (!all_finished && opModeIsActive() && !isStopRequested()) {
            all_finished = false;
            for (Action action : actions) {
                if (!action.isFinished()) {
                    action.update();
                    all_finished = true;
                }
            }
        }

        for (Action action : actions) {
            action.done();
        }
    }

}