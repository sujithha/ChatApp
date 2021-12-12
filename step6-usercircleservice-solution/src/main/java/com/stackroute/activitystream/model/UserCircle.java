package com.stackroute.activitystream.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.springframework.stereotype.Component;
/*
 * The class "UserCircle" will be acting as the data model for the user_circle Table in the database. Please
 * note that this class is annotated with @Entity annotation. Hibernate will scan all package for 
 * any Java objects annotated with the @Entity annotation. If it finds any, then it will begin the 
 * process of looking through that particular Java object to recreate it as a table in your database.
 *
 * Please note that you will have to use @Component annotation on this class if wish
 * to autowire the class from any other components of the application
 */
@Component
@Entity
@Table (name ="userCircle")
public class UserCircle {
	/*
	 * This class should have three fields
	 * (userCircleId,username,circleName). Out of these three fields, the
	 * field userCircleId should be the primary key and should be generated. This class 
	 * should also contain the getters and setters for the fields. 
	 */
	@Id
	@GeneratedValue (strategy = GenerationType.AUTO)
	private int userCircleId;
	private String username;
	private String circleName;
	
	public UserCircle() {
		
	}
	
	public UserCircle(String username, String circleName) {
		super();
		this.username = username;
		this.circleName = circleName;
	}

	public int getUserCircleId() {
		return userCircleId;
	}

	public void setUserCircleId(int userCircleId) {
		this.userCircleId = userCircleId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getCircleName() {
		return circleName;
	}

	public void setCircleName(String circleName) {
		this.circleName = circleName;
	}
	
	
	
}