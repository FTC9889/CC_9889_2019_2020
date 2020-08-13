package com.team9889.ftc2019.auto.actions.utl;

import com.team9889.ftc2019.auto.actions.Action;
import com.team9889.lib.control.Timeout;

/**
 * Created by joshua9889 on 6/29/2020.
 */
public class TimeoutAction extends Action {
    private Action action;
    private Timeout timeout;

    public TimeoutAction(Action action, Timeout timeout) {
        this.action = action;
        this.timeout = timeout;
    }

    @Override
    public void setup(String args) {}

    @Override
    public void start() {
        timeout.restart();
        action.start();
    }

    @Override
    public void update() {
        action.update();
    }

    @Override
    public boolean isFinished() {
        return timeout.is_timed_out() || action.isFinished();
    }

    @Override
    public void done() {
        action.done();
    }
}
