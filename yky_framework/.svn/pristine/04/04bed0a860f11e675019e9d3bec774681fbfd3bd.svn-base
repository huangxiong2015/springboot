package com.framework.springboot.model;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;



public class LoginUser extends User {

	private static final long serialVersionUID = 818028364896944595L;
	private String id;
	
	public LoginUser(String userId, String username, String password,
			Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
		this.id = userId;
	}
	
	public String getId() {
		return id;
	}

	@Override
	public boolean equals(Object rhs) {
		boolean idEqual = false;
		boolean nameEqual = false;
		boolean result;
		if(rhs instanceof LoginUser){
			LoginUser cUser = (LoginUser)rhs;
			//如果id均为null或相等则为id相等
			if((this.getId()==null&&this.getId()==cUser.getId())||
					(this.getId()!=null&&this.getId().equals(this.getId()))){
				idEqual = true;
			}
			//如果name均为null或相等则为name相等
			if((this.getUsername()==null&&this.getId()==cUser.getUsername())||
					(this.getUsername()!=null&&this.getUsername().equals(this.getUsername()))){
				nameEqual = true;
			}
			result = idEqual&&nameEqual;//id和name相同则结果为true，否则结果为false
		}else{
			result = super.equals(rhs);
		}
		return result;
	}

	@Override
	public int hashCode() {
		return (this.getId()+this.getUsername()).hashCode();
	}
	
}