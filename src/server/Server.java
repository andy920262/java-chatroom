package server;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import common.*;

/**
 * @author andy920262
 * The ChatRoom Server
 */
public class Server {

	private ServerSocket serverSocket;
	private HashMap<String, Room> rooms = new HashMap<String, Room>(); /* Only one room for test */

	/**
	 * Find a room for new connection
	 * @param a unmatched connection
	 */
	protected void findAndJoin(Connection connection){	
		for (String key: rooms.keySet()) {
			if (rooms.get(key).isEmpty()) {
				rooms.remove(key);
				System.out.println("Room " + key + " Clear.");
			}
		}
		Account friend;
		try {
			friend = (Account) connection.receive();
		} catch (Exception e) {
			return;
		}
		if (friend.getAccount() == null) {
			for(Entry<String, Room> entry : rooms.entrySet()) {
			    Room room = entry.getValue();
				if (room.isWaiting()) {
					room.addConnection(connection);
					return;
				}
			}
			String key = connection.getUser().getAccount();
			rooms.put(key, new Room(key, false));
			rooms.get(key).addConnection(connection);
		} else {
			String key = friend.getAccount() + "@" + connection.getUser().getAccount();
			if (rooms.containsKey(key)) {
				rooms.get(key).addConnection(connection);
			} else {
				key = connection.getUser().getAccount() + "@" + friend.getAccount();
				Room room = new Room(key, true);
				rooms.put(key, room);
				rooms.get(key).addConnection(connection);
			}
		}

	}

	public void run() {
		try {
			serverSocket = new ServerSocket(Properties.getPort());
			System.out.println("Server Start!");
			System.out.println("Listening on port " + Properties.getPort());
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}

		ExecutorService threadExecutor = Executors.newCachedThreadPool();
		while (true) {
			try {
				synchronized (serverSocket) {
					Socket socket = serverSocket.accept();
					threadExecutor.execute(() -> {
						/* Login */
						try {
							System.out.println("New connection from " + socket.getRemoteSocketAddress());
							Connection connection = new Connection(socket);
							Account account = (Account) connection.receive();
							if (account.isReg()) {
								if (DataBase.findUser(account.getAccount()) != null) {
									connection.send("帳號名稱已被使用！");
									return;
								}
								Boolean ret = DataBase.addUser(account);
								System.out.println(account.getAccount() + " registor: "  + (ret ? "Success" : "Failed"));
								connection.send(ret ? "註冊成功！" : "註冊失敗！");
							} else {
								account = DataBase.checkUser(account);
								if (account != null) {
									connection.setUser(account);
									connection.send(DataBase.getFriendList(account));
									findAndJoin(connection);
								} else {
									connection.send(Boolean.FALSE);
									System.out.println(socket.getRemoteSocketAddress() + " login failed.");
								}
							}
						} catch (IOException | ClassNotFoundException e) {
							e.printStackTrace();
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
