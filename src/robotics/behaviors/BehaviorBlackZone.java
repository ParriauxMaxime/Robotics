package robotics.behaviors;

import lejos.hardware.lcd.LCD;
import lejos.robotics.Color;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.navigation.Waypoint;
import lejos.robotics.pathfinding.Path;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;
import robotics.AbstractBehaviorRobot;
import lejos.hardware.sensor.EV3ColorSensor;

public class BehaviorBlackZone extends SmartBehavior {
	public BehaviorBlackZone(AbstractBehaviorRobot robot) {
		super(robot);
	}
	
	@Override
	public final boolean takeControl() {
		return this.robot.colorAdapter.getColorID() == Color.BLACK;
	}

	@Override
	public final void  action() {
		super.action();
		/*this.robot.pilot.travel(- 20);
		int angle = (int)Math.round(Math.random() * 10) + 180;
		int reverse  = Math.round(Math.random()) == 0 ? -1 : 1;
		this.robot.pilot.arc(0, reverse * angle);*/
		this.robot.navigator.getPath().add(0, new Waypoint(this.robot.navigator.getPoseProvider().getPose().getX(), 0));
		this.robot.navigator.followPath();
		while (this.robot.navigator.isMoving() && !this.suppressed);
		this.suppress();
	}
}