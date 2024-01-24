// Copyright (c) FIRST and other WPILib contributors.

// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.PWMTalonSRX;

import java.util.Random;

import static edu.wpi.first.wpilibj.DoubleSolenoid.Value.kForward;

public class Robot extends TimedRobot{
    int colorType = 0; //0-3
    private final PWMTalonSRX leftMotor = new PWMTalonSRX(8);
    private final PWMTalonSRX rightMotor = new PWMTalonSRX(9);
    private final DifferentialDrive robotDrive = new DifferentialDrive(leftMotor, rightMotor);
    private final Joystick stick = new Joystick(0);
    private final Compressor compressor = new Compressor(PneumaticsModuleType.CTREPCM);
    private final DoubleSolenoid solenoid = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 0, 1);
    private final PWMTalonSRX ejectMotor1 = new PWMTalonSRX(7);
    private final PWMTalonSRX ejectMotor2 = new PWMTalonSRX(6);
    private static final byte AIRHORN_BUTTON = 1;
    private static final byte HEADLIGHTS_BUTTON = 2;
    private static final byte COMPRESSOR_INJECT_BUTTON = 3;
    private static final byte TOGGLE_LIGHTS_BUTTON = 4;
    private static final byte PREVIOUS_COLOR_BUTTON = 5;
    private static final byte NEXT_COLOR_BUTTON = 6;
    private static final byte LEFT_CANNON_FIRE = 2;
    private static final byte RIGHT_CANNON_FIRE = 3;
    private final Relay headlight1 = new Relay(0);
    private final Relay headlight2 = new Relay(1);
    private final Relay airhorn = new Relay(2);
    private byte headlightTimer = 0;
    private boolean lightsPermToggled = false;
    private final ToggleButtonCallback compressorInjectToggler = new ToggleButtonCallback(stick, COMPRESSOR_INJECT_BUTTON, solenoid::toggle);
    private final ToggleButtonCallback lightsToggler = new ToggleButtonCallback(stick, TOGGLE_LIGHTS_BUTTON, () -> lightsPermToggled = !lightsPermToggled);
    private final ToggleButtonCallback previousColorButtonToggle = new ToggleButtonCallback(stick, PREVIOUS_COLOR_BUTTON, () -> colorType = (colorType+3)%4);

    private final ToggleButtonCallback nextColorButtonToggle = new ToggleButtonCallback(stick, NEXT_COLOR_BUTTON, () -> colorType = (colorType+1)%4);
    DigitalOutput digitalOutput1 = new DigitalOutput(0);
    DigitalOutput digitalOutput2 = new DigitalOutput(1);
    @Override
    public void robotInit()
    {
        // We need to invert one side of the drivetrain so that positive voltages
        // result in both sides moving forward. Depending on how your robot's
        // gearbox is constructed, you might have to invert the left side instead.
        rightMotor.setInverted(true);
        solenoid.set(kForward);
        compressor.disable();
    }


    @Override
    public void teleopPeriodic()
    {
        // Drive with arcade drive.
        // That means that the Y axis drives forward
        // and backward, and the X turns left and right.
        previousColorButtonToggle.tick();
        nextColorButtonToggle.tick();

        switch(colorType){
            case 0:
                digitalOutput1.set(false);
                digitalOutput2.set(false);
                break;
            case 1:
                digitalOutput1.set(true);
                digitalOutput2.set(false);
                break;
            case 2:
                digitalOutput1.set(false);
                digitalOutput2.set(true);
                break;
            case 3:
                digitalOutput1.set(true);
                digitalOutput2.set(true);
                break;
        }

        updateHeadlights();
        updateReleaseAir();

        compressorInjectToggler.tick();

        toggleRelay(airhorn, stick.getRawButton(AIRHORN_BUTTON));

        robotDrive.arcadeDrive(stick.getY(), stick.getX());
    }
    private void updateHeadlights(){
        lightsToggler.tick();

        if(lightsPermToggled){
            toggleRelay(headlight1, true);
            toggleRelay(headlight2, true);
        } else if(stick.getRawButton(HEADLIGHTS_BUTTON)){
            headlightTimer = (byte)((headlightTimer+1)%20);
            boolean firstLightOn = headlightTimer <= 10;
            toggleRelay(headlight1, firstLightOn);
            toggleRelay(headlight2, !firstLightOn);
        } else {
            toggleRelay(headlight1, false);
            toggleRelay(headlight2, false);
        }
    }
    private void updateReleaseAir(){
        double pullAmountLeft = stick.getRawAxis(LEFT_CANNON_FIRE); //0.0 - 1.0
        double pullAmountRight = stick.getRawAxis(RIGHT_CANNON_FIRE); //0.0-1.0
        ejectMotor1.set((int)(pullAmountLeft+.25)); // (>.75) ? 1.0 : 0.0
        ejectMotor2.set((int)(pullAmountRight+.25)); // (>.75) ? 1.0 : 0.0
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
