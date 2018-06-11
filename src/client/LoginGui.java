package client;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JLabel;
import java.awt.FlowLayout;
import javax.swing.JTextField;

import common.User;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;


class JNumberTextField extends JTextField {
    private static final long serialVersionUID = 1L;

    @Override
    public void processKeyEvent(KeyEvent ev) {
        if (Character.isDigit(ev.getKeyChar()) || ev.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            super.processKeyEvent(ev);
        }
        ev.consume();
        return;
    }

    /**
     * As the user is not even able to enter a dot ("."), only integers (whole numbers) may be entered.
     */
    public Integer getNumber() {
        Integer result = null;
        String text = getText();
        if (text != null && !"".equals(text)) {
        	
            result = Integer.valueOf(text);
        }
        return result;
    }
}

public class LoginGui implements Runnable {

	private JFrame frmv;
	private JTextField nameText;
	private JButton loginBtn;
	@SuppressWarnings("rawtypes")
	private JComboBox regionText;
	@SuppressWarnings("rawtypes")
	private JComboBox genderText;
	private JNumberTextField ageText;
	private JLabel msg;

	public void setMsg(String text) {
		this.msg.setText(text);
	}
	
	public User getUser() {
		return new User(nameText.getText(), ageText.getNumber(), (String) genderText.getSelectedItem(), (String) regionText.getSelectedItem());
	}
	
	public void close() {
		frmv.setVisible(false);
	}
	
	public void launch() {
		EventQueue.invokeLater(this);
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
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void initialize() {
		frmv = new JFrame();
		frmv.setTitle("聊天室 V0.87");
		frmv.setBounds(100, 100, 313, 215);
		frmv.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmv.getContentPane().setLayout(new BorderLayout(0, 0));
		
		loginBtn = new JButton("登入");

		frmv.getContentPane().add(loginBtn, BorderLayout.SOUTH);
		
		JPanel panel_1 = new JPanel();
		frmv.getContentPane().add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_2 = new JPanel();
		panel_1.add(panel_2, BorderLayout.CENTER);
		panel_2.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JLabel lblNewLabel_1 = new JLabel("性別");
		panel_2.add(lblNewLabel_1);
		
		genderText = new JComboBox();
		panel_2.add(genderText);
		genderText.setModel(new DefaultComboBoxModel(new String[] {"男", "女"}));
		
		JLabel lblNewLabel_2 = new JLabel("年齡");
		panel_2.add(lblNewLabel_2);
		
		ageText = new JNumberTextField();
		panel_2.add(ageText);
		ageText.setColumns(3);
		
		JLabel lblNewLabel_3 = new JLabel("地區");
		panel_2.add(lblNewLabel_3);
		
		regionText = new JComboBox();
		panel_2.add(regionText);
		regionText.setModel(new DefaultComboBoxModel(new String[] {"台北", "台中", "台南"}));
		
		JPanel panel_3 = new JPanel();
		panel_1.add(panel_3, BorderLayout.NORTH);
		
		JLabel lblNewLabel = new JLabel("你的名字");
		panel_3.add(lblNewLabel);
		
		nameText = new JTextField();
		panel_3.add(nameText);
		nameText.setColumns(8);
		
		msg = new JLabel("");
		panel_1.add(msg, BorderLayout.SOUTH);
	}
}
