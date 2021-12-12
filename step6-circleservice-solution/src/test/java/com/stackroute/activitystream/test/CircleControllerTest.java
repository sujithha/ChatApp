package com.stackroute.activitystream.test;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.Timestamp;
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
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stackroute.activitystream.controller.CircleController;
import com.stackroute.activitystream.model.Circle;
import com.stackroute.activitystream.service.CircleService;
import com.stackroute.activitystream.service.CircleServiceImpl;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class CircleControllerTest {

	private MockMvc circleMockMvc;
	private MockMvc userAuthMockMvc;
	
	
	@Mock
	private Circle circle;
	
	@Mock
	private CircleService circleService;
	
	@InjectMocks
	private CircleController circleController=new CircleController();
	
	@Autowired
	private WebApplicationContext webApplicationContext;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		circleMockMvc=MockMvcBuilders.standaloneSetup(circleController)
				.build();
			
	}
	
	@Test
    public void testCreateCircle() throws Exception {
			
		Circle newCircle=new Circle("Spring", "john", null);
		when(circleService.get(anyString())).thenReturn(null);
		when(circleService.save((Circle)any())).thenReturn(true);
		
        circleMockMvc.perform(post("/api/circle")
        		 .contentType(MediaType.APPLICATION_JSON)
                 .content(asJsonString(newCircle)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.circleName", is("Spring")))
                .andExpect(jsonPath("$.creatorId", is("john")));
        verify(circleService, times(1)).save((Circle)any());
        verify(circleService, times(1)).get(anyString());
    }
	
	
	@Test
    public void testCreateDuplicateCircleFailure() throws Exception {
			
		
		Circle newCircle=new Circle("Spring", null, null);
		
		when(circleService.get(newCircle.getCircleName())).thenReturn(newCircle);
        
		circleMockMvc.perform(post("/api/circle")
        		 .contentType(MediaType.APPLICATION_JSON)
                 .content(asJsonString(newCircle)))
                .andExpect(status().isConflict())
                ;
        verify(circleService, times(1)).get(newCircle.getCircleName());
        verify(circleService, times(0)).save(newCircle);
    }
	
	
	
	@Test
    public void testGetAllCircles() throws Exception {
				
		List<Circle> circles=Arrays.asList(
				new Circle("Angular","john",new Timestamp(System.currentTimeMillis())),
				new Circle("Spring","chris",new Timestamp(System.currentTimeMillis())));
		
		when(circleService.getAllCircles()).thenReturn(circles);
		
        circleMockMvc.perform(get("/api/circle"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].circleName", is("Angular")))
                .andExpect(jsonPath("$[0].creatorId", is("john")))
                .andExpect(jsonPath("$[1].circleName", is("Spring")))
                .andExpect(jsonPath("$[1].creatorId", is("chris")));
        verify(circleService, times(1)).getAllCircles(); 
    }
	
	@Test
    public void testGetAllCirclesFailure() throws Exception {
		
		
		verifyZeroInteractions(circleService);
    }
	
	
	@Test
    public void testGetAllMatchingCircles() throws Exception {
			
		List<Circle> circles=Arrays.asList(
				new Circle("Spring","chris",new Timestamp(System.currentTimeMillis())),
				new Circle("Spring Security","chris",new Timestamp(System.currentTimeMillis())));
		
	
		when(circleService.getAllCircles("spring")).thenReturn(circles);
		
        circleMockMvc.perform(get("/api/circle/search/spring"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].circleName", is("Spring")))
                .andExpect(jsonPath("$[0].creatorId", is("chris")))
                .andExpect(jsonPath("$[1].circleName", is("Spring Security")))
                .andExpect(jsonPath("$[1].creatorId", is("chris")));
        verify(circleService, times(1)).getAllCircles(("spring")); 
    }
	
	@Test
    public void testGetAllMatchingCirclesFailure() throws Exception {
     
        verifyZeroInteractions(circleService);
    }
		
	/*
	 * converts a Java object into JSON representation
     */ 
    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
