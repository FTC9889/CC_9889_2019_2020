package com.team9889.lib.android;

import java.util.Date;

/**
 * Created by joshua9889 on 6/28/2020.
 * Class to make it easier to keep track of time.
 */
public class Time {
    private Date date = new Date();
    private long currentTime, lastTime;

    public Time() {
        lastTime = date.getTime();
    }

    /**
     * @return Change in time since last method call.
     */
    public long getDt() {
        long currentTime = now();
        long dt = currentTime - lastTime;
        lastTime = currentTime;
        return dt;
    }

    /**
     * @return Last retrieved time in milliseconds
     */
    public long getLastTime() {
        return currentTime;
    }


    /**
     * @return Current time in milliseconds
     */
    public long now() {
        currentTime = date.getTime();
        return getLastTime();
    }

    @Override
    public String toString() {
        return "Current Time: " + getLastTime();
    }
}
