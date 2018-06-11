package client;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import javax.swing.JScrollPane;

public class RoomGui implements Runnable{

	private JFrame frame;
	private JTextField textField;
	private JTextArea textArea;
	private JButton sendBtn;
	private JScrollPane scrollPane;
	
	public void registerSendEvent(ActionListener actionListener) {
		this.sendBtn.addActionListener(actionListener);
		this.textField.addActionListener(actionListener);
	}
	
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

		sendBtn = new JButton("Send");
		panel.add(sendBtn, BorderLayout.EAST);

		JPanel panel_1 = new JPanel();
		frame.getContentPane().add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		panel_1.add(textArea);
		
		scrollPane = new JScrollPane(textArea);
		panel_1.add(scrollPane, BorderLayout.NORTH);
		
		frame.getContentPane().add(scrollPane);
	}

}
