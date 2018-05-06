package com.kekeinfo.web.admin.controller.content;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.kekeinfo.test.shop.controller.base.AbstractController;
import com.kekeinfo.test.shop.controller.base.SecurityRequestPostProcessors;

public class ContentImageControllerTest extends AbstractController {
	@Autowired
	private FilterChainProxy springSecurityFilterChain;

	@Override
	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).addFilters(this.springSecurityFilterChain).build();
	}

	@Test
	public void test1() throws Exception {
		ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.get("/admin/content/fileBrowser.html")
				.with(SecurityRequestPostProcessors.userDeatilsService("admin")));
		ra.andDo(MockMvcResultHandlers.print());
		MvcResult mr = ra.andReturn();
		System.out.println(">>>>>testListContentPagesByAuth>>>>>" + mr.getResponse().getContentAsString());

	}

	@Test
	public void test2() throws Exception {
		ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.get("/admin/content/image/upload.shtml")
				.with(SecurityRequestPostProcessors.userDeatilsService("admin"))).andExpect(status().isOk());
		ra.andDo(MockMvcResultHandlers.print());
		MvcResult mr = ra.andReturn();
		System.out.println(">>>>>testListContentPagesByAuth>>>>>" + mr.getResponse().getContentAsString());

	}

	@Test
	public void test3() throws Exception {
		ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.get("/admin/content/contentImages.html")
				.with(SecurityRequestPostProcessors.userDeatilsService("admin")));
		ra.andDo(MockMvcResultHandlers.print());
		MvcResult mr = ra.andReturn();
		System.out.println(">>>>>testListContentPagesByAuth>>>>>" + mr.getResponse().getContentAsString());

	}

	@Test
	public void test4() throws Exception {
		ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.get("/admin/content/images/paging.shtml")
				.with(SecurityRequestPostProcessors.userDeatilsService("admin"))).andExpect(status().isOk());
		ra.andDo(MockMvcResultHandlers.print());
		MvcResult mr = ra.andReturn();
		System.out.println(">>>>>testListContentPagesByAuth>>>>>" + mr.getResponse().getContentAsString());

	}

	@Test
	public void test5() throws Exception {
		ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.get("/admin/content/createContentImages.html")
				.with(SecurityRequestPostProcessors.userDeatilsService("admin")));
		ra.andDo(MockMvcResultHandlers.print());
		MvcResult mr = ra.andReturn();
		System.out.println(">>>>>testListContentPagesByAuth>>>>>" + mr.getResponse().getContentAsString());

	}

	@Test
	public void test6() throws Exception {
		ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.get("/admin/content/saveContentImages.html")
				.with(SecurityRequestPostProcessors.userDeatilsService("admin")));
		ra.andDo(MockMvcResultHandlers.print());
		MvcResult mr = ra.andReturn();
		System.out.println(">>>>>testListContentPagesByAuth>>>>>" + mr.getResponse().getContentAsString());

	}

	@Test
	public void test7() throws Exception {
		ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.get("/admin/content/removeImage.shtml")
				.with(SecurityRequestPostProcessors.userDeatilsService("admin"))).andExpect(status().isOk());
		ra.andDo(MockMvcResultHandlers.print());
		MvcResult mr = ra.andReturn();
		System.out.println(">>>>>testListContentPagesByAuth>>>>>" + mr.getResponse().getContentAsString());

	}
}