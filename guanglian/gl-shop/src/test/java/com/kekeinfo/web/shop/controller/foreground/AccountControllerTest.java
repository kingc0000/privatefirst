package com.kekeinfo.web.shop.controller.foreground;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.kekeinfo.test.shop.controller.base.AbstractController;
import com.kekeinfo.test.shop.controller.base.SecurityRequestPostProcessors;

public class AccountControllerTest extends AbstractController {
	@Autowired
	private FilterChainProxy springSecurityFilterChain;

	@Override
	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).addFilters(this.springSecurityFilterChain).build();
	}

	@Test
	public void test1() throws Exception {
		ResultActions ra = mockMvc
				.perform(MockMvcRequestBuilders.get("/shop/customer/home.html")
						.with(SecurityRequestPostProcessors.userDeatilsService("test")))
				.andExpect(MockMvcResultMatchers.model().attributeExists("dcount"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("pcount"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("rcount"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("total"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("orders"));
		ra.andDo(MockMvcResultHandlers.print());
		MvcResult mr = ra.andReturn();
		System.out.println(">>>>>testListContentPagesByAuth>>>>>" + mr.getResponse().getContentAsString());

	}

	@Test
	public void test21() throws Exception {
		ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.get("/shop/customer/home.html"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("dcount"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("pcount"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("rcount"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("total"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("orders"));
		ra.andDo(MockMvcResultHandlers.print());
		MvcResult mr = ra.andReturn();
		System.out.println(">>>>>testListContentPagesByAuth>>>>>" + mr.getResponse().getContentAsString());

	}

	@Test
	public void test2() throws Exception {
		ResultActions ra = mockMvc
				.perform(MockMvcRequestBuilders.get("/shop/customer/logon.html")
						.with(SecurityRequestPostProcessors.userDeatilsService("test")))
				.andExpect(MockMvcResultMatchers.model().attributeExists("fuser"));
		ra.andDo(MockMvcResultHandlers.print());
		MvcResult mr = ra.andReturn();
		System.out.println(">>>>>testListContentPagesByAuth>>>>>" + mr.getResponse().getContentAsString());

	}

	@Test
	public void test22() throws Exception {
		ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.get("/shop/customer/logon.html"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("fuser"));
		ra.andDo(MockMvcResultHandlers.print());
		MvcResult mr = ra.andReturn();
		System.out.println(">>>>>testListContentPagesByAuth>>>>>" + mr.getResponse().getContentAsString());

	}

	@Test
	public void test3() throws Exception {
		ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.get("/shop/customer/customLogin.html")
				.with(SecurityRequestPostProcessors.userDeatilsService("test")));
		ra.andDo(MockMvcResultHandlers.print());
		MvcResult mr = ra.andReturn();
		System.out.println(">>>>>testListContentPagesByAuth>>>>>" + mr.getResponse().getContentAsString());

	}

	@Test
	public void test23() throws Exception {
		ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.get("/shop/customer/customLogin.html"));
		ra.andDo(MockMvcResultHandlers.print());
		MvcResult mr = ra.andReturn();
		System.out.println(">>>>>testListContentPagesByAuth>>>>>" + mr.getResponse().getContentAsString());

	}

	@Test
	public void test4() throws Exception {
		ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.get("/shop/customer/login.html")
				.with(SecurityRequestPostProcessors.userDeatilsService("test")));
		ra.andDo(MockMvcResultHandlers.print());
		MvcResult mr = ra.andReturn();
		System.out.println(">>>>>testListContentPagesByAuth>>>>>" + mr.getResponse().getContentAsString());

	}

	@Test
	public void test24() throws Exception {
		ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.get("/shop/customer/login.html"));
		ra.andDo(MockMvcResultHandlers.print());
		MvcResult mr = ra.andReturn();
		System.out.println(">>>>>testListContentPagesByAuth>>>>>" + mr.getResponse().getContentAsString());

	}
}