package robotics;

import java.io.*;
import java.net.*;
import lejos.hardware.*;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.*;
import lejos.hardware.port.MotorPort;

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

	public RemoteCarServer(Socket client) {
		try {
			robot = new BehaviorRobot15();
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.client = client;
		Button.ESCAPE.addKeyListener(new EscapeListener());
	}

	public static void main(String[] args) throws IOException {
		server = new ServerSocket(port);
		while (looping) {
			System.out.println("Awaiting client..");
			new RemoteCarServer(server.accept()).start();
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
		case RemoteCarClient.START:
			robot.startArbitrator();
			break;
		case RemoteCarClient.STOP:
			robot.stopArbitrator();
			break;
		}
	}
	
	public void sendData() {
		try {
			dOut.writeInt(0);
			dOut.writeInt(robot.irAdapter.getObjectDistance());
			dOut.writeInt(robot.colorSensor.getColorID());
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
