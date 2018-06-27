package common;

import java.io.Serializable;

public class Account implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String account;
	private String password;
	private boolean reg;
	private String name;
	
	public String getName() {
		return name;
	}
	public boolean isReg() {
		return reg;
	}
	public String getAccount() {
		return account;
	}
	public String getPassword() {
		return password;
	}
	public Account(String account, String password) {
		this.account = account;
		this.password = password;
		this.reg = false;
	}
	
	public Account(String account, String password, String name) {
		this.name = name;
		this.account = account;
		this.password = password;
		this.reg = true;
	}
	public String toString() {
		if (account == null) {
			return name;
		}
		return this.name + "(" + this.account + ")";
	}
}
