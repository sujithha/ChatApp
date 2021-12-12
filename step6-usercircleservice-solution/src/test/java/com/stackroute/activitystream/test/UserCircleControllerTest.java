package com.stackroute.activitystream.test;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stackroute.activitystream.controller.UserCircleController;
import com.stackroute.activitystream.model.UserCircle;
import com.stackroute.activitystream.service.UserCircleService;
import com.stackroute.activitystream.service.UserCircleServiceImpl;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class UserCircleControllerTest {

	private MockMvc userCircleMockMvc;
	private MockMvc userAuthMockMvc;
	
	
	@Mock
	private UserCircleService userCircleService;
	
	@InjectMocks
	private UserCircleController userCircleController=new UserCircleController();
	
	@Autowired
	private WebApplicationContext webApplicationContext;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		userCircleMockMvc=MockMvcBuilders.standaloneSetup(userCircleController)
				.build();
	}
	
	@Test
    public void testAddUserToCircle() throws Exception {
		
		UserCircle userCircle=new UserCircle("john", "Spring");
		
		when(userCircleService.get("john", "spring")).thenReturn(null);
		when(userCircleService.addUser("john", "spring")).thenReturn(true);
		
		userCircleMockMvc.perform(put("/api/usercircle/addToCircle/john/spring"))
                .andExpect(status().isOk());
          
        verify(userCircleService, times(1)).get("john", "spring");
        verify(userCircleService, times(1)).addUser("john", "spring");
    }
	
	@Test
    public void testAddUserToCircleDuplicateFailure() throws Exception {
		
		
		UserCircle userCircle=new UserCircle("john", "Spring");
		
	
		when(userCircleService.get("john", "spring")).thenReturn(userCircle);
		when(userCircleService.addUser("john", "spring")).thenReturn(true);
		
		userCircleMockMvc.perform(put("/api/usercircle/addToCircle/john/spring"))
                .andExpect(status().isConflict());
                
        verify(userCircleService, times(1)).get("john", "spring");
        verify(userCircleService, times(0)).addUser("john", "spring");
    }
	
	
	
	
	
	
	@Test
    public void testRemoveUserFromCircle() throws Exception {
		
		UserCircle userCircle=new UserCircle("john", "Spring");
		
		when(userCircleService.get("john", "spring")).thenReturn(null);
		when(userCircleService.removeUser("john", "spring")).thenReturn(true);
		
		userCircleMockMvc.perform(put("/api/usercircle/removeFromCircle/john/spring"))
                .andExpect(status().isOk());
                
        verify(userCircleService, times(0)).get("john", "spring");
        verify(userCircleService, times(1)).removeUser("john", "spring");
    }
	
	@Test
    public void testRemoveUserFromCircleFailure() throws Exception {
		
		UserCircle userCircle=new UserCircle("john", "Spring");
		
		
		when(userCircleService.get("john", "spring")).thenReturn(null);
		when(userCircleService.removeUser("john", "spring")).thenReturn(false);
		
		userCircleMockMvc.perform(put("/api/usercircle/removeFromCircle/john/spring"))
                .andExpect(status().isInternalServerError());
                
        
        verify(userCircleService, times(1)).removeUser("john", "spring");
    }
	
	
	@Test
    public void testUserSubscriptionToCircle() throws Exception {
		
		
		UserCircle userCircle=new UserCircle("john", "Spring");
		
		
		when(userCircleService.getMyCircles("john")).thenReturn(Arrays.asList("Spring","Angular"));
		
		userCircleMockMvc.perform(get("/api/usercircle/searchByUser/john"))
                .andExpect(status().isOk());
               
        verify(userCircleService, times(1)).getMyCircles("john");
    }
	
	
	@Test
    public void testUserSubscriptionToCircleFailure() throws Exception {
		
		UserCircle userCircle=new UserCircle("john", "Spring");
		
		
		when(userCircleService.getMyCircles("john")).thenReturn(Arrays.asList("Spring","Angular"));
		
        verify(userCircleService, times(0)).getMyCircles("john");
    }
	
	
     /*converts a Java object into JSON representation*/
     
    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
