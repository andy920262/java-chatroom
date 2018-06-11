package client;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;

import common.*;

public class Client {
	private LoginGui loginGui;
	private RoomGui roomGui;
	private User user;
	private Socket socket;
	private ObjectInputStream inputStream;
	private ObjectOutputStream outputStream;
	
	public void login() {
		try {
			user = loginGui.getUser();
		} catch (NullPointerException e) {
			loginGui.setMsg("請填完整資料！");
			return;
		}
		
		try {
			try {
				socket = new Socket(Constants.HOST, Constants.PORT);
			} catch (ConnectException e) {
				loginGui.setMsg("連線失敗");
				return;
			}
			outputStream = new ObjectOutputStream(socket.getOutputStream());
			
			/* Login */
			outputStream.writeObject(new Packet(Packet.Type.LOGIN, user));
			
			loginGui.close();
			roomGui.run();
			
			/* Send Message Event */
			roomGui.registerSendEvent(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						outputStream.writeObject(new Packet(Packet.Type.SEND_MSG, roomGui.getMessage()));
						roomGui.resetMessage();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			});

			/* Receive Message Event */
			inputStream = new ObjectInputStream(socket.getInputStream());
			Thread msgListener = new Thread(new Runnable() {
				public void run() {
					while (true) {
						Packet packet;
						try {
							packet = (Packet) inputStream.readObject();
							if (packet.type == Packet.Type.RECV_MSG) {
								roomGui.setMessage(packet.msg);
							}
						} catch (ClassNotFoundException | IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			});
			msgListener.start();
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
	}
	
	public void run () {
		loginGui = new LoginGui();
		roomGui = new RoomGui();
		loginGui.registerLoginEvent(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				login();
			}
		});
		loginGui.run();	
	}
	
	public static void main(String[] args) {
		Client client = new Client();
		client.run();
	}

}
