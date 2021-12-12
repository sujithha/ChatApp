package com.stackroute.activitystream.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stackroute.activitystream.model.Message;
import com.stackroute.activitystream.model.UserTag;
import com.stackroute.activitystream.service.MessageService;
import com.stackroute.activitystream.service.MessageServiceImpl;
/*
 * As in this assignment, we are working with creating RESTful web service, hence annotate
 * the class with @RestController annotation.A class annotated with @Controller annotation
 * has handler methods which returns a view. However, if we use @ResponseBody annotation along
 * with @Controller annotation, it will return the data directly in a serialized 
 * format. Starting from Spring 4 and above, we can use @RestController annotation which 
 * is equivalent to using @Controller and @ResposeBody annotation
 */
@RestController
@RequestMapping("/api/message")
public class MessageController {
	/*
	 * From the problem statement, we can understand that the application	 requires us to 
	 * implement nine functionalities regarding circles. They are as following:
	 * 
	 * 1. Send message to circle 
	 * 2. Send message to users
	 * 3. Retrieve message from users
	 * 4. Retrieve message from circles
	 * 5. Retrieve all tags
	 * 6. Retrieve messages containing a specific tag
	 * 7. Subscribe a user to stream containing a specific tag
	 * 8. Unsubscribe a user from a stream containing a specific tag
	 * 9. Retrieve the set of tags subscribed by a specific user
	 * 
	 * we must also ensure that only a user who is logged in should be able to perform the
	 * functionalities mentioned above.
	 * 
	 */
	
	/*
	 * Autowiring should be implemented for the MessageService. Please note that 
	 * we should not create any object using the new keyword
	 * */
	@Autowired
	private MessageService messageService;
	
	
	/* Define a handler method which will send a message to a circle by reading the Serialized message
	 * object from request body and save the message in message table in database. Please 
	 * note that the loggedIn userID should be taken as the senderId for the message. 
	 * This handler method should return any one of the status messages basis on different
	 * situations:
	 * 1. 200(OK) - If the message is sent successfully
	 * 2. 500(INTERNAL SERVER ERROR) - If the message could not be sent
	 * 
	 * This handler method should map to the URL "/api/message/sendMessageToCircle/{circleName}" using HTTP POST method"
	 * where "circleName" should be replaced by the destination circle name without {} 
	*/
	
