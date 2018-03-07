package robotics;

import lejos.hardware.motor.Motor;
import lejos.utility.Delay;
public class Movement {

	public static void testMotor() {
		Motor.A.setSpeed(360);
		Motor.B.setSpeed(360);
		Motor.A.forward();
		Motor.B.forward();
		try {
			Thread.sleep(2000);
		}
		catch(Exception e) {
			Motor.B.stop();
			Motor.A.stop();
		}
	}
	
	public static void testMotor2() {
	   Motor.A.setAcceleration(1000); 
	   Motor.B.setAcceleration(1000); 
	   Motor.A.setSpeed(180); 
	   Motor.B.setSpeed(180); 
	   Motor.A.forward();  
	   Motor.B.forward();
	   try{
		   Thread.sleep(1000);
		   }
	   catch(Exception e)
	   {}
	   Motor.A.stop();     
	   Motor.B.stop();
	   Motor.A.rotate(10); 
	   Motor.B.rotate(10);
	   try{
		   Thread.sleep(1000);
		   }
	   catch(Exception e)
	   {}
	   Motor.A.rotateTo(Motor.B.getTachoCount());
  
	}
	
	public static void goForward(int time) {
		Motor.A.forward();
		Motor.B.forward();
		try {
			Thread.sleep(time * 1000);
		}
		catch (Exception e) {
			
		}
		Motor.A.stop();
		Motor.B.stop();
		Motor.A.rotateTo(Motor.B.getTachoCount());
	}
	
	public static void goLeft() {
		Motor.A.rotate(90);
	}
	
	public static void goRight() {
		Motor.B.rotate(1100);
	}
	
	public static void Assignement1() {
		Movement.goForward(4);
		int t = Motor.A.getSpeed();
		//Movement.goLeft();
		//Movement.goRight();
		//Main.printSomething(t + "", 4000);
		//System.out.println("Going for 4seconds");
		Movement.goRight();
		Movement.goForward(2);
		Movement.goLeft();
		Movement.goForward(2);
		Movement.goRight();
		Movement.goForward(4);
		Movement.goRight();
		Movement.goForward(4);
		Movement.goRight();
		Movement.goForward(2);
		Movement.goLeft();
		Movement.goForward(2);
		Movement.goRight();
		Movement.goForward(4);
	}
}
