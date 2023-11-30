// Copyright (c) FIRST and other WPILib contributors.

// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.PWMTalonSRX;

import java.util.logging.Logger;


/**
 * This is a demo program showing the use of the DifferentialDrive class. Runs the motors with
 * arcade steering.
 */
public class Robot extends TimedRobot
{
    private final PWMTalonSRX leftMotor = new PWMTalonSRX(8);
    private final PWMTalonSRX rightMotor = new PWMTalonSRX(9);
    private final DifferentialDrive robotDrive = new DifferentialDrive(leftMotor, rightMotor);
    private final Joystick stick = new Joystick(0);
    private final Compressor compressor = new Compressor(PneumaticsModuleType.CTREPCM);
    private final DoubleSolenoid solenoid = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 0, 1);
    private final PWMTalonSRX ejectMotor1 = new PWMTalonSRX(6);
    private final PWMTalonSRX ejectMotor2 = new PWMTalonSRX(7);

    private static final int AIRHORN_BUTTON = 1;
    private static final int HEADLIGHTS_BUTTON = 2;
    private static final int COMPRESSOR_INJECT_BUTTON = 3;
    private final Relay headlight1 = new Relay(0);
    private final Relay headlight2 = new Relay(1);
    private final Relay airhorn = new Relay(2);
    private static byte headlightTimer = 0;

    @Override
    public void robotInit()
    {
        // We need to invert one side of the drivetrain so that positive voltages
        // result in both sides moving forward. Depending on how your robot's
        // gearbox is constructed, you might have to invert the left side instead.
        rightMotor.setInverted(true);
        compressor.enableDigital();
    }
    
    
    @Override
    public void teleopPeriodic()
    {
        // Drive with arcade drive.
        // That means that the Y axis drives forward
        // and backward, and the X turns left and right.
        updateHeadlights();
        updateReleaseAir();
        if(stick.getRawButton(COMPRESSOR_INJECT_BUTTON)) solenoid.set(DoubleSolenoid.Value.kForward);
        else solenoid.set(DoubleSolenoid.Value.kReverse);
        toggleRelay(airhorn, stick.getRawButton(AIRHORN_BUTTON));

        robotDrive.arcadeDrive(stick.getY(), stick.getX());
    }
    private void updateHeadlights(){
        boolean headLightsOn = stick.getRawButton(HEADLIGHTS_BUTTON);
        if(headLightsOn){
            headlightTimer = (byte)((headlightTimer +1)%20);
            boolean firstLightOn = headlightTimer <= 10;
            toggleRelay(headlight1, firstLightOn);
            toggleRelay(headlight2, !firstLightOn);

        } else {
            toggleRelay(headlight1, false);
            toggleRelay(headlight2, false);
        }
    }
    private void updateReleaseAir(){
        double pullAmount = stick.getRawAxis(2); //0.0 - 1.0
        if(pullAmount < 0.5) {
            ejectMotor1.set(-1.0);
            ejectMotor2.set(-1.0);
        } else {
            ejectMotor1.set(1.0);
            ejectMotor2.set(1.0);
        }
    }
    public void toggleRelay(Relay relay, boolean on){
        if(on){
            relay.set(Relay.Value.kOn);
            relay.set(Relay.Value.kReverse);
        } else {
            relay.set(Relay.Value.kOff);
            relay.set(Relay.Value.kForward);
        }
    }
}
