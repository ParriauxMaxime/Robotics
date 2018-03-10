package robotics;
import lejos.hardware.motor.Motor;
import lejos.utility.Delay;

public class Movement {
	static double trackWidth = 14.0;
	
	public static void goForward(int time) {
		Motor.C.resetTachoCount();
		Motor.B.resetTachoCount();
		Motor.B.forward();
		Motor.C.forward();
		try {
			Thread.sleep(time * 500);
		}
		catch (Exception e) {
			
		}
		Motor.B.stop();
		Motor.C.stop();
		Motor.B.rotateTo(Motor.C.getTachoCount());
	}
	
	public static void goLeft() {
		int w = 90;
		Motor.B.rotate((int)(Movement.trackWidth / 2 * w));
	}
	
	public static void goRight() {
		int w = 90;
		Motor.C.rotate((int)(Movement.trackWidth / 2 * w));
	}
	
	public static void Assignement1() {
		Movement.goForward(2);
		//Movement.goLeft();
		//int t = Motor.A.getSpeed();
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
		Movement.goRight();
	}
}
