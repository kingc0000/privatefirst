package com.kekeinfo.test.shop.controller.base;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value="src/main/webapp")
@ContextConfiguration(locations = { "classpath:spring/test-spring-context.xml", 
		"file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml",
		"file:src/main/webapp/WEB-INF/spring/appServlet/guanglian-security.xml"})
public abstract class AbstractController {

	@Autowired
	protected WebApplicationContext wac;

	protected MockMvc mockMvc;
	
	@Before
	public void setup() {
		//mockMvc = MockMvcBuilders.standaloneSetup(contentPagesController).build();
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}

}

