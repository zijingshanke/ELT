package com.chinarewards.gwt.elt.client.staff.model;

import java.io.Serializable;

public class StaffVo implements Serializable, Comparable<StaffVo> {
	private static final long serialVersionUID = 1L;

	private String id;
	private String name;
	private String username;
	private String password;
	private String tell;
	private String email;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getTell() {
		return tell;
	}

	public void setTell(String tell) {
		this.tell = tell;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public int compareTo(StaffVo o) {
		return null == o ? -1 : o.getId().compareTo(id);
	}

	@Override
	public String toString() {
		return "UserVo [id=" + id + ", name=" + name + "]";
	}

}
