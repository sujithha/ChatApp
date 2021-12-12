package com.stackroute.activitystream.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.stackroute.activitystream.model.UserTag;

/*
* This class is implementing the JpaRepository interface for UserTag.
* */
public interface UserTagRepository extends JpaRepository<UserTag, String>{

	/*
	* Apart from the standard CRUD methods already available in JPA Repository, based
	* on our requirements, we might need to create few query methods for getting 
	* specific data from the database. Please annotate these methods with @Query 
	* annotation. We can configure the invoked database query by annotating the 
	* query method with the @Query annotation. It supports both JPQL and SQL queries,
	*  and the query that is specified by using the @Query annotation precedes all 
	*  other query generation strategies.
	* */
	/*
	* This method will retrieve an UserTag object from database which is matching
	* the username and tag
	* 
	* Write a query to retrieve UserTag object from database matching with username and tag.
	* */	
	@Query("from UserTag where username = :username and tag = :tag")
	public UserTag getUserTag(@Param("username")String username, @Param("tag")String tag);
	
}