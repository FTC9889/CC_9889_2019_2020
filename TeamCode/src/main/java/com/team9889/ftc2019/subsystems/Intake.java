package com.team9889.ftc2019.subsystems;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Created by Eric on 8/19/2019.
 */

public class Intake extends Subsystem {

    @Override
    public void init(boolean auto) {

    }

    @Override
    public void outputToTelemetry(Telemetry telemetry) {

    }

    @Override
    public void update() {

    }

    public void SetIntakePower(double power){
        Robot.getInstance().intakeLeft.setPower(power);
        Robot.getInstance().intakeRight.setPower(power);
    }
    public void Intake(){
        SetIntakePower(0.5);
        RollerIn();
    }
    public void Outtake(){
        SetIntakePower(-0.3);
        RollerOut();
    }
    public void Stop(){
        SetIntakePower(0);
        RollerStop();
    }

    public void SetRollerPower(double power){
        Robot.getInstance().roller.setPower(power);
    }
    public void RollerIn(){
        SetRollerPower(1);
    }
    public void RollerStop(){
        SetRollerPower(0);
    }
    public void RollerOut(){
        SetRollerPower(-1);
    }


    public void SetLeftIntakeHeight(double height){
        Robot.getInstance().intakeLeftS.setPosition(height);
    }
    public void SetRightIntakeHeight(double height){
        Robot.getInstance().intakeRightS.setPosition(height);
    }
    public void SetIntakeHeight(double leftHeight, double rightHeight){
        SetLeftIntakeHeight(leftHeight);
        SetRightIntakeHeight(rightHeight);
    }

    //TODO test these two heights
    public void IntakeDown(){
        SetIntakeHeight(1, 0);
    }
    public void IntakeUp(){
        SetIntakeHeight(0, 1);
    }
}