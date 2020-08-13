package com.team9889.lib.control;

import com.team9889.lib.android.Time;

import java.util.concurrent.TimeUnit;

/**
 * Created by joshua9889 on 6/28/2020.
 * Class to deal with all kinds of timeouts, mainly used for actions and teleop automation.
 */
public class Timeout {
    private long startTime = 0;
    private Time currentTime = new Time();
    private double millisecondTimeout;

    public Timeout(int time, TimeUnit unit) {
        double factor = 1;
        switch (unit) {
            case SECONDS:
                factor = 1000;
                break;
            case MILLISECONDS:
                factor = 1;
                break;
        }
        millisecondTimeout = factor * time;
    }

    /**
     * @return If the proper amount of time has elapsed.
     */
    public boolean is_timed_out() {
        return currentTime.now() - (startTime + millisecondTimeout) >= 0;
    }

    /**
     * Restart the timeout.
     */
    public void restart() {
        startTime = currentTime.now();
    }
}
