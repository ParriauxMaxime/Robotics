package robotics;

import behaviors.*;
import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.*;
import lejos.robotics.subsumption.Behavior;
import lejos.robotics.chassis.*;
import lejos.robotics.geometry.*;
import lejos.robotics.mapping.*;
import lejos.robotics.navigation.MoveController;
import lejos.hardware.port.*;

public class BehaviorRobot15 extends AbstractBehaviorRobot {
	static double WHEEL_DIAMETER =  MoveController.WHEEL_SIZE_EV3 ;
	static double WIDTH_TRACK = 14.50;
	static BaseRegulatedMotor leftMotor = Motor.A;
	static BaseRegulatedMotor rightMotor = Motor.D;
	static  BaseRegulatedMotor clamp = Motor.C;
	static Port irPort = SensorPort.S1;
	static Port colorPort = SensorPort.S2;
	
	public LineMap map = new LineMap(new Line[] {}, new Rectangle(0, 0, 1000, 1000));
	public Point dumpPoint = new Point(0, 0);
	boolean clampOpen = false;
	
	public void debugIR() throws InterruptedException {
		while (Button.DOWN.isUp()) {
			LCD.clear();
			LCD.drawInt(this.irAdapter.getObjectDistance(), 4, 4);
			Thread.sleep(100);
		}
	}
	
	public BehaviorRobot15() throws Exception {
		super( new Wheel[] {
				new WheeledChassis.Modeler(leftMotor, WHEEL_DIAMETER ).offset(WIDTH_TRACK / 2),
				new WheeledChassis.Modeler(rightMotor, WHEEL_DIAMETER ).offset(-WIDTH_TRACK / 2)
		}, irPort, colorPort, clamp, WHEEL_DIAMETER, WIDTH_TRACK);
		this.setBehaviorList(this.getBehaviorList());
		this.startArbitrator();
	}
	
	void clampToggle() {
		this.clampOpen = !this.clampOpen;
	}
	
	protected Behavior[] getBehaviorList() {
		return new Behavior[] { 
				new BehaviorBlackZone(this),
				new BehaviorGoTake(this),
				new BehaviorSearch(this),
				new BehaviorGrab(this),
				new BehaviorGoBack(this),
				new BehaviorRelease(this),
		
				//new BehaviorMove(BehaviorRobot15.leftMotor, BehaviorRobot15.rightMotor),
				//new BehaviorAvoid(BehaviorRobot15.leftMotor, BehaviorRobot15.rightMotor, BehaviorRobot15.irAdapter),
				//new BehaviorTouch(BehaviorRobot15.touchSensor),
		};
	}

}
