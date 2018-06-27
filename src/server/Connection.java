package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import common.Account;

/**
 * 
 * @author andy920262
 * A class for handling user connection in server.
 */
public class Connection {
	private Account user;
	private ObjectInputStream inputStream;
	private ObjectOutputStream outputStream;

	public Connection(Socket socket) throws IOException {
		inputStream = new ObjectInputStream(socket.getInputStream());
		outputStream = new ObjectOutputStream(socket.getOutputStream());
	}
	
	public Account getUser() {
		return user;
	}
	public void setUser(Account user) {
		this.user = user;
	}
	
	public Object receive() throws ClassNotFoundException, IOException {
		return inputStream.readObject();
	}
	public void send(Object obj) throws IOException {
		outputStream.writeObject(obj);
	}
}