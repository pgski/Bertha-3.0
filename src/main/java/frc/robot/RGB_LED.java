package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PWM;

public class RGB_LED {
    private final PWM redLight;
    private final PWM greenLight;
    private final PWM blueLight;
    private final Joystick controller;

    public RGB_LED(int red, int green, int blue, int controller_port) {
        redLight = new PWM(red);
        greenLight = new PWM(green);
        blueLight = new PWM(blue);
        controller = new Joystick(controller_port);
    }

    public void update() {
        if (controller.getRawButtonPressed(2)) {
            redLight.setRaw(255);
            greenLight.setRaw(255);
            blueLight.setRaw(255);
        }
    }
}