package com.kekeinfo.web.shop.controller.newsContent;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

public class ShopNewsContentControllerTest extends AbstractController {
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
				.perform(MockMvcRequestBuilders.get("/shop/newscontent/list.html")
						.with(SecurityRequestPostProcessors.userDeatilsService("test")))
				.andExpect(MockMvcResultMatchers.model().attributeExists("requesttype"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("newslist"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("paginationData"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("paginationData"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("paginationData"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("content"));
		ra.andDo(MockMvcResultHandlers.print());
		MvcResult mr = ra.andReturn();
		System.out.println(">>>>>testListContentPagesByAuth>>>>>" + mr.getResponse().getContentAsString());

	}

	@Test
	public void test21() throws Exception {
		ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.get("/shop/newscontent/list.html"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("requesttype"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("newslist"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("paginationData"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("paginationData"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("paginationData"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("content"));
		ra.andDo(MockMvcResultHandlers.print());
		MvcResult mr = ra.andReturn();
		System.out.println(">>>>>testListContentPagesByAuth>>>>>" + mr.getResponse().getContentAsString());

	}

	@Test
	public void test2() throws Exception {
		ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.get("/shop/newscontent/listTab.shtml")
				.with(SecurityRequestPostProcessors.userDeatilsService("test"))).andExpect(status().isOk());
		ra.andDo(MockMvcResultHandlers.print());
		MvcResult mr = ra.andReturn();
		System.out.println(">>>>>testListContentPagesByAuth>>>>>" + mr.getResponse().getContentAsString());

	}

	@Test
	public void test22() throws Exception {
		ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.get("/shop/newscontent/listTab.shtml"))
				.andExpect(status().isOk());
		ra.andDo(MockMvcResultHandlers.print());
		MvcResult mr = ra.andReturn();
		System.out.println(">>>>>testListContentPagesByAuth>>>>>" + mr.getResponse().getContentAsString());

	}

	@Test
	public void test3() throws Exception {
		ResultActions ra = mockMvc
				.perform(MockMvcRequestBuilders.get("/shop/newscontent/view/{newsid}.html")
						.with(SecurityRequestPostProcessors.userDeatilsService("test")))
				.andExpect(MockMvcResultMatchers.model().attributeExists("news"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("content"));
		ra.andDo(MockMvcResultHandlers.print());
		MvcResult mr = ra.andReturn();
		System.out.println(">>>>>testListContentPagesByAuth>>>>>" + mr.getResponse().getContentAsString());

	}

	@Test
	public void test23() throws Exception {
		ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.get("/shop/newscontent/view/{newsid}.html"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("news"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("content"));
		ra.andDo(MockMvcResultHandlers.print());
		MvcResult mr = ra.andReturn();
		System.out.println(">>>>>testListContentPagesByAuth>>>>>" + mr.getResponse().getContentAsString());

	}
}