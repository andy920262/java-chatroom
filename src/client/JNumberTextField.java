package client;

import java.awt.event.KeyEvent;

import javax.swing.JTextField;

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