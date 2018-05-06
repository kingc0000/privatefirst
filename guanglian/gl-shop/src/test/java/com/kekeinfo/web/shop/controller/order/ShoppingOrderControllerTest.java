package com.kekeinfo.web.shop.controller.order;

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

public class ShoppingOrderControllerTest extends AbstractController {
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
				.perform(MockMvcRequestBuilders.get("/shop/order/checkout.html")
						.with(SecurityRequestPostProcessors.userDeatilsService("test")))
				.andExpect(MockMvcResultMatchers.model().attributeExists("address"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("shipping"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("adefault"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("billing"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("invoice"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("idefault"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("cart"));
		ra.andDo(MockMvcResultHandlers.print());
		MvcResult mr = ra.andReturn();
		System.out.println(">>>>>testListContentPagesByAuth>>>>>" + mr.getResponse().getContentAsString());

	}

	@Test
	public void test21() throws Exception {
		ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.get("/shop/order/checkout.html"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("address"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("shipping"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("adefault"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("billing"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("invoice"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("idefault"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("cart"));
		ra.andDo(MockMvcResultHandlers.print());
		MvcResult mr = ra.andReturn();
		System.out.println(">>>>>testListContentPagesByAuth>>>>>" + mr.getResponse().getContentAsString());

	}

	@Test
	public void test2() throws Exception {
		ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.get("/shop/order/addres.shtml")
				.with(SecurityRequestPostProcessors.userDeatilsService("test"))).andExpect(status().isOk());
		ra.andDo(MockMvcResultHandlers.print());
		MvcResult mr = ra.andReturn();
		System.out.println(">>>>>testListContentPagesByAuth>>>>>" + mr.getResponse().getContentAsString());

	}

	@Test
	public void test22() throws Exception {
		ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.get("/shop/order/addres.shtml"))
				.andExpect(status().isOk());
		ra.andDo(MockMvcResultHandlers.print());
		MvcResult mr = ra.andReturn();
		System.out.println(">>>>>testListContentPagesByAuth>>>>>" + mr.getResponse().getContentAsString());

	}

	@Test
	public void test3() throws Exception {
		ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.get("/shop/order/invoice.shtml")
				.with(SecurityRequestPostProcessors.userDeatilsService("test"))).andExpect(status().isOk());
		ra.andDo(MockMvcResultHandlers.print());
		MvcResult mr = ra.andReturn();
		System.out.println(">>>>>testListContentPagesByAuth>>>>>" + mr.getResponse().getContentAsString());

	}

	@Test
	public void test23() throws Exception {
		ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.get("/shop/order/invoice.shtml"))
				.andExpect(status().isOk());
		ra.andDo(MockMvcResultHandlers.print());
		MvcResult mr = ra.andReturn();
		System.out.println(">>>>>testListContentPagesByAuth>>>>>" + mr.getResponse().getContentAsString());

	}

	@Test
	public void test4() throws Exception {
		ResultActions ra = mockMvc
				.perform(MockMvcRequestBuilders.get("/shop/order/commitOrder.html")
						.with(SecurityRequestPostProcessors.userDeatilsService("test")))
				.andExpect(MockMvcResultMatchers.model().attributeExists("information"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("image"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("orderid"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("orderMessage"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("information"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("image"));
		ra.andDo(MockMvcResultHandlers.print());
		MvcResult mr = ra.andReturn();
		System.out.println(">>>>>testListContentPagesByAuth>>>>>" + mr.getResponse().getContentAsString());

	}

	@Test
	public void test24() throws Exception {
		ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.get("/shop/order/commitOrder.html"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("information"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("image"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("orderid"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("orderMessage"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("information"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("image"));
		ra.andDo(MockMvcResultHandlers.print());
		MvcResult mr = ra.andReturn();
		System.out.println(">>>>>testListContentPagesByAuth>>>>>" + mr.getResponse().getContentAsString());

	}
}