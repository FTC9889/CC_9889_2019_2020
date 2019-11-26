package com.team9889.ftc2019.test.local;

import com.team9889.lib.android.FileWriter;
import com.team9889.lib.control.controllers.cruiseController;
import com.team9889.lib.control.motion.MotionProfileSegment;

/**
 * Created by joshua9889 on 11/26/2019.
 */
public class CruiseControllerTest {
    public static void main(String... args) {

        FileWriter log = new FileWriter(cruiseController.class.getClass().getSimpleName() + ".csv");
        cruiseController controller = new cruiseController(0.04, 0.12);
        int step = 1000;
        for (int i = 0; i < step; i++) {
            double output =  controller.update(i, step);
            System.out.println( "Step: " + i + " | Output: " + output);
            log.write(output);
        }

        log.close();
    }
}
