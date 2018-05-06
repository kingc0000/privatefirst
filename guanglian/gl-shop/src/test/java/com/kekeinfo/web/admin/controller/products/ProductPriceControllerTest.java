package com.kekeinfo.web.admin.controller.products;

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

public class ProductPriceControllerTest extends AbstractController {
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
				.perform(MockMvcRequestBuilders.get("/admin/products/prices.html")
						.with(SecurityRequestPostProcessors.userDeatilsService("admin")))
				.andExpect(MockMvcResultMatchers.model().attributeExists("product"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("availability"));
		ra.andDo(MockMvcResultHandlers.print());
		MvcResult mr = ra.andReturn();
		System.out.println(">>>>>testListContentPagesByAuth>>>>>" + mr.getResponse().getContentAsString());

	}

	@Test
	public void test2() throws Exception {
		ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.get("/admin/products/prices/paging.html")
				.with(SecurityRequestPostProcessors.userDeatilsService("admin"))).andExpect(status().isOk());
		ra.andDo(MockMvcResultHandlers.print());
		MvcResult mr = ra.andReturn();
		System.out.println(">>>>>testListContentPagesByAuth>>>>>" + mr.getResponse().getContentAsString());

	}

	@Test
	public void test3() throws Exception {
		ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.get("/admin/products/price/edit.html")
				.with(SecurityRequestPostProcessors.userDeatilsService("admin")));
		ra.andDo(MockMvcResultHandlers.print());
		MvcResult mr = ra.andReturn();
		System.out.println(">>>>>testListContentPagesByAuth>>>>>" + mr.getResponse().getContentAsString());

	}

	@Test
	public void test4() throws Exception {
		ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.get("/admin/products/price/create.html")
				.with(SecurityRequestPostProcessors.userDeatilsService("admin")));
		ra.andDo(MockMvcResultHandlers.print());
		MvcResult mr = ra.andReturn();
		System.out.println(">>>>>testListContentPagesByAuth>>>>>" + mr.getResponse().getContentAsString());

	}

	@Test
	public void test5() throws Exception {
		ResultActions ra = mockMvc
				.perform(MockMvcRequestBuilders.get("/admin/products/price/save.html")
						.with(SecurityRequestPostProcessors.userDeatilsService("admin")))
				.andExpect(MockMvcResultMatchers.model().attributeExists("product"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("success"));
		ra.andDo(MockMvcResultHandlers.print());
		MvcResult mr = ra.andReturn();
		System.out.println(">>>>>testListContentPagesByAuth>>>>>" + mr.getResponse().getContentAsString());

	}

	@Test
	public void test6() throws Exception {
		ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.get("/admin/products/price/remove.html")
				.with(SecurityRequestPostProcessors.userDeatilsService("admin"))).andExpect(status().isOk());
		ra.andDo(MockMvcResultHandlers.print());
		MvcResult mr = ra.andReturn();
		System.out.println(">>>>>testListContentPagesByAuth>>>>>" + mr.getResponse().getContentAsString());

	}
}