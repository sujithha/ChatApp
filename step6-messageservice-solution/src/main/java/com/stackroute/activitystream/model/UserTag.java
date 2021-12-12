package com.stackroute.activitystream.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.stereotype.Component;

/*
 * The class "UserTag" will be acting as the data model for the user_tag Table in the database. Please
 * note that this class is annotated with @Entity annotation. Hibernate will scan all package for 
 * any Java objects annotated with the @Entity annotation. If it finds any, then it will begin the 
 * process of looking through that particular Java object to recreate it as a table in your database.
 *
 * Please note that you will have to use @Component annotation on this class if wish
 * to autowire the class from any other components of the application
 */
@Component
@Entity
@Table (name = "usertag")
public class UserTag {
	
	/*
	 * This class should have three fields
	 * (userTagId,username,tag). Out of these three fields, the
	 * field userTagId should be the primary key and should be generated. This class 
	 * should also contain the getters and setters for the fields. 
	 */
	@Id
	@GeneratedValue (strategy = GenerationType.AUTO)
	private long userTagId;
	private String username;
	private String tag;
	
	public long getUserTagId() {
		return userTagId;
	}
	public void setUserTagId(long userTagId) {
		this.userTagId = userTagId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	
	
}