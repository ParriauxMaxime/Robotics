package robotics;

import lejos.hardware.lcd.*;
import lejos.utility.*;
public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Movement.goLeft();
	}
	
	public static void printSomething(String str, int delay) {
		LCD.drawString(str, 0, 4);
		Delay.msDelay(delay);
	}
}