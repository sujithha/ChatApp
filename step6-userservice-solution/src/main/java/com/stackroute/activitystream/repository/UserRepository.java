package com.stackroute.activitystream.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.stackroute.activitystream.model.User;

/*
* This class is implementing the CrudRepository interface for User.
* */
public interface UserRepository extends CrudRepository<User, String>{

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
	* This method will validate a user from User table by username and password. 
	* 
	* Write query to validate user using username and password.
	* For example : @Query("select u from User u where u.username = (?1) and u.password = (?2)")
	* */
	@Query("select u from User u where u.username = :username and u.password = :password")
	User validate(@Param("username")String username,@Param("password")String password);
}