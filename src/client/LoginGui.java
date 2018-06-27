package client;

import java.awt.*;
import javax.swing.*;


import common.Account;

import java.awt.event.*;

public class LoginGui implements Runnable {

	private JFrame frmv;
	public JButton loginBtn;
	public JButton registerBtn;
	private JLabel msg;
	private JTabbedPane tab;
	
	private JTextField loginAccount;
	private JPasswordField loginPassword;

	private JTextField registerAccount;
	private JPasswordField registerPassword;
	private JTextField registerName;
	
	public void setMsg(String text) {
		JOptionPane.showMessageDialog(frmv, text);
		//this.msg.setText(text);
	}
	
	public Account getLoginAccount() {
		return new Account(loginAccount.getText(), new String(loginPassword.getPassword()));
	}
	public Account getRegisterAccount() {
		return new Account(registerAccount.getText(), new String(registerPassword.getPassword()), registerName.getText());
	}	
	public void close() {
		frmv.setVisible(false);
	}
	
	public void run() {
		try {
			this.frmv.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}

	/**
	 * Create the application.
	 */
	public LoginGui() {
		initialize();
	}

	public void registerLoginEvent(ActionListener actionListener) {
		this.loginBtn.addActionListener(actionListener);
	}
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmv = new JFrame();
		frmv.setTitle("聊天室 V0.87");
		frmv.setBounds(0, 0, 300, 300);
		frmv.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmv.getContentPane().setLayout(new BorderLayout(0, 0));
		
		tab = new JTabbedPane();
		frmv.getContentPane().add(tab);
		

		JPanel loginPanel = new JPanel();	
		loginPanel.setLayout(new FlowLayout());
		
		loginBtn = new JButton("登入");
		registerBtn = new JButton("註冊");
		
		JPanel accountPanel = new JPanel();
		accountPanel.setLayout(new FlowLayout());
		loginAccount = new JTextField();
		loginAccount.setColumns(10);
		accountPanel.add(new JLabel("帳號"));
		accountPanel.add(loginAccount);
		JPanel passwordPanel = new JPanel();
		passwordPanel.setLayout(new FlowLayout());
		loginPassword = new JPasswordField();
		loginPassword.setColumns(10);
		passwordPanel.add(new JLabel("密碼"));
		passwordPanel.add(loginPassword);
		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new GridLayout(2, 1));
		inputPanel.add(accountPanel);
		inputPanel.add(passwordPanel);
		
		loginPanel.add(inputPanel);
		loginPanel.add(loginBtn);
		
		tab.addTab("登入", loginPanel);
		
		JPanel registerPanel = new JPanel();
		
		accountPanel = new JPanel();
		accountPanel.setLayout(new FlowLayout());
		registerAccount = new JTextField();
		registerAccount.setColumns(10);
		accountPanel.add(new JLabel("帳號"));
		accountPanel.add(registerAccount);
		
		passwordPanel = new JPanel();
		passwordPanel.setLayout(new FlowLayout());
		registerPassword = new JPasswordField();
		registerPassword.setColumns(10);
		passwordPanel.add(new JLabel("密碼"));
		passwordPanel.add(registerPassword);
		
		JPanel namePanel = new JPanel();
		namePanel.setLayout(new FlowLayout());
		registerName = new JTextField();
		registerName.setColumns(10);
		namePanel.add(new JLabel("暱稱"));
		namePanel.add(registerName);
		
		registerPanel.setLayout(new GridLayout(4, 1));
		registerPanel.add(accountPanel);
		registerPanel.add(passwordPanel);
		registerPanel.add(namePanel);
		registerPanel.add(registerBtn);
		
		tab.addTab("註冊", registerPanel);
		
		msg = new JLabel();
		frmv.getContentPane().add(msg, BorderLayout.SOUTH);
		
	}
	public static void main(String[] argv) {
		LoginGui g = new LoginGui();
		g.run();
	}
}
