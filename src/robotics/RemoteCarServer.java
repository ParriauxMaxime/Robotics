package robotics;

import java.io.*;
import java.net.*;
import lejos.hardware.*;
import lejos.hardware.motor.*;
import lejos.hardware.port.MotorPort;

public class RemoteCarServer extends Thread {

	public static final int port = 7360;
	private Socket client;
	private static boolean looping = true;
	private static ServerSocket server;
	static EV3LargeRegulatedMotor right = new EV3LargeRegulatedMotor(MotorPort.B);
	static EV3LargeRegulatedMotor left =  new EV3LargeRegulatedMotor(MotorPort.C);

	public RemoteCarServer(Socket client) {
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

	public void carAction(int command) {
		switch (command) {
		case RemoteCarClient.BACKWARD:
			RemoteCarServer.right.rotate(-360, true);
			RemoteCarServer.left.rotate(-360);
			break;
		case RemoteCarClient.FORWARD:
			RemoteCarServer.right.rotate(360, true);
			RemoteCarServer.left.rotate(360);
			break;
		case RemoteCarClient.RIGHT:
			RemoteCarServer.right.rotateTo(170);
			break;
		case RemoteCarClient.LEFT:
			RemoteCarServer.left.rotateTo(170);
			break;
		}
	}

	public void run() {
		System.out.println("CLIENT CONNECT");
		try {
			InputStream in = client.getInputStream();
			DataInputStream dIn = new DataInputStream(in);

			while (client != null) {
				int command = dIn.readInt();
				System.out.println("REC:" + command);
				if (command == RemoteCarClient.CLOSE) {
					client.close();
					client = null;
				} else {
					carAction(command);
				}
			}

		} catch (IOException e) {
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
