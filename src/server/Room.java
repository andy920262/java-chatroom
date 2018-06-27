package server;

import java.io.IOException;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author andy920262
 * Room object to handle user's message
 */
public class Room {
	private int id;
	private LinkedList<Connection> connectionList = new LinkedList<Connection>();
	private ExecutorService threadExecutor = Executors.newCachedThreadPool();
	private boolean privateRoom;
	
	public Room(String key, boolean privateRoom) {
		this.id = key.hashCode();
		this.privateRoom = privateRoom;
		log("Start.");
	}
	
	public boolean isWaiting() {
		return (!privateRoom) && connectionList.size() < 2;
	}
	public boolean isEmpty() {
		return connectionList.size() == 0;
	}
	/**
	 * Send message to all user in the room.
	 * @param message to send
	 */
	public void broadCast(String msg) {
		for (Connection c : connectionList) {
			try {
				c.send(msg);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void log(String msg) {
		System.out.println("[Room " + id + "] " + msg);
	}
	
	public void addFriend(Connection connection) {
		if (connectionList.size() == 1) {
			broadCast("請等待對方連線！");
		}
		for (Connection c : connectionList) {
			if (c != connection) {
				try {
					if (DataBase.isFriend(connection.getUser(), c.getUser())) {
						connection.send("已經是好友了喔！");
						return;
					}
					DataBase.addFriend(connection.getUser(), c.getUser());
					connection.send("你將 " + c.getUser().getName() + " 登錄為好友了！");
					c.send(connection.getUser().getName() + " 將你加入好友！");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Add new user to this room
	 * @param connection
	 */
	public void addConnection(Connection connection) {
		if (connectionList.size() == 0) {
			try {
				connection.send("正在進行配對，請稍等！");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			connectionList.forEach(c -> {
				try {
					connection.send(c.getUser().getName() + "加入聊天室");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			});
		}

		connectionList.add(connection);
		log(connection.getUser().getName() + " join.");
		broadCast(connection.getUser().getName() + " 加入聊天室");
		threadExecutor.execute(() -> {
			while (true) {
				try {
					String msg = (String) connection.receive();
					if (msg.equals("@ADDFRIEND")) {
						addFriend(connection);
					} else {
						msg = connection.getUser().getName() + "：" + msg;
						broadCast(msg);
					}
					log(msg);
				} catch (IOException e) {
					String msg = connection.getUser().getName() + " 已離開";
					connectionList.remove(connection);
					log(msg);
					broadCast(msg);
					return;
				} catch (ClassNotFoundException | NullPointerException e) {
					e.printStackTrace();
					return;
				}
			}
		});
	}
}