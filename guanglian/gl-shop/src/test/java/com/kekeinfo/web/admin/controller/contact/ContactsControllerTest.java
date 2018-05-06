package com.kekeinfo.web.admin.controller.contact;

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

public class ContactsControllerTest extends AbstractController {
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
				.perform(MockMvcRequestBuilders.get("/admin/contact/list.html")
						.with(SecurityRequestPostProcessors.userDeatilsService("admin")))
				.andExpect(MockMvcResultMatchers.model().attributeExists("contact"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("zones"));
		ra.andDo(MockMvcResultHandlers.print());
		MvcResult mr = ra.andReturn();
		System.out.println(">>>>>testListContentPagesByAuth>>>>>" + mr.getResponse().getContentAsString());

	}

	@Test
	public void test2() throws Exception {
		ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.get("/admin/contact/server_processing.shtml")
				.with(SecurityRequestPostProcessors.userDeatilsService("admin"))).andExpect(status().isOk());
		ra.andDo(MockMvcResultHandlers.print());
		MvcResult mr = ra.andReturn();
		System.out.println(">>>>>testListContentPagesByAuth>>>>>" + mr.getResponse().getContentAsString());

	}

	@Test
	public void test3() throws Exception {
		ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.get("/admin/contact/save.shtml")
				.with(SecurityRequestPostProcessors.userDeatilsService("admin"))).andExpect(status().isOk());
		ra.andDo(MockMvcResultHandlers.print());
		MvcResult mr = ra.andReturn();
		System.out.println(">>>>>testListContentPagesByAuth>>>>>" + mr.getResponse().getContentAsString());

	}

	@Test
	public void test4() throws Exception {
		ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.get("/admin/contact/checkPhone.shtml")
				.with(SecurityRequestPostProcessors.userDeatilsService("admin"))).andExpect(status().isOk());
		ra.andDo(MockMvcResultHandlers.print());
		MvcResult mr = ra.andReturn();
		System.out.println(">>>>>testListContentPagesByAuth>>>>>" + mr.getResponse().getContentAsString());

	}

	@Test
	public void test5() throws Exception {
		ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.get("/admin/contact/remove.shtml")
				.with(SecurityRequestPostProcessors.userDeatilsService("admin"))).andExpect(status().isOk());
		ra.andDo(MockMvcResultHandlers.print());
		MvcResult mr = ra.andReturn();
		System.out.println(">>>>>testListContentPagesByAuth>>>>>" + mr.getResponse().getContentAsString());

	}

	@Test
	public void test6() throws Exception {
		ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.get("/admin/contact/zones.shtml")
				.with(SecurityRequestPostProcessors.userDeatilsService("admin"))).andExpect(status().isOk());
		ra.andDo(MockMvcResultHandlers.print());
		MvcResult mr = ra.andReturn();
		System.out.println(">>>>>testListContentPagesByAuth>>>>>" + mr.getResponse().getContentAsString());

	}

	@Test
	public void test7() throws Exception {
		ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.get("/admin/contact/lists.shtml")
				.with(SecurityRequestPostProcessors.userDeatilsService("admin"))).andExpect(status().isOk());
		ra.andDo(MockMvcResultHandlers.print());
		MvcResult mr = ra.andReturn();
		System.out.println(">>>>>testListContentPagesByAuth>>>>>" + mr.getResponse().getContentAsString());

	}

	@Test
	public void test8() throws Exception {
		ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.get("/admin/contact/repassword.shtml")
				.with(SecurityRequestPostProcessors.userDeatilsService("admin"))).andExpect(status().isOk());
		ra.andDo(MockMvcResultHandlers.print());
		MvcResult mr = ra.andReturn();
		System.out.println(">>>>>testListContentPagesByAuth>>>>>" + mr.getResponse().getContentAsString());

	}

	@Test
	public void test9() throws Exception {
		ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.get("/admin/contact/active.shtml")
				.with(SecurityRequestPostProcessors.userDeatilsService("admin"))).andExpect(status().isOk());
		ra.andDo(MockMvcResultHandlers.print());
		MvcResult mr = ra.andReturn();
		System.out.println(">>>>>testListContentPagesByAuth>>>>>" + mr.getResponse().getContentAsString());

	}

	@Test
	public void test10() throws Exception {
		ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.get("/admin/contact/freeze.shtml")
				.with(SecurityRequestPostProcessors.userDeatilsService("admin"))).andExpect(status().isOk());
		ra.andDo(MockMvcResultHandlers.print());
		MvcResult mr = ra.andReturn();
		System.out.println(">>>>>testListContentPagesByAuth>>>>>" + mr.getResponse().getContentAsString());

	}
}