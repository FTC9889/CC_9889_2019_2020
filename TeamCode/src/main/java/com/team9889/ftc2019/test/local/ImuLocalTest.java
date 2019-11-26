package com.team9889.ftc2019.test.local;

import com.team9889.lib.android.FileWriter;

/**
 * Created by joshua9889 on 11/26/2019.
 */
public class ImuLocalTest {
    public static void main(String... args) {
        FileWriter log = new FileWriter("IMUTest.csv");

        double wantedAngle = -70;
        for (int realAngle = -180; realAngle <= 180; realAngle++) {
            double currentAngle = realAngle;

            if(Math.abs(wantedAngle) > 175) {
                currentAngle = Math.signum(currentAngle) > 0 ? currentAngle - 175 : currentAngle + 175;
                wantedAngle = Math.signum(wantedAngle) > 0 ? wantedAngle - 175 : wantedAngle + 175;
            }

            log.write(currentAngle + ", " + wantedAngle);

        }

        log.close();
    }
}
