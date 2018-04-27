package robotics.behaviors;

import lejos.hardware.Sound;
import lejos.robotics.Color;
import lejos.robotics.geometry.*;
import robotics.AbstractBehaviorRobot;

public class BehaviorSearch extends SmartBehavior  {
	public Point zone;
	static final int MAX_DISTANCE = 45;

	public BehaviorSearch(AbstractBehaviorRobot robot) {
		super(robot);
	}

	@Override
	public final boolean takeControl() {
		return this.robot.isObjectGrabbed() == false && this.robot.irAdapter.getObjectDistance() > MAX_DISTANCE  && !this.robot.pilot.isMoving(); 
	}
	
	 void randomMove() {
		this.robot.pilot.rotate(Math.round(Math.random() * 3) * (Math.round(Math.random()) == 1 ? 1 : -1) * 90, true);
		this.robot.pilot.travel(50, true);
	}
	 
	 @Override
	public final void action() {
		 super.action();
		this.robot.setObjectFound(false);
		int angle = 6;
		int step = 360 / angle;
		for (int i = 0; i < step; i++) {
			this.robot.pilot.rotate(angle);
			if (this.robot.irAdapter.getObjectDistance() <= MAX_DISTANCE) {
				Sound.playTone(400, 50);
				this.robot.setObjectFound(true);
				break;
			}
		}
		if (!this.robot.getObjectFound())
			this.randomMove();
		this.suppress();
	}
	 
	 @Override
	 public final void suppress() {
		 super.suppress();
		 this.robot.pilot.stop();
	 }
}
