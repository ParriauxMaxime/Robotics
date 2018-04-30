package robotics.behaviors;

import lejos.hardware.Sound;
import lejos.robotics.Color;
import lejos.robotics.geometry.*;
import lejos.robotics.navigation.Waypoint;
import lejos.robotics.pathfinding.Path;
import robotics.AbstractBehaviorRobot;

public class BehaviorSearch extends SmartBehavior {
	public Path zone = new Path();
	public int currentZone = -1;

	public BehaviorSearch(AbstractBehaviorRobot robot) {
		super(robot);
		this.prepareZone();
		this.robot.navigator.setPath(this.zone);
		this.robot.navigator.singleStep(true);
	}

	void prepareZone() {
		this.zone.add(new Waypoint (20, -15));
		this.zone.add(new Waypoint (50, -15));
		this.zone.add(new Waypoint (50, 15));
		this.zone.add(new Waypoint (20, 15));
		this.zone.add(new Waypoint (0, 0));
	}
	@Override
	public final boolean takeControl() {
		return true;
	}
		
	@Override
	public final void action() {
		super.action();
		if (this.robot.navigator.getPath().isEmpty()) {
			this.prepareZone();
			System.out.println("RECREATE PATH");
			this.robot.navigator.setPath(this.zone);
			this.robot.navigator.singleStep(true);
		}
		// RANDOM MOVE
		this.robot.setCurrentBehavior(this.getClass().getName()+ "-Step-" + this.robot.navigator.getWaypoint().getX() + "-" + this.robot.navigator.getWaypoint().getY() );
		this.robot.navigator.followPath();
		while (this.robot.navigator.isMoving() && !this.suppressed);
		this.robot.pilot.rotate(360, true);
		while (this.robot.pilot.isMoving() && !this.suppressed);
	}
}