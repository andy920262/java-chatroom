package client;
import javax.swing.*;
import common.Account;
import java.awt.*;
import java.util.ArrayList;

public class MenuGui {
	private JFrame frame;	
	private DefaultListModel<Account> listModel;
	public JList<Account> friendList;
	public JButton chatBtn; 

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		new MenuGui().run();
	}
	
	public void run() {
		try {
			this.frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	public void close() {
		frame.setVisible(false);
	}
	/**
	 * Create the application.
	 */
	public MenuGui() {
		initialize();
	}
	public void updateFriend(ArrayList<Account> friendList) {
		for (Account u : friendList) {
			listModel.addElement(u);
		}
	}
	public Account getFriend() {
		return friendList.getSelectedValue();
	}
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		listModel = new DefaultListModel<>();
		listModel.addElement(new Account(null, null, "<隨機配對>"));
		friendList = new JList<Account>(listModel);
		chatBtn = new JButton("聊天");
		
		frame.getContentPane().add(new JLabel("好友名單"), BorderLayout.NORTH);
		frame.getContentPane().add(new JScrollPane(friendList), BorderLayout.CENTER);
		frame.getContentPane().add(chatBtn, BorderLayout.SOUTH);
	}

}
