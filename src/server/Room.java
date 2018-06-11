package server;

import java.io.EOFException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Room {
	private int id;
	private LinkedList<Connection> connectionList = new LinkedList<Connection>();
	private ExecutorService threadExecutor = Executors.newCachedThreadPool();

	public void broadCast(String msg) {
		
		for (Connection c : connectionList) {
			try {
				c.sendMessage(msg);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void log(String msg) {
		System.out.println("[Room " + id + "] " + msg);
	}
	
	public void addConnection(Connection connection) {
		connectionList.add(connection);
		log(connection.getUser().getName() + " join.");
		broadCast(connection.getUser().getName() + " 來聊天惹");
		threadExecutor.execute(new Runnable() {
			public void run() {
				while (true) {
					try {
						String msg = connection.receiveMessage();
						msg = connection.getUser().getName() + "：" + msg;
						log(msg);
						broadCast(msg);
					} catch (EOFException e) {
						String msg = connection.getUser().getName() + " 已離開";
						log(msg);
						connectionList.remove(connection);
						return;
					} catch (ClassNotFoundException | IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}
}