package common;

import java.io.Serializable;

public class User implements Serializable{
	private String name;
	private int age;
	private String gender;
	private String region;
	
	public String getName() {
		return name;
	}
	public User(String name, int age, String gender, String region) {
		this.name = name;
		this.age = age;
		this.gender = gender;
		this.region = region;
	}

}
