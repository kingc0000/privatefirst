package com.kekeinfo.web.admin.controller;

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

public class AdminControllerTest extends AbstractController {
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
				.perform(MockMvcRequestBuilders.get("/admin/home.html")
						.with(SecurityRequestPostProcessors.userDeatilsService("admin")))
				.andExpect(MockMvcResultMatchers.model().attributeExists("activeMenus"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("store"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("usernum"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("ocount"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("pcount"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("total"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("past12"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("user"));
		ra.andDo(MockMvcResultHandlers.print());
		MvcResult mr = ra.andReturn();
		System.out.println(">>>>>testListContentPagesByAuth>>>>>" + mr.getResponse().getContentAsString());

	}

	@Test
	public void test2() throws Exception {
		ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.get("/admin/locksreen.html")
				.with(SecurityRequestPostProcessors.userDeatilsService("admin")));
		ra.andDo(MockMvcResultHandlers.print());
		MvcResult mr = ra.andReturn();
		System.out.println(">>>>>testListContentPagesByAuth>>>>>" + mr.getResponse().getContentAsString());

	}
}