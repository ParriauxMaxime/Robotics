package robotics;

import java.io.*;
import java.net.*;
import lejos.hardware.*;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.*;
import lejos.hardware.port.MotorPort;
import lejos.robotics.geometry.Point;
import lejos.robotics.navigation.Waypoint;
import lejos.robotics.pathfinding.Path;

public class RemoteCarServer extends Thread {

	public static final int port = 7360;
	private Socket client;
	private static boolean looping = true;
	private static ServerSocket server;
	private static AbstractBehaviorRobot robot;
	private static InputStream in;
	private static DataInputStream dIn;
	private static OutputStream out;
	private static DataOutputStream dOut;
	private Thread threadArbitrator;

	public RemoteCarServer(Socket client, AbstractBehaviorRobot r) {
		try {
			robot = r;
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.client = client;
		Button.ESCAPE.addKeyListener(new EscapeListener());
	}

	public static void main(String[] args) throws Exception {
		server = new ServerSocket(port);
		while (looping) {
			System.out.println("Awaiting client..");
			new RemoteCarServer(server.accept(), new BehaviorRobot15()).start();
		}
	}

	public void carAction(int command) throws Exception {
		switch (command) {
		case RemoteCarClient.CALIBRATE_CIRCLE:
			robot.pilot.arc(0, 360);
			break;
		case RemoteCarClient.CALIBRATE_DISTANCE:
			robot.pilot.travel(100);
			break;
		case RemoteCarClient.SEND_CALIBRATION:
			double trackWidth = dIn.readDouble();
			double wheelDiameter = dIn.readDouble();
			robot.setWheelDiameter(wheelDiameter);
			robot.setWidthTrack(trackWidth);
			LCD.clear();
			LCD.drawString(Double.toString(robot.getWheelDiameter()), 6, 4);
			break;
		case RemoteCarClient.NOTIFY:
			sendData();
			break;
		case RemoteCarClient.DEBUG: 
			Path zone = new Path();
			Point p = robot.navigator.getPoseProvider().getPose().getLocation();
			zone.add(new Waypoint(p.getX(), p.getY()));
			zone.add(new Waypoint (20, -20));
			 zone.add(new Waypoint (60, -20));
			 zone.add(new Waypoint (60, 20));
			 zone.add(new Waypoint (20, 20));
			 zone.add(new Waypoint (0, 0));
			 robot.navigator.setPath(zone);
			 robot.navigator.followPath();
			break;
		case RemoteCarClient.START:
			threadArbitrator = new Thread() {
				@Override
				public void run() {
					robot.startArbitrator();					
					}
				};
				threadArbitrator.setDaemon(true);
				threadArbitrator.start();
			break;
		case RemoteCarClient.STOP:
			threadArbitrator.interrupt();
			robot.stopArbitrator();
			break;
		}
	}
	
	public void sendData() {
		try {
			dOut.writeInt(0);
			dOut.writeInt(robot.irAdapter.getObjectDistance());
			dOut.writeInt(robot.colorAdapter.getColorID());
			dOut.writeUTF(robot.getCurrentBehavior());
			} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		System.out.println("CLIENT CONNECT");
		try {
			 in = client.getInputStream();
			dIn = new DataInputStream(in);
			out = client.getOutputStream();
			dOut = new DataOutputStream(out);

			while (client != null) {
				int command = dIn.readInt();
				if (command != 1) {
					System.out.println("REC:" + command);	
				}
				if (command == RemoteCarClient.CLOSE) {
					client.close();
					client = null;
				} else {
					carAction(command);
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} 

	}

	private class EscapeListener implements KeyListener {
		public void keyPressed(Key k) {
			looping = false;
			System.exit(0);
		}

		public void keyReleased(Key k) {
		}
	}
}
