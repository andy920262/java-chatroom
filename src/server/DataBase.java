package server;

import common.Account;
import java.sql.*;
import java.sql.Connection;
import java.util.ArrayList;

public class DataBase {
	static private Connection c;
	static {
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:user.db");
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
	}

	public static boolean addUser(Account account) {
		String query = "insert into user (account,password,name) values(?,?,?)";
		PreparedStatement pst = null;
		try {
			pst = c.prepareStatement(query);
			int idx = 1;
			pst.setString(idx++, account.getAccount());
			pst.setString(idx++, account.getPassword());
			pst.setString(idx++, account.getName());
			pst.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	public static Account findUser(String account) {
		String query = "select * from user";
        Statement stat = null;
        ResultSet rs = null;
        try {
        	stat = c.createStatement();
        	rs = stat.executeQuery(query);
        	while(rs.next()) {
        		if (account.equals(rs.getString("account"))) {
        			return new Account(rs.getString("account"), null, rs.getString("name"));
        		}
            }
        } catch (SQLException e) {
        	e.printStackTrace();
        }
		return null;
	}
	
	public static Account checkUser(Account account) {
		String query = "select * from user";
        Statement stat = null;
        ResultSet rs = null;
        try {
        	stat = c.createStatement();
        	rs = stat.executeQuery(query);
        	while(rs.next()) {
        		if (account.getAccount().equals(rs.getString("account")) && account.getPassword().equals(rs.getString("password"))) {
        			return new Account(rs.getString("account"), rs.getString("password"), rs.getString("name"));
        		}
            }
        } catch (SQLException e) {
        	e.printStackTrace();
        }
		return null;
	}

	public static void addFriend(Account user, Account friend) {
		String query = "insert into friend (user1,user2) values(?,?)";
		PreparedStatement pst = null;
		try {
			pst = c.prepareStatement(query);
			int idx = 1;
			pst.setString(idx++, user.getAccount());
			pst.setString(idx++, friend.getAccount());
			pst.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static boolean isFriend(Account user1, Account user2) {
		String query = "select * from friend";
        Statement stat = null;
        ResultSet rs = null;
        try {
        	stat = c.createStatement();
        	rs = stat.executeQuery(query);
        	while(rs.next()) {
        		if (rs.getString("user1").equals(user1.getAccount()) &&
        			rs.getString("user2").equals(user2.getAccount())) {
        			return true;
        		}
            }
        } catch (SQLException e) {
        	e.printStackTrace();
        }
        return false;
	}
	public static ArrayList<Account> getFriendList(Account user) {
		String query = "select * from friend";
		ArrayList<Account> friendList = new ArrayList<Account>(); 
        Statement stat = null;
        ResultSet rs = null;
        try {
        	stat = c.createStatement();
        	rs = stat.executeQuery(query);
        	while(rs.next()) {
        		if (rs.getString("user1").equals(user.getAccount())) {
        			friendList.add(findUser(rs.getString("user2")));
        		}
            }
        } catch (SQLException e) {
        	e.printStackTrace();
        }
		return friendList;
	}

	static public void main(String[] argv) {
		ArrayList<Account> a = getFriendList(new Account("777", "777", "777"));
		a.forEach(e->System.out.println(e));
	}
}
