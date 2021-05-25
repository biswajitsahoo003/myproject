package com.tcl.dias.common.beans;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import com.tcl.dias.common.redis.beans.CustomerDetail;

/**
 * 
 * @author Manojkumar R
 *
 */
public class CustomUserData implements UserDetails {

	private static final long serialVersionUID = 1L;

	private Collection<? extends GrantedAuthority> list = null;
	String userName = null;
	String password = null;
	boolean status = false;
	User user = null;
	CustomerDetail customerDetail;

	public CustomUserData() {
		list = new ArrayList<>();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.list;
	}

	public void setAuthorities(Collection<? extends GrantedAuthority> roles) {
		this.list = roles;
	}

	public void setAuthentication(boolean status) {
		this.status = status;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String pass) {
		this.password = pass;
	}

	@Override
	public String getUsername() {
		return this.userName;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public CustomerDetail getCustomerDetail() {
		return customerDetail;
	}

	public void setCustomerDetail(CustomerDetail customerDetail) {
		this.customerDetail = customerDetail;
	}

}
