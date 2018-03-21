package robotics;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;

public class BehaviorNothing implements Behavior {

	public BehaviorNothing() {
		// TODO Auto-generated constructor stub
	}
	
	public boolean takeControl() {
		return true;
	}
	
	public void action() { LCD.drawString("Hello world", 0, 0); Delay.msDelay(1000); }
	
	public void suppress() {}

}
