package frc.robot;

import edu.wpi.first.wpilibj.Joystick;

public class ToggleButtonCallback {
    public final byte BUTTON_CHANNEL;
    public final Joystick stick;
    public final Runnable callback;
    public boolean buttonPressedOnLastUpdate = false;
    public ToggleButtonCallback(Joystick stick, byte buttonChannel, Runnable callback){
        BUTTON_CHANNEL = buttonChannel;
        this.stick = stick;
        this.callback = callback;
    }
    public void tick(){
        if(stick.getRawButton(BUTTON_CHANNEL)){
            if(!buttonPressedOnLastUpdate){
                callback.run();
                buttonPressedOnLastUpdate = true;
            }
        } else {
            buttonPressedOnLastUpdate = false;
        }
    }
}
