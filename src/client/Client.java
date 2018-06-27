package client;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

import common.*;

public class Client {
	private LoginGui loginGui;
	private RoomGui roomGui;
	private MenuGui menuGui;
	private Socket socket;
	private ObjectInputStream inputStream;
	private ObjectOutputStream outputStream;

	public void login() {
		Account account;
		try {
			account = loginGui.getLoginAccount();
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
			inputStream = new ObjectInputStream(socket.getInputStream());

			/* Login */
			outputStream.writeObject(account);
			try {
				Object friendList = inputStream.readObject();
				if (!(friendList instanceof ArrayList)) {
					loginGui.setMsg("登入失敗！");
					return;
				}
				menuGui = new MenuGui();
				menuGui.updateFriend((ArrayList<Account>) friendList);
			} catch (ClassNotFoundException e2) {
				e2.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		loginGui.close();
		menuGui.chatBtn.addActionListener(l -> chat());
		menuGui.run();
	}

	private void chat() {	
		try {
			Account friend = menuGui.getFriend();
			if (friend == null) {
				return;
			}
			outputStream.writeObject(friend);
		} catch (IOException e2) {
			return;
		}
		menuGui.close();
		roomGui = new RoomGui();
		roomGui.run();

		/* Send Message Event */
		roomGui.textField.addActionListener(l -> {
			try {
				outputStream.writeObject(roomGui.getMessage());
				roomGui.resetMessage();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
		
		/* Add Friend Event */
		roomGui.friendBtn.addActionListener(l -> {
			try {
				outputStream.writeObject("@ADDFRIEND");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});

		/* Receive Message Event */
		(new Thread(() -> {
			while (true) {
				try {
					String msg = (String) inputStream.readObject();
					roomGui.setMessage(msg);
				} catch (ClassNotFoundException | IOException e) {
					e.printStackTrace();
				}
			}
		})).start();

	}

	public void register() {
		Socket regSocket;
		Account account;
		try {
			account = loginGui.getRegisterAccount();
		} catch (NullPointerException e) {
			loginGui.setMsg("請填完整資料！");
			return;
		}
		try {
			try {
				regSocket = new Socket(Constants.HOST, Constants.PORT);
			} catch (ConnectException e) {
				loginGui.setMsg("無法連接伺服器");
				return;
			}
			outputStream = new ObjectOutputStream(regSocket.getOutputStream());
			inputStream = new ObjectInputStream(regSocket.getInputStream());

			/* Register */
			outputStream.writeObject(account);
			try {
				Boolean ret = (Boolean) inputStream.readObject();
				if (!ret) {
					loginGui.setMsg("註冊失敗！");
					return;
				}
				loginGui.setMsg("註冊成功！");
			} catch (ClassNotFoundException e2) {
				e2.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public void run() {
		loginGui = new LoginGui();
		loginGui.loginBtn.addActionListener(l -> login());
		loginGui.registerBtn.addActionListener(l -> register());
		loginGui.run();
	}

	public static void main(String[] args) {
		Client client = new Client();
		client.run();
	}

}
