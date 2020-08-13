package com.team9889.ftc2019.auto.actions.utl;

import com.team9889.ftc2019.auto.actions.Action;

import java.util.Arrays;
import java.util.List;

/**
 * Created by joshua9889 on 6/29/2020.
 */
public class ParallelAction extends Action {

    private List<Action> actionList;
    private boolean[] isFinished;
    private int counter = 0;
    private boolean actionStep = true;

    public ParallelAction(List<Action> list) {
        this.actionList = list;
    }

    @Override
    public void setup(String args) {}

    @Override
    public void start() {
        isFinished = new boolean[actionList.size()];
        Arrays.fill(isFinished, false);

        for (Action action : actionList) {
            action.start();
        }
    }

    @Override
    public void update() {
        for (Action action : actionList) {
            int actionIndex = actionList.indexOf(action);
            if(!action.isFinished() && !isFinished[actionIndex])
                action.update();
            else
                isFinished[actionIndex] = true;
        }
    }

    @Override
    public boolean isFinished() {
        boolean isfinished = true;
        for (boolean val : isFinished)
            isfinished = isfinished && val;

        return isfinished;
    }

    @Override
    public void done() {
        for (Action action : actionList) {
            action.done();
        }
    }
}
