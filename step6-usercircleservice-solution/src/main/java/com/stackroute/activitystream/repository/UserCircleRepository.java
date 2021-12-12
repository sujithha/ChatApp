package com.stackroute.activitystream.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.stackroute.activitystream.model.UserCircle;

/*
* This class is implementing the JpaRepository interface for UserCircle.
* */


public interface UserCircleRepository extends CrudRepository<UserCircle, Integer>{
	
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
	* This method will retrieve an UserCircle object from database which is matching
	* the username and circleName
	* 
	* Write a query to get user circle object object from Database useing username and circlename.
	* 
	* */
	@Query("from UserCircle where username = :username and circleName =:circleName")
	UserCircle getUsernameAndCircleName(@Param("username")String username, @Param("circleName")String circleName);
	
	/*
	* This method will retrieve an circleName from UserCircle table which is matching
	* the username in the method parameter. This method will help us to find out all
	* subscribed circles by a specific user
	* 
	* Write a query to retrive circle name from usercircle matching with username
	* */
	@Query("select circleName from UserCircle where username = :username")
	List<String> findCircleNameByUserName(@Param("username")String username);
	

	

}