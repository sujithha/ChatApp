package com.stackroute.activitystream.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.stackroute.activitystream.model.Message;
import com.stackroute.activitystream.model.UserTag;

/*
* This class is implementing the JpaRepository interface for Message.
* */

public interface MessageRepository extends CrudRepository<Message, Integer>{

	/*
	* Apart from the standard CRUD methods already available in JPA Repository, based
	* on our requirements, we might need to create few query methods for getting 
	* specific data from the datasource. Please annotate these methods with @Query 
	* annotation. We can configure the invoked database query by annotating the 
	* query method with the @Query annotation. It supports both JPQL and SQL queries,
	*  and the query that is specified by using the @Query annotation precedes all 
	*  other query generation strategies.
	* */
	
	/*
	* This method will retrieve all messages in database which are posted on the
	* specific circle specified in the method parameter. The messages should come
	* ordered by postedDate in descending order.
	* 
	* Write a query to retrieve all messages from database posted on specific circle.
	* */
	@Query("from Message where circleName = :circleName order by postedDate desc")
	public List<Message> getMessagesFromCircle(@Param("circleName")String circleName);
	
	
	/*
	* This method will retrieve all messages in database which are sent between two
	* specific users specified in the method parameters. The messages should come
	* ordered by postedDate in descending order
	* 
	* Write a query to retrieve all messages from the database send between two specified users. 
	* */
	@Query("from Message where senderName = :username and receiverId = :otherUsername order by postedDate desc")
	public List<Message> getMessagesFromUser(@Param("username")String username, @Param("otherUsername")String otherUsername);
	
	/*
	* This method will retrieve all distinct tags available in all messages and write a query for the same.
	* 
	* */
	@Query("select distinct tag from UserTag")
	public List<String> listAllTags();
	/*
	* This method will retrieve all tags which are subscribed by a specific user and write a query for the same.
	* 
	* */
	@Query("from UserTag where username = :username")
	public List<String> listMyTags(@Param("username")String username);
	
	
	/*
	* This method will retrieve all messages containing tag matching which is 
	* matching the tag in method parameter among all messages and write a query for the same.
	* 
	* */
	@Query("from Message where tag = :tag")
	public List<Message> showMessagesWithTag(@Param("tag")String tag);
	
	
	/*
	* This method will retrieve an UserTag from UserTag table which matches the username
	* and tag in parameter, write a query for the same.
	* */
	@Query("from UserTag where username = :username and tag = :tag")
	public UserTag getUserTag(@Param("username")String username,@Param("tag") String tag);
}