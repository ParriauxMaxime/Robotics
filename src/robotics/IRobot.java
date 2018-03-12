package robotics;

public interface IRobot {
	public int forward(double distance);
	public int turnLeft(int angle, double radiusTurn);
	public int turnRight(int angle, double radiusTurn);
}
