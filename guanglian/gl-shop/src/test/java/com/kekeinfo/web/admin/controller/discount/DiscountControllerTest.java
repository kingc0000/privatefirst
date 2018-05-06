package com.kekeinfo.web.admin.controller.discount;

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

public class DiscountControllerTest extends AbstractController {
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
				.perform(MockMvcRequestBuilders.get("/admin/discount/store.html")
						.with(SecurityRequestPostProcessors.userDeatilsService("admin")))
				.andExpect(MockMvcResultMatchers.model().attributeExists("dtype"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("dGeneral"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("dGrad"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("nomeo"));
		ra.andDo(MockMvcResultHandlers.print());
		MvcResult mr = ra.andReturn();
		System.out.println(">>>>>testListContentPagesByAuth>>>>>" + mr.getResponse().getContentAsString());

	}

	@Test
	public void test2() throws Exception {
		ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.get("/admin/discount/save.shtml")
				.with(SecurityRequestPostProcessors.userDeatilsService("admin"))).andExpect(status().isOk());
		ra.andDo(MockMvcResultHandlers.print());
		MvcResult mr = ra.andReturn();
		System.out.println(">>>>>testListContentPagesByAuth>>>>>" + mr.getResponse().getContentAsString());

	}

	@Test
	public void test3() throws Exception {
		ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.get("/admin/discount/saveGrad.shtml")
				.with(SecurityRequestPostProcessors.userDeatilsService("admin"))).andExpect(status().isOk());
		ra.andDo(MockMvcResultHandlers.print());
		MvcResult mr = ra.andReturn();
		System.out.println(">>>>>testListContentPagesByAuth>>>>>" + mr.getResponse().getContentAsString());

	}

	@Test
	public void test4() throws Exception {
		ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.get("/admin/discount/deleteGrad.shtml")
				.with(SecurityRequestPostProcessors.userDeatilsService("admin"))).andExpect(status().isOk());
		ra.andDo(MockMvcResultHandlers.print());
		MvcResult mr = ra.andReturn();
		System.out.println(">>>>>testListContentPagesByAuth>>>>>" + mr.getResponse().getContentAsString());

	}

	@Test
	public void test5() throws Exception {
		ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.get("/admin/discount/mans.shtml")
				.with(SecurityRequestPostProcessors.userDeatilsService("admin"))).andExpect(status().isOk());
		ra.andDo(MockMvcResultHandlers.print());
		MvcResult mr = ra.andReturn();
		System.out.println(">>>>>testListContentPagesByAuth>>>>>" + mr.getResponse().getContentAsString());

	}

	@Test
	public void test6() throws Exception {
		ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.get("/admin/discount/cates.shtml")
				.with(SecurityRequestPostProcessors.userDeatilsService("admin"))).andExpect(status().isOk());
		ra.andDo(MockMvcResultHandlers.print());
		MvcResult mr = ra.andReturn();
		System.out.println(">>>>>testListContentPagesByAuth>>>>>" + mr.getResponse().getContentAsString());

	}
}