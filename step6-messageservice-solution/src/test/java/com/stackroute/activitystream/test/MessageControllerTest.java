package com.stackroute.activitystream.test;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stackroute.activitystream.controller.MessageController;
import com.stackroute.activitystream.model.Message;
import com.stackroute.activitystream.service.MessageService;
import com.stackroute.activitystream.service.MessageServiceImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class MessageControllerTest {

	private MockMvc messageMockMvc;
	private MockMvc userAuthMockMvc;

	@Mock
	private Message message;

	@Mock
	private MessageService messageService;

	@InjectMocks
	private MessageController messageController = new MessageController();

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		messageMockMvc = MockMvcBuilders.standaloneSetup(messageController).build();

	}

	@Test
	public void testSendMessageToCircle() throws Exception {

		Message newMessage = new Message("john", null, null, null, "text", "Sample Message", null);

		when(messageService.sendMessageToCircle(anyString(), any())).thenReturn(true);

		messageMockMvc
				.perform(post("/api/message/sendMessageToCircle/spring").contentType(MediaType.APPLICATION_JSON)
						.content(asJsonString(newMessage)))

				.andExpect(status().isOk());

		verify(messageService, times(1)).sendMessageToCircle(anyString(), any());
	}

	@Test
	public void testSendMessageToCircleFailure() throws Exception {

		Message newMessage = new Message("john", null, null, null, "text", "Sample Message", null);

		when(messageService.sendMessageToCircle(anyString(), any())).thenReturn(false);

		messageMockMvc.perform(post("/api/message/sendMessageToCircle/spring").contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(newMessage))).andExpect(status().isInternalServerError());

		verify(messageService, times(1)).sendMessageToCircle(anyString(), any());
	}

	@Test
	public void testSendMessageToUser() throws Exception {

		Message newMessage = new Message("john", "chris", null, null, "text", "Hello!", null);

		when(messageService.sendMessageToUser(anyString(), any())).thenReturn(true);

		messageMockMvc.perform(post("/api/message/sendMessageToUser/chris").contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(newMessage))).andExpect(status().isOk());

		verify(messageService, times(1)).sendMessageToUser(anyString(), any());
	}

	@Test
	public void testSendMessageToUserFailure() throws Exception {

		Message newMessage = new Message("john", "chris", null, null, "text", "Hello!", null);

		when(messageService.sendMessageToUser(anyString(), any())).thenReturn(false);

		messageMockMvc.perform(post("/api/message/sendMessageToUser/chris").contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(newMessage))).andExpect(status().isInternalServerError());

		verify(messageService, times(1)).sendMessageToUser(anyString(), any());
	}

	@Test
	public void testGetMessagesByUser() throws Exception {

		List<Message> messages = Arrays.asList(new Message("john", "chris", null, null, "text", "First Message", null),
				new Message("john", "chris", null, null, "text", "Second Message", null),
				new Message("john", "chris", null, null, "text", "Third Message", null));

		when(messageService.getMessagesFromUser(anyString(), anyString(), anyInt())).thenReturn(messages);

		messageMockMvc.perform(get("/api/message/getMessagesByUser/john/chris/1"))
				.andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(3)))
				.andExpect(jsonPath("$[0].senderName", is("john"))).andExpect(jsonPath("$[0].receiverId", is("chris")))
				.andExpect(jsonPath("$[0].streamType", is("text")))
				.andExpect(jsonPath("$[0].message", is("First Message")))
				.andExpect(jsonPath("$[1].senderName", is("john"))).andExpect(jsonPath("$[1].receiverId", is("chris")))
				.andExpect(jsonPath("$[1].streamType", is("text")))
				.andExpect(jsonPath("$[1].message", is("Second Message")))
				.andExpect(jsonPath("$[2].senderName", is("john"))).andExpect(jsonPath("$[2].receiverId", is("chris")))
				.andExpect(jsonPath("$[2].streamType", is("text")))
				.andExpect(jsonPath("$[2].message", is("Third Message")));

		verify(messageService, times(1)).getMessagesFromUser(anyString(), anyString(), anyInt());
	}

	@Test
	public void testGetMessagesByUserFailure() throws Exception {

		List<Message> messages = Arrays.asList(new Message("john", "chris", null, null, "text", "First Message", null),
				new Message("john", "chris", null, null, "text", "Second Message", null),
				new Message("john", "chris", null, null, "text", "Third Message", null));

		when(messageService.getMessagesFromUser(anyString(), anyString(), anyInt())).thenReturn(null);

		verify(messageService, times(0)).getMessagesFromUser(anyString(), anyString(), anyInt());
	}

	@Test
	public void testGetMessagesByCircle() throws Exception {

		List<Message> messages = Arrays.asList(new Message("john", null, "Spring", null, "text", "First Message", null),
				new Message("john", null, "Spring", null, "text", "Second Message", null),
				new Message("john", null, "Spring", null, "text", "Third Message", null));

		when(messageService.getMessagesFromCircle(anyString(), anyInt())).thenReturn(messages);

		messageMockMvc.perform(get("/api/message/getMessagesByCircle/spring/1")).andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(3))).andExpect(jsonPath("$[0].senderName", is("john")))
				.andExpect(jsonPath("$[0].circleName", is("Spring"))).andExpect(jsonPath("$[0].streamType", is("text")))
				.andExpect(jsonPath("$[0].message", is("First Message")))
				.andExpect(jsonPath("$[1].senderName", is("john"))).andExpect(jsonPath("$[1].circleName", is("Spring")))
				.andExpect(jsonPath("$[1].streamType", is("text")))
				.andExpect(jsonPath("$[1].message", is("Second Message")))
				.andExpect(jsonPath("$[2].senderName", is("john"))).andExpect(jsonPath("$[2].circleName", is("Spring")))
				.andExpect(jsonPath("$[2].streamType", is("text")))
				.andExpect(jsonPath("$[2].message", is("Third Message")));

		verify(messageService, times(1)).getMessagesFromCircle(anyString(), anyInt());
	}

	@Test
	public void testGetMessagesByCircleFailure() throws Exception {

		List<Message> messages = Arrays.asList(new Message("john", null, "Spring", null, "text", "First Message", null),
				new Message("john", null, "Spring", null, "text", "Second Message", null),
				new Message("john", null, "Spring", null, "text", "Third Message", null));

		when(messageService.getMessagesFromCircle(anyString(), anyInt())).thenReturn(messages);

		verify(messageService, times(0)).getMessagesFromCircle(anyString(), anyInt());
	}

	@Test
	public void testListAllTags() throws Exception {

		when(messageService.listTags()).thenReturn(Arrays.asList("angular", "spring", "java"));
		messageMockMvc.perform(get("/api/message/listAllTags")).andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(3))).andExpect(jsonPath("$[0]", is("angular")))
				.andExpect(jsonPath("$[1]", is("spring"))).andExpect(jsonPath("$[2]", is("java")));

		verify(messageService, times(1)).listTags();
	}

	@Test
	public void testListAllTagsFailure() throws Exception {

		when(messageService.listTags()).thenReturn(Arrays.asList("angular", "spring", "java"));

		verify(messageService, times(0)).listTags();
	}

	@Test
	public void testShowMessagesWithTag() throws Exception {

		List<Message> messages = Arrays.asList(
				new Message("john", null, "Spring", null, "text", "First Message", "spring"),
				new Message("john", null, "Spring", null, "text", "Second Message", "spring"),
				new Message("john", null, "Spring", null, "text", "Third Message", "spring"));

		when(messageService.showMessagesWithTag(anyString(), anyInt())).thenReturn(messages);
		messageMockMvc.perform(get("/api/message/showMessagesWithTag/spring/1"))
				.andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(3)))
				.andExpect(jsonPath("$[0].senderName", is("john"))).andExpect(jsonPath("$[0].circleName", is("Spring")))
				.andExpect(jsonPath("$[0].streamType", is("text")))
				.andExpect(jsonPath("$[0].message", is("First Message"))).andExpect(jsonPath("$[0].tag", is("spring")))
				.andExpect(jsonPath("$[1].senderName", is("john"))).andExpect(jsonPath("$[1].circleName", is("Spring")))
				.andExpect(jsonPath("$[1].streamType", is("text")))
				.andExpect(jsonPath("$[1].message", is("Second Message"))).andExpect(jsonPath("$[1].tag", is("spring")))
				.andExpect(jsonPath("$[2].senderName", is("john"))).andExpect(jsonPath("$[2].circleName", is("Spring")))
				.andExpect(jsonPath("$[2].streamType", is("text")))
				.andExpect(jsonPath("$[2].message", is("Third Message"))).andExpect(jsonPath("$[2].tag", is("spring")));

		verify(messageService, times(1)).showMessagesWithTag(anyString(), anyInt());
	}

	@Test
	public void testShowMessagesWithTagFailure() throws Exception {

		List<Message> messages = Arrays.asList(
				new Message("john", null, "Spring", null, "text", "First Message", "spring"),
				new Message("john", null, "Spring", null, "text", "Second Message", "spring"),
				new Message("john", null, "Spring", null, "text", "Third Message", "spring"));

		verify(messageService, times(0)).showMessagesWithTag(anyString(), anyInt());
	}

	@Test
	public void testUserSubscriptionToCircle() throws Exception {

		when(messageService.subscribeUserToTag(anyString(), anyString())).thenReturn(true);
		messageMockMvc.perform(put("/api/message/subscribe/john/spring")).andExpect(status().isOk());

		verify(messageService, times(1)).subscribeUserToTag(anyString(), anyString());
	}

	@Test
	public void testUserSubscriptionToCircleFailure() throws Exception {

		when(messageService.subscribeUserToTag(anyString(), anyString())).thenReturn(false);
		messageMockMvc.perform(put("/api/message/subscribe/john/spring"))
				.andExpect(status().isInternalServerError());

		verify(messageService, times(1)).subscribeUserToTag(anyString(), anyString());
	}

	@Test
	public void testUserUnsubscriptionToCircle() throws Exception {

		when(messageService.unsubscribeUserToTag(anyString(), anyString())).thenReturn(true);
		messageMockMvc.perform(put("/api/message/unsubscribe/john/spring"))
				.andExpect(status().isOk());

		verify(messageService, times(1)).unsubscribeUserToTag(anyString(), anyString());
	}

	@Test
	public void testUserUnsubscriptionToCircleFailure() throws Exception {

		when(messageService.unsubscribeUserToTag(anyString(), anyString())).thenReturn(false);
		messageMockMvc.perform(put("/api/message/unsubscribe/john/spring"))

				.andExpect(status().isInternalServerError());

		verify(messageService, times(1)).unsubscribeUserToTag(anyString(), anyString());
	}

	@Test
	public void testRetrieveSubscribedTags() throws Exception {

		when(messageService.listMyTags(anyString())).thenReturn(Arrays.asList("angular", "spring"));
		messageMockMvc.perform(get("/api/message/tags/search/user/john"))

				.andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0]", is("angular"))).andExpect(jsonPath("$[1]", is("spring")));

		verify(messageService, times(1)).listMyTags(anyString());
	}

	@Test
	public void testRetrieveSubscribedTagsFailure() throws Exception {
		verify(messageService, times(0)).listMyTags(anyString());
	}

	/* converts a Java object into JSON representation */

	public static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
