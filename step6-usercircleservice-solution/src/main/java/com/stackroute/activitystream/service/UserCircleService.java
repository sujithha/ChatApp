package com.stackroute.activitystream.service;

import java.util.List;

import com.stackroute.activitystream.model.UserCircle;

public interface UserCircleService {

	/* You Should not modify this interface.  You have to implement these methods in corresponding Impl class*/
	public boolean addUser(String username, String circleName);	
	
	public boolean removeUser(String username, String circleName);
	
	public List<String> getMyCircles(String username);
	
	public UserCircle get(String username, String circleName);
}
