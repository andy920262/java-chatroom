package common;

import java.io.Serializable;

public class Packet implements Serializable{

	public enum Type {
		LOGIN,
		SEND_MSG,
		RECV_MSG
	}
	public Packet (Type type, String msg) {
		this.type = type;
		this.msg = msg;
	}
	
	public Packet (Type type, User user) {
		this.type = type;
		this.user = user;
	}
	
	public Type type;
	public String msg;
	public User user;
	
}