	@PostMapping(value = "/sendMessageToCircle/{circleName}")
	public ResponseEntity<Message> sendMessageToCircle(@PathVariable("circleName")String circleName,@RequestBody Message message){
		boolean isSent = messageService.sendMessageToCircle(circleName, message);
		if(isSent) {
			return new ResponseEntity<Message>(HttpStatus.OK);
		}
			return new ResponseEntity<Message>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	/* Define a handler method which will send a message to an individual user by reading the Serialized message
	 * object from request body and save the message in message table in database. Please 
	 * note that the loggedIn userID should be taken as the senderId for the message. 
	 * This handler method should return any one of the status messages basis on different
	 * situations:
	 * 1. 200(OK) - If the message is sent successfully
	 * 2. 500(INTERNAL SERVER ERROR) - If the message could not be sent
	 * 
	 * This handler method should map to the URL "/api/message/sendMessageToUser/{receiverId}" using HTTP POST method"
	 * where "receiverId" should be replaced by the recipient user name without {} 
	*/
	
	@PostMapping(value ="/sendMessageToUser/{receiverId}")
	public ResponseEntity<Message> sendMessageToUser(@PathVariable("receiverId")String receiverId, @RequestBody Message message){
		boolean isSent = messageService.sendMessageToUser(receiverId, message);
		if(!isSent) {
			return new ResponseEntity<Message>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
			return new ResponseEntity<Message>(HttpStatus.OK);
	}
	
	/* Define a handler method which will get all messages sent by a specific user to another specific user. Please 
	 * note that there can be huge number of messages which has been transmitted between two users. Hence, retrieving
	 * messages partially will help to improve performance. Pagination can be implemented here. 
	 * This handler method should return any one of the status messages basis on different
	 * situations:
	 * 1. 200(OK) - If the messages are retrieved successfully(provided that the messages exist)
	 * 
	 * This handler method should map to the URL 
	 * "/api/message/getMessagesByUser/{senderUsername}/{receiverUserName}/{pageNumber}" 
	 * using HTTP GET method"
	 * where "senderUsername" should be replaced by a valid user name without {}
	 * and "receiverUsername" should be replaced by a valid user name without {}
	 * and "pageNumber" should be replaced by the numeric page number that we are looking for without {}
	*/
	@GetMapping(value = "/getMessagesByUser/{senderUsername}/{receiverUserName}/{pageNumber}")
	public ResponseEntity<List<Message>> getMessagesByUser(@PathVariable("senderUsername")String senderUserName,@PathVariable("receiverUserName")String receiverUserName, @PathVariable("pageNumber")int pageNumber){
		return new ResponseEntity<List<Message>>(messageService.getMessagesFromUser(senderUserName, receiverUserName, pageNumber),HttpStatus.OK);
	}
	
	
	/* Define a handler method which will get all messages sent to a specific circle by all users. Please 
	 * note that there can be huge number of messages which has been transmitted to a circle. Hence, retrieving
	 * messages partially will help to improve performance. Pagination can be implemented here. 
	 * This handler method should return any one of the status messages basis on different
	 * situations:
	 * 1. 200(OK) - If the messages are retrieved(if the messages exist)
	 * 
	 * This handler method should map to the URL 
	 * "/api/message/getMessagesByUser/{circleName}/{pageNumber}" 
	 * using HTTP GET method"
	 * where "circleName" should be replaced by a valid user name without {}
	 * and "pageNumber" should be replaced by the numeric page number that we are looking for without {}
	*/
	@GetMapping(value = "/getMessagesByCircle/{circleName}/{pageNumber}")
	public ResponseEntity<List<Message>> getMessagesByCircle(@PathVariable("circleName")String circleName, @PathVariable("pageNumber")int pageNumber){
		return new ResponseEntity<List<Message>>(messageService.getMessagesFromCircle(circleName, pageNumber),HttpStatus.OK);
	}
	
	
	/* As per our problem statement, each message can have some tags. We will learn how to extract the tags from 
	 * the messages in future, but here we would like to define a handler method which will get all tags which has been
	 * extracted from all messages.  
	 * This handler method should return any one of the status messages basis on different
	 * situations:
	 * 1. 200(OK) - If the tags are retrieved successfully
	 * 
	 * This handler method should map to the URL 
	 * "/api/message/listAllTags" using HTTP GET method"
	 
	*/
	@GetMapping(value="/listAllTags")
	public ResponseEntity<List<String>> listAllTags(){
		return new ResponseEntity<List<String>>(messageService.listTags(),HttpStatus.OK);
	}
	
	
	
	/* Define a handler method which will get all messages containing a specific tag. Please note that there 
	 * can be huge number of messages which has the same tag. Hence, retrieving
	 * messages partially will help to improve performance. Pagination can be implemented here. 
	 * This handler method should return any one of the status messages basis on different
	 * situations:
	 * 1. 200(OK) - If the message is sent successfully
	 * 
	 * This handler method should map to the URL 
	 * "/api/message/showMessagesWithTag/{tag}/{pageNumber}" 
	 * using HTTP GET method"
	 * where "tag" should be replaced by a tag(string) without {}
	 * and "pageNumber" should be replaced by the numeric page number that we are looking for without {}
	*/
	@GetMapping(value ="/showMessagesWithTag/{tag}/{pageNumber}")
	public ResponseEntity<List<Message>> showMessagesWithTag(@PathVariable("tag")String tag, @PathVariable("pageNumber")int pageNumber){
		return new ResponseEntity<List<Message>>(messageService.showMessagesWithTag(tag, pageNumber),HttpStatus.OK);
	}
	
	/* As per our problem statement, user can subscribe to one or more tag(s). Hence, the user will be able to see all
	 * messages containing those tags. Define a handler method which will subscribe a specific user a specific tag. 
	 * This handler method should return any one of the status messages basis on different
	 * situations:
	 * 1. 200(OK) - If the user has subscribed to the tag successfully
	 * 2. 500(INTERNAL SERVER ERROR) - In case the user could not be subscribed. For eg: if the the user is already 
	 *   subscribed to the tag 
	 * 
	 * This handler method should map to the URL 
	 * "/api/message/subscribe/{username}/{tag}" 
	 * using HTTP PUT method"
	 * where "username" should be replaced by a valid user name without {}
	 * and "tag" should be replaced by a valid tag without {}
	*/
	
	@PutMapping(value = "/subscribe/{username}/{tag}")
	public ResponseEntity<UserTag> subscribeTag (@PathVariable("username")String username, @PathVariable("tag")String tag){
		boolean isSubscribed = messageService.subscribeUserToTag(username, tag);
		if(!isSubscribed) {
			return new ResponseEntity<UserTag>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
			return new ResponseEntity<UserTag>(HttpStatus.OK);
	}
	
	/* As per our problem statement, user can unsubscribe from one or more tag(s). Define a handler method which 
	 * will unsubscribe a specific user from a specific tag. 
	 * This handler method should return any one of the status messages basis on different
	 * situations:
	 * 1. 200(OK) - If the user has unsubscribed from the tag successfully
	 * 3. 500(INTERNAL SERVER ERROR) - In case the user could not be unsubscribed. For eg: if the the user is not 
	 *   subscribed to the tag 
	 * 
	 * This handler method should map to the URL 
	 * "/api/message/unsubscribe/{username}/{tag}" 
	 * using HTTP PUT method"
	 * where "username" should be replaced by a valid user name without {}
	 * and "tag" should be replaced by a valid tag without {}
	*/
	
	@PutMapping(value = "/unsubscribe/{username}/{tag}")
	public ResponseEntity<UserTag> unsubscribeTag (@PathVariable("username")String username, @PathVariable("tag")String tag){
		boolean isUnsubscribed = messageService.unsubscribeUserToTag(username, tag);
		if(!isUnsubscribed) {
			return new ResponseEntity<UserTag>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
			return new ResponseEntity<UserTag>(HttpStatus.OK);
	}
	
	
	/* Define a handler method which will show all the subscribed tags by a specific user. 
	 * This handler method should return any one of the status messages basis on different
	 * situations:
	 * 1. 200(OK) - If the user has subscribed to the tag successfully
	 * 
	 * This handler method should map to the URL 
	 * "/api/message/tags/search/user/{username}" 
	 * using HTTP GET method"
	 * where "username" should be replaced by a valid user name without {}
	*/
	@GetMapping(value = "/tags/search/user/{username}")
	public ResponseEntity<List<String>> searchTag(@PathVariable("username")String username){
		return new ResponseEntity<List<String>>(messageService.listMyTags(username), HttpStatus.OK);
	}
	
	}