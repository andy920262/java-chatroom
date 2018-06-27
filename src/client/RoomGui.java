package client;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

public class RoomGui implements Runnable{

	private JFrame frame;
	public JTextField textField;
	private JTextArea textArea;
	public JButton friendBtn;
	
	public String getMessage() {
		return textField.getText();
	}
	
	public void resetMessage() {
		textField.setText("");
	}
	
	public void setMessage(String msg) {
		textArea.append(msg + "\n");
	}
	
	
	/**
	 * Launch the application.
	 */
	public void launch() {
		EventQueue.invokeLater(this);
	}
	
	public void run() {
		try {
			this.frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * Create the application.
	 */
	public RoomGui() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("聊天室滑起來");
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.SOUTH);
		panel.setLayout(new BorderLayout(0, 0));

		textField = new JTextField();
		panel.add(textField, BorderLayout.CENTER);

		friendBtn = new JButton("加好友");
		panel.add(friendBtn, BorderLayout.EAST);

		JPanel panel_1 = new JPanel();
		panel_1.setLayout(new BorderLayout(0, 0));
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		panel_1.add(textArea);
		
		frame.getContentPane().add(new JScrollPane(textArea));
	}
	static public void main(String[] argv) {
		new RoomGui().run();
	}

}
