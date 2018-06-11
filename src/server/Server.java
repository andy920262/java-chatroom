package server;
import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import common.*;

public class Server {

	private ServerSocket serverSocket;
	private Room room = new Room(); /* Only one room for test */

	protected Room findAndJoin(Connection connection) {
		room.addConnection(connection);
		return room;
	}

	public void run() {
		try {
			serverSocket = new ServerSocket(Constants.PORT);
			System.out.println("Server Start!");
			System.out.println("Listening on port " + Constants.PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}

		ExecutorService threadExecutor = Executors.newCachedThreadPool();
		while (true) {
			try {
				synchronized (serverSocket) {
					Socket socket  = serverSocket.accept();
					threadExecutor.execute(new Runnable() {
						public void run() {
							/* Login Subroutine*/
							try {
								System.out.println("New connection from " + socket.getRemoteSocketAddress());
								Connection connection = new Connection(socket);
								if (connection.receiveUser()) {
									findAndJoin(connection);
								} else {
									System.out.println(socket.getRemoteSocketAddress() + " login failed.");
								}
							} catch (IOException | ClassNotFoundException e) {
								e.printStackTrace();
							}
						}
					});
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		Server server = new Server();
		server.run();
	}

}
