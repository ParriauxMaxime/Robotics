package robotics;

import lejos.hardware.motor.*;
import lejos.robotics.subsumption.Behavior;
import lejos.hardware.port.*;

public class BehaviorRobot15 extends AbstractBehaviorRobot {
	static double WHEEL_DIAMETER = 0.038;
	static double WIDTH_TRACK = 0.1418;
	protected int DEFAULT_SPEED = 600;
	static BaseRegulatedMotor leftMotor = Motor.B;
	static BaseRegulatedMotor rightMotor = Motor.C;
	static InfraredAdapter irAdapter = new InfraredAdapter(); 
	static Port touchSensor = SensorPort.S1; 
	public BehaviorRobot15() {
		super(BehaviorRobot15.leftMotor, 
				BehaviorRobot15.rightMotor, 
				BehaviorRobot15.WIDTH_TRACK, 
				BehaviorRobot15.WHEEL_DIAMETER,
				BehaviorRobot15.getBehaviorList());
		this.leftWheel.setSpeed(DEFAULT_SPEED);
		this.rightWheel.setSpeed(DEFAULT_SPEED);
		// TODO Auto-generated constructor stub
	}
	
	static protected Behavior[] getBehaviorList() {
		return new Behavior[] { 
				new BehaviorMove(BehaviorRobot15.leftMotor, BehaviorRobot15.rightMotor),
				new BehaviorAvoid(BehaviorRobot15.leftMotor, BehaviorRobot15.rightMotor, BehaviorRobot15.irAdapter),
				new BehaviorTouch(BehaviorRobot15.touchSensor),
		};
	}

}
