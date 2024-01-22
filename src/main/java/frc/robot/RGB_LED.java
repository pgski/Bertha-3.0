package frc.robot;

import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PWM;

public class RGB_LED {
    private final DigitalOutput redLight;
    private final DigitalOutput greenLight;
    private final DigitalOutput blueLight;

    public RGB_LED(int red, int green, int blue) {
        redLight = new DigitalOutput(red);
        greenLight = new DigitalOutput(green);
        blueLight = new DigitalOutput(blue);
        redLight.enablePWM(0.6);
        greenLight.enablePWM(0.6);
        blueLight.enablePWM(0.6);

    }

    public void update(int red_light, int green_light, int blue_light) {
        redLight.setPWMRate(red_light);
        greenLight.setPWMRate(green_light);
        blueLight.setPWMRate(blue_light);
        System.out.println(red_light);
    }
}