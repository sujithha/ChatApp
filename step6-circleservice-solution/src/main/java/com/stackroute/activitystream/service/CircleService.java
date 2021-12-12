package com.stackroute.activitystream.service;

import java.util.List;

import com.stackroute.activitystream.model.Circle;

public interface CircleService {
	/* You Should not modify this interface.  You have to implement these methods in corresponding Impl class*/
	public boolean save(Circle circle);
	
	public List<Circle> getAllCircles();
	
	public List<Circle> getAllCircles(String searchString);
	
	public Circle get(String circleName);
	
	public boolean delete(Circle circle);
	
}
