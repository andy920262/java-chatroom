package common;

import java.io.Serializable;

public class Message implements Serializable {
    private static final long serialVersionUID = 5636117929331512268L;
    private String name;
    private String account;
    private String msg;

    public Message(Account account, String msg) {
        this.name = account.getName();
        this.account = account.getAccount();
        this.msg = msg;
    }


    public String getName() {
        return name;
    }
    public String getAccount() {
        return account;
    }
    public String getMsg() {
        return msg;
    }

}