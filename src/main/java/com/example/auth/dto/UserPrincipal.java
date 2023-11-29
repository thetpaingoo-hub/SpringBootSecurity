package com.example.auth.dto;

import java.util.List;

public class UserPrincipal {
	private String syskey;
	private String name;
	private List<String> roles;
	
	public List<String> getRoles() {
		return roles;
	}
	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
	
	public String getSyskey() {
		return syskey;
	}
	public void setSyskey(String syskey) {
		this.syskey = syskey;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}
