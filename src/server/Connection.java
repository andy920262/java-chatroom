package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import common.Packet;
import common.User;


public class Connection{
	//private Socket socket;
	private User user;
	private ObjectInputStream inputStream;
	private ObjectOutputStream outputStream;

	public Connection(Socket socket) throws IOException {
		//this.socket = socket;
		inputStream = new ObjectInputStream(socket.getInputStream());
		outputStream = new ObjectOutputStream(socket.getOutputStream());
	}
	
	public User getUser() {
		return user;
	}
	
	public boolean receiveUser() throws ClassNotFoundException, IOException {
		Packet packet = receive();
		if (packet.type == Packet.Type.LOGIN) {
			user = packet.user;
			return true;
		} else {
			return false;
		}
	}
	
	public String receiveMessage() throws ClassNotFoundException, IOException {
		Packet packet = receive();
		if (packet.type == Packet.Type.SEND_MSG) {
			return packet.msg;
		} else {
			return null;
		}
	}
	
	private Packet receive() throws ClassNotFoundException, IOException {
		return (Packet) inputStream.readObject();
	}
	
	public void sendMessage(String msg) throws IOException {
		Packet packet = new Packet(Packet.Type.RECV_MSG, msg);
		send(packet);
	}
	
	private void send(Packet packet) throws IOException {
		outputStream.writeObject(packet);
	}
}