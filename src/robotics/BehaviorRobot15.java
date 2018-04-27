package robotics;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.*;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
import robotics.behaviors.*;
import lejos.robotics.geometry.*;
import lejos.robotics.mapping.*;
import lejos.hardware.port.*;

public class BehaviorRobot15 extends AbstractBehaviorRobot {
	static final BaseRegulatedMotor leftMotor = Motor.A;
	static final BaseRegulatedMotor rightMotor = Motor.D;
	static  final BaseRegulatedMotor clamp = Motor.C;
	static final Port irPort = SensorPort.S1;
	static final Port colorPort = SensorPort.S2;
	
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
		super(new BaseRegulatedMotor[] {
				leftMotor,
				rightMotor,
		}, irPort, colorPort, clamp);
		this.setBehaviorList(this.getBehaviorList());
		this.arbitrator = new Arbitrator((Behavior [])(this.getBehaviorList()));
	}

	
	protected SmartBehavior[] getBehaviorList() {
		return new SmartBehavior[] { 
				new BehaviorGoBack(this),
				new BehaviorRelease(this),
				new BehaviorSearch(this),
				new BehaviorGoTake(this),
				new BehaviorGrab(this),
				new BehaviorBlackZone(this),
		};
	}

}
