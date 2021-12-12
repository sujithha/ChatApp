package com.stackroute.activitystream.service;

import java.util.List;

import com.stackroute.activitystream.model.User;

public interface UserService {

	/* You Should not modify this interface.  You have to implement these methods in corresponding Impl class*/
	public boolean save(User user);
	
	public boolean update(User user);
	
	public boolean delete(User user);
	
	public List<User> list();
	
	public boolean validate(String username, String password);
	
	public User get(String username);
	
	public boolean exists(String username);
}
