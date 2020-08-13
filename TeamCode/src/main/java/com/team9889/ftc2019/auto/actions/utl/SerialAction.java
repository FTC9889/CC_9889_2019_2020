package com.team9889.ftc2019.auto.actions.utl;

import com.team9889.ftc2019.auto.actions.Action;

import java.util.List;

/**
 * Created by joshua9889 on 6/28/2020.
 */
public class SerialAction extends Action {

    private List<Action> actionList;
    private int counter = 0;
    private boolean actionStep = true;

    public SerialAction(List<Action> actions) {
        this.actionList = actions;
    }

    @Override
    public void setup(String args) {}

    @Override
    public void start() {

    }

    @Override
    public void update() {
        Action currentAction = actionList.get(counter);

        if(!currentAction.isFinished()) {
            if(actionStep) {
                currentAction.start();
                actionStep = false;
            } else
                currentAction.update();
        } else {
            currentAction.done();
            actionStep = true;
            counter++;
        }
    }

    @Override
    public boolean isFinished() {
        return counter > actionList.size();
    }

    @Override
    public void done() {

    }
}
