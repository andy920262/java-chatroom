package client;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.awt.event.*;

import common.*;


/**
 * @author andy920262
 * The ChatRoom Client
 */
public class Client {
	private LoginGui loginGui;
	private RoomGui roomGui;
	private MenuGui menuGui;
	private Socket socket;
	private ObjectInputStream inputStream;
	private ObjectOutputStream outputStream;
	/**
	 * Login event.
	 */
	@SuppressWarnings("unchecked")
	public void login() {
		Account account;
		try {
			account = loginGui.getLoginAccount();
			if (account.getAccount().equals("") || account.getPassword().equals("")) {
				throw new NullPointerException();
			}
		} catch (NullPointerException e) {
			loginGui.setMsg("請填完整資料！");
			return;
		}
		try {
			try {
				socket = new Socket(Properties.getHost(), Properties.getPort());
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
					loginGui.setMsg("帳號或密碼錯誤！");
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
	/**
	 * Start chatting event.
	 */
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
		roomGui.frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				login();
				roomGui.close();
			}
		});
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
	/**
	 * Register new account event.
	 */
	public void register() {
		Socket regSocket;
		Account account;
		try {
			account = loginGui.getRegisterAccount();
			if (account.getAccount().equals("") || account.getPassword().equals("")) {
				throw new NullPointerException();
			}
		} catch (NullPointerException e) {
			loginGui.setMsg("請填完整資料！");
			return;
		}
		try {
			try {
				regSocket = new Socket(Properties.getHost(), Properties.getPort());
			} catch (ConnectException e) {
				loginGui.setMsg("無法連接伺服器");
				return;
			}
			outputStream = new ObjectOutputStream(regSocket.getOutputStream());
			inputStream = new ObjectInputStream(regSocket.getInputStream());

			/* Register */
			outputStream.writeObject(account);
			try {
				Object ret = inputStream.readObject();
				if (ret instanceof String) {
					loginGui.setMsg((String) ret);
					return;
				}
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
